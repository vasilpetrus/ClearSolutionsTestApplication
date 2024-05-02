package com.clearsolutions.controller;

import com.clearsolutions.exeption.UserNotFoundException;
import com.clearsolutions.model.User;
import com.clearsolutions.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

/**
 * Controller class for managing user-related HTTP requests.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final Environment env;

    /**
     * Create a new user.
     *
     * @param user The user object to be created.
     * @return ResponseEntity representing the HTTP response.
     */
    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody User user) {
        try {
            // Get the minimum age required from the environment properties file
            int minAge = env.getProperty("registration.min-age", Integer.class, 18);

            // Calculate the age of the user
            LocalDate currentDate = LocalDate.now();
            if (Period.between(user.getBirthDate(), currentDate).getYears() < minAge) {
                return ResponseEntity.badRequest().body("User must be at least " + minAge + " years old.");
            }

            // Create the user if age requirement is met
            userService.createUser(user);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (IllegalArgumentException e) {
            // Return bad request if there is an illegal argument
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Update an existing user.
     *
     * @param userId The ID of the user to be updated.
     * @param user   The updated user object.
     * @return ResponseEntity representing the HTTP response.
     */
    @PutMapping("/{userId}")
    public ResponseEntity<?> updateUser(@PathVariable Long userId, @RequestBody User user) {
        try {
            userService.updateUser(userId, user);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            // Return bad request if there is an illegal argument
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (UserNotFoundException e) {
            // Return not found if the user to update is not found
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Delete an existing user.
     *
     * @param userId The ID of the user to be deleted.
     * @return ResponseEntity representing the HTTP response.
     */
    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable Long userId) {
        try {
            userService.deleteUser(userId);
            return ResponseEntity.ok().build();
        } catch (UserNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Search users by birth date range.
     *
     * @param fromDate The start date of the birth date range.
     * @param toDate   The end date of the birth date range.
     * @return ResponseEntity representing the HTTP response.
     */
    @GetMapping("/search")
    public ResponseEntity<String> searchUsersByBirthDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {
        try {
            List<User> users = userService.getUsersByBirthDateRange(fromDate, toDate);
            return ResponseEntity.ok(users.toString());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}