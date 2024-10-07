

import clientservice.application.service.UserServiceCache;
import clientservice.domain.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@EnableCaching
public class UserServiceCacheTest {

    @InjectMocks
    private UserServiceCache userServiceCache;

    @Mock
    private CacheManager cacheManager;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        cacheManager = new ConcurrentMapCacheManager("userCache");
        userServiceCache = new UserServiceCache();
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
}

