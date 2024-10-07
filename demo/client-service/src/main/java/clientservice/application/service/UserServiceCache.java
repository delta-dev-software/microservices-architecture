package clientservice.application.service;

import clientservice.domain.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserServiceCache {

    // Simulated database using a Map
    private final Map<Long, User> userDatabase = new HashMap<>();

    // Initialize some sample users for demonstration
    public UserServiceCache() {
        userDatabase.put(1L, new User("1L", "John Doe"));
        userDatabase.put(2L, new User("2L", "Jane Smith"));
        userDatabase.put(3L, new User("3L", "Alice Johnson"));
    }

    /**
     * Retrieves a user by ID. The result is cached.
     *
     * @param userId The ID of the user to retrieve.
     * @return The user object.
     */
    @Cacheable(value = "userCache", key = "#userId")
    public User getUserById(Long userId) {
        // Simulate a slow method (e.g., database call)
        simulateSlowService();
        return userDatabase.get(userId);
    }

    /**
     * Updates or creates a user. The result is also cached.
     *
     * @param user The user object to update or create.
     * @return The updated user object.
     */
    @CachePut(value = "userCache", key = "#user.id")
    public User updateUser(User user) {
        // Update the user in the simulated database
        userDatabase.put(user.getId(), user);
        return user;
    }

    /**
     * Deletes a user by ID and evicts the cached entry.
     *
     * @param userId The ID of the user to delete.
     */
    @CacheEvict(value = "userCache", key = "#userId")
    public void deleteUser(Long userId) {
        // Remove the user from the simulated database
        userDatabase.remove(userId);
    }

    private void simulateSlowService() {
        try {
            Thread.sleep(2000); // Simulate delay of 2 seconds
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

