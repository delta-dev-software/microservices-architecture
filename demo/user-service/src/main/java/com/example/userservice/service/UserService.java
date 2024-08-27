package com.example.userservice.service;

import com.example.userservice.config.RabbitMQConfig;
import com.example.userservice.domain.Role;
import com.example.userservice.domain.User;
import com.example.userservice.dto.UserDTO;
import com.example.userservice.mapper.UserMapper;
import com.example.userservice.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RabbitTemplate rabbitTemplate;
    private final RedisTemplate<String, Object> redisTemplate;
    private final BCryptPasswordEncoder passwordEncoder;
    private  UserMapper userMapper=UserMapper.INSTANCE;

    public UserService(UserRepository userRepository, RabbitTemplate rabbitTemplate, RedisTemplate<String, Object> redisTemplate, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.rabbitTemplate = rabbitTemplate;
        this.redisTemplate = redisTemplate;
        this.passwordEncoder = passwordEncoder;
    }

    private static final String REDIS_USER_KEY_PREFIX = "USER_";

    @PostConstruct
    public void init() {
        // Initialization logic, if needed
        System.out.println("UserService initialized");
    }

    @PreDestroy
    public void cleanup() {
        // Cleanup logic, if needed
        System.out.println("UserService destroyed");
    }

    //@Async
    public UserDTO registerUser(String name, String email, String password) {

        Role adminRole = new Role("ADMIN");
        Role userRole = new Role("USER");

        Set<Role> roles = new HashSet<>();
        roles.add(adminRole);
        roles.add(userRole);

        User user = User.builder()
                .name(name)
                .email(email)
                .password(passwordEncoder.encode(password))
                .roles(roles)
                .build();
        User savedUser = userRepository.save(user);

        // Store user in Redis
        redisTemplate.opsForValue().set(REDIS_USER_KEY_PREFIX + savedUser.getId(), userMapper.userToUserDTO(savedUser));

        // Send message to RabbitMQ asynchronously
        UserDTO userDTO = userMapper.userToUserDTO(savedUser);
        rabbitTemplate.convertAndSend(RabbitMQConfig.QUEUE_NAME, userDTO);

        return userDTO;
    }

    @Cacheable(value = "users", key = "#email")
    public Optional<UserDTO> findByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(userMapper::userToUserDTO);
    }

    @Cacheable(value = "users", key = "#userId")
    public Optional<UserDTO> findById(UUID userId) {
        // Try to get user from Redis first
        String redisKey = REDIS_USER_KEY_PREFIX + userId;
        UserDTO cachedUser = (UserDTO) redisTemplate.opsForValue().get(redisKey);

        if (cachedUser != null && cachedUser.getName().equals("anis")) {
            return Optional.of(cachedUser);
        }

        // If user is not in Redis, retrieve from DB and store in Redis
        return userRepository.findById(userId)
                .map(user -> {
                    UserDTO userDTO = userMapper.userToUserDTO(user);
                    redisTemplate.opsForValue().set(redisKey, userDTO);
                    return userDTO;
                });
    }
}
