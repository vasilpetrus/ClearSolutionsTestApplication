package com.clearsolutions.service.implementation;

import com.clearsolutions.model.User;
import com.clearsolutions.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void createUser_ValidUser_Success() {
        // Arrange
        User user = new User(1L,"john@example.com","John", "Doe", LocalDate.of(1990, 1, 1), "123 Street", "123456789");

        // Mock repository behavior
        when(userRepository.save(user)).thenReturn(user);

        // Act
        User createdUser = userService.createUser(user);

        // Assert
        assertNotNull(createdUser);
        assertEquals(user, createdUser);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void updateUser_UserExists_Success() {
        // Arrange
        Long userId = 1L;
        User existingUser = new User(1L,"john@example.com","John", "Doe", LocalDate.of(1990, 1, 1), "123 Street", "123456789");
        User updatedUser = new User(1L, "jane@example.com","Jane", "Doe", LocalDate.of(2000, 5, 15), "456 Elm St", "0974547155");

        // Mock repository behavior
        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(updatedUser)).thenReturn(updatedUser);

        // Act
        User result = userService.updateUser(userId, updatedUser);

        // Assert
        assertNotNull(result);
        assertEquals(updatedUser, result);
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).save(updatedUser);
    }

    @Test
    void updateUser_UserDoesNotExist_Success() {
        // Arrange
        Long userId = 1L;
        User newUser = new User(1L,"john@example.com","John", "Doe", LocalDate.of(1990, 1, 1), "123 Street", "123456789");

        // Mock repository behavior
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        when(userRepository.save(newUser)).thenReturn(newUser);

        // Act
        User result = userService.updateUser(userId, newUser);

        // Assert
        assertNotNull(result);
        assertEquals(newUser, result);
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).save(newUser);
    }

    @Test
    void deleteUser_UserExists_Success() {
        // Arrange
        Long userId = 1L;

        // Mock repository behavior
        doNothing().when(userRepository).deleteById(userId);

        // Act
        userService.deleteUser(userId);

        // Assert
        verify(userRepository, times(1)).deleteById(userId);
    }

    @Test
    void getUsersByBirthDateRange_ValidDateRange_Success() {
        // Arrange
        LocalDate fromDate = LocalDate.of(1990, 1, 1);
        LocalDate toDate = LocalDate.of(1995, 12, 31);
        List<User> expectedUsers = Arrays.asList(
                new User(1L,"john@example.com","John", "Doe", LocalDate.of(1990, 1, 1), "123 Street", "123456789"),
                new User(1L, "jane@example.com","Jane", "Doe", LocalDate.of(2000, 5, 15), "456 Elm St", "0974547155")
        );

        // Mock repository behavior
        when(userRepository.findByBirthDateBetween(fromDate, toDate)).thenReturn(expectedUsers);

        // Act
        List<User> actualUsers = userService.getUsersByBirthDateRange(fromDate, toDate);

        // Assert
        assertEquals(expectedUsers.size(), actualUsers.size());
        assertEquals(expectedUsers, actualUsers);
    }

    @Test
    void getUsersByBirthDateRange_InvalidDateRange_ExceptionThrown() {
        // Arrange
        LocalDate fromDate = LocalDate.of(1995, 1, 1);
        LocalDate toDate = LocalDate.of(1990, 12, 31);

        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> userService.getUsersByBirthDateRange(fromDate, toDate));
    }
}