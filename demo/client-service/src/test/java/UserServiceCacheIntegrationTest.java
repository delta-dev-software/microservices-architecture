

import clientservice.application.service.UserServiceCache;
import clientservice.domain.model.User;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

//@SpringBootTest
@Testcontainers
public class UserServiceCacheIntegrationTest {

    @Container
    private static final GenericContainer<?> redisContainer = new GenericContainer<>("redis:6.0.9")
            .withExposedPorts(6379)
            .waitingFor(Wait.forListeningPort());

    @Autowired
    private UserServiceCache userServiceCache;

    @BeforeAll
    public static void startRedisContainer() {
        redisContainer.start();
    }

    @AfterAll
    public static void stopRedisContainer() {
        redisContainer.stop();
    }

    @Test
    public void testGetUserById_CachesUser() {
        Long userId = 1L;
        User user = userServiceCache.getUserById(userId);

        assertNotNull(user);
        assertEquals("John Doe", user.getName());

        // Fetch the same user again to test caching
        User cachedUser = userServiceCache.getUserById(userId);
        assertSame(user, cachedUser, "The cached user should be the same instance.");
    }

    @Test
    public void testUpdateUser() {
        User user = new User("1L", "John Doe Updated");
        User updatedUser = userServiceCache.updateUser(user);

        assertNotNull(updatedUser);
        assertEquals("John Doe Updated", updatedUser.getName());

        // Verify that the updated user is cached
        User cachedUser = userServiceCache.getUserById(1L);
        assertEquals("John Doe Updated", cachedUser.getName());
    }

    @Test
    public void testDeleteUser() {
        Long userId = 1L;

        // Delete user
        userServiceCache.deleteUser(userId);

        // Try to fetch deleted user
        User deletedUser = userServiceCache.getUserById(userId);
        assertNull(deletedUser, "Deleted user should not be found.");
    }

    @Test
    public void testGetNonExistentUser() {
        User user = userServiceCache.getUserById(99L); // Non-existent user
        assertNull(user, "User should be null for non-existent ID.");
    }

    @Configuration
    static class RedisConfig {
        @Bean
        public CacheManager cacheManager(RedisConnectionFactory connectionFactory) {
            RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                    .entryTtl(Duration.ofMinutes(10)); // Set your cache TTL here

            return RedisCacheManager.builder(connectionFactory)
                    .cacheDefaults(config)
                    .build();
        }

        @Bean
        public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
            RedisTemplate<String, Object> template = new RedisTemplate<>();
            template.setConnectionFactory(connectionFactory);
            return template;
        }
    }
}

