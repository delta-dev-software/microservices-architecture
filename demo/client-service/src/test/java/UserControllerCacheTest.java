


import clientservice.application.service.UserServiceCache;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import clientservice.domain.model.User;

@ExtendWith(SpringExtension.class) // JUnit 5 extension
//@SpringBootTest // Loads the Spring context for integration tests
@AutoConfigureMockMvc // Auto-configures MockMvc
public class UserControllerCacheTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserServiceCache userService; // Mock the service

    @Autowired
    private ObjectMapper objectMapper; // JSON parser for request/response

    private User sampleUser;

    @BeforeEach
    public void setup() {
        // Create a sample user object
        sampleUser = new User( "John Doe","");
    }

    @Test
    public void testGetUserById() throws Exception {
        // Mock the service to return the sample user when called
        Mockito.when(userService.getUserById(1L)).thenReturn(sampleUser);

        mockMvc.perform(get("/cache/users/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("John Doe"));

        // Verify that the service method was called once with the correct ID
        Mockito.verify(userService, Mockito.times(1)).getUserById(1L);
    }

    @Test
    public void testCreateUser() throws Exception {
        // Mock the service to return the sample user when the user is created
        Mockito.when(userService.updateUser(Mockito.any(User.class))).thenReturn(sampleUser);

        // Convert the user object to JSON for the request
        String userJson = objectMapper.writeValueAsString(sampleUser);

        mockMvc.perform(post("/cache/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("John Doe"));

        // Verify that the service method was called once with the correct user object
        Mockito.verify(userService, Mockito.times(1)).updateUser(Mockito.any(User.class));
    }

    @Test
    public void testDeleteUser() throws Exception {
        // Mock the service to do nothing when delete is called
        Mockito.doNothing().when(userService).deleteUser(1L);

        mockMvc.perform(delete("/cache/users/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // Verify that the service method was called once with the correct ID
        Mockito.verify(userService, Mockito.times(1)).deleteUser(1L);
    }
}

