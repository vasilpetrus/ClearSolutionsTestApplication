package com.clearsolutions.controller;

import com.clearsolutions.model.User;
import com.clearsolutions.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;

import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    void createUser_ValidUser_Success() throws Exception {
        // Arrange
        String jsonUser = "{\"firstName\":\"John\",\"lastName\":\"Doe\",\"birthDate\":\"1990-01-01\",\"email\":\"john@example.com\",\"address\":\"123 Street\",\"phoneNumber\":\"123456789\"}";

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonUser))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        verify(userService, times(1)).createUser(any());
    }

    @Test
    void updateUser_ValidUserIdAndUser_Success() throws Exception {
        // Arrange
        long userId = 1L;
        User user = new User(1L,"john@example.com","John", "Doe", LocalDate.of(1990, 1, 1), "123 Street", "123456789");
        when(userService.updateUser(userId, user)).thenReturn(user);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.put("/users/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\":\"John\",\"lastName\":\"Doe\",\"birthDate\":\"1990-01-01\",\"email\":\"john@example.com\",\"address\":\"123 Street\",\"phoneNumber\":\"123456789\"}"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void deleteUser_ValidUserId_Success() throws Exception {
        // Arrange
        long userId = 1L;

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.delete("/users/{userId}", userId))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void searchUsersByBirthDateRange_ValidDateRange_Success() throws Exception {
        // Arrange
        LocalDate fromDate = LocalDate.of(1990, 1, 1);
        LocalDate toDate = LocalDate.of(2000, 1, 1);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/users/search")
                        .param("fromDate", fromDate.toString())
                        .param("toDate", toDate.toString()))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
