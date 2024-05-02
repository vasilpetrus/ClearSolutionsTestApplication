package com.clearsolutions.service;

import com.clearsolutions.model.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface UserService {

    User createUser(User user);

    User updateUser(Long userId, User user);

    Optional<User> findUserById(Long id);

    void deleteUser(Long userId);

    List<User> getUsersByBirthDateRange(LocalDate fromDate, LocalDate toDate);
}
