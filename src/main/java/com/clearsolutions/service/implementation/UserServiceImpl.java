package com.clearsolutions.service.implementation;

import com.clearsolutions.model.User;
import com.clearsolutions.repository.UserRepository;
import com.clearsolutions.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of the UserService interface providing CRUD operations for User entities.
 */
@Service
public class UserServiceImpl implements UserService {

    private static final int MINIMUM_AGE = 18;

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Creates a new user if the provided birth date meets the minimum age requirement.
     * @param user The user entity to be created
     * @throws IllegalArgumentException If the user's age is below the minimum required age
     */
    @Override
    public User createUser(User user) {
        validateUserAge(user.getBirthDate());
        return userRepository.save(user);
    }

    /**
     * Updates an existing user or creates a new user if the specified user ID doesn't exist.
     * @param userId The ID of the user to be updated
     * @param user The updated user entity
     * @return The updated or newly created user entity
     */
    @Override
    public User updateUser(Long userId, User user) {
        return findUserById(userId)
                .map(userInDb -> userInDb.toBuilder()
                        .email(user.getEmail())
                        .address(user.getAddress())
                        .phoneNumber(user.getPhoneNumber())
                        .firstName(user.getFirstName())
                        .lastName(user.getLastName())
                        .birthDate(user.getBirthDate())
                        .build())
                .map(userRepository::save)
                .orElseGet(() -> {
                    user.setId(userId);
                    return userRepository.save(user);
                });
    }

    /**
     * Retrieves a user by their ID.
     * @param id The ID of the user to retrieve
     * @return An optional containing the user entity if found, otherwise empty
     */
    @Override
    public Optional<User> findUserById(Long id) {
        return userRepository.findById(id);
    }

    /**
     * Deletes a user by their ID.
     * @param userId The ID of the user to delete
     */
    @Override
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }

    /**
     * Retrieves a list of users within the specified birth date range.
     * @param fromDate The start date of the birth date range
     * @param toDate The end date of the birth date range
     * @return A list of users within the specified birth date range
     * @throws IllegalArgumentException If the 'from' date is after the 'to' date
     */
    @Override
    public List<User> getUsersByBirthDateRange(LocalDate fromDate, LocalDate toDate) {
        validateDateRange(fromDate, toDate);
        return userRepository.findByBirthDateBetween(fromDate, toDate);
    }

    /**
     * Validates that a user's age meets the minimum required age.
     * @param birthDate The birth date of the user to validate
     * @throws IllegalArgumentException If the user's age is below the minimum required age
     */
    private void validateUserAge(LocalDate birthDate) {
        LocalDate minDate = LocalDate.now().minusYears(MINIMUM_AGE);
        if (birthDate.isAfter(minDate)) {
            throw new IllegalArgumentException("User must be at least 18 years old.");
        }
    }

    /**
     * Validates that the 'from' date is before the 'to' date.
     * @param fromDate The 'from' date
     * @param toDate The 'to' date
     * @throws IllegalArgumentException If the 'from' date is after the 'to' date
     */
    private void validateDateRange(LocalDate fromDate, LocalDate toDate) {
        if (fromDate.isAfter(toDate)) {
            throw new IllegalArgumentException("'From' date must be before 'To' date.");
        }
    }
}
