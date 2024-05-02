package com.clearsolutions.fillDB;

import com.clearsolutions.model.User;
import com.clearsolutions.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.util.List;

/**
 * This class is a configuration component responsible for initializing the database with sample data
 * using Spring Boot's CommandLineRunner interface. It creates and saves sample items and users to
 * the corresponding repositories upon application startup.
 */
@Configuration
public class DatabaseInitializer {

    @Bean
    public CommandLineRunner initializeDatabase(UserRepository userRepository) {
        return args -> {
            // Default users
            User user1 = User.builder()
                    .email("john@example.com")
                    .firstName("John")
                    .lastName("Doe")
                    .birthDate(LocalDate.of(1990, 5, 15))
                    .address("123 Main St")
                    .phoneNumber("1234567890")
                    .build();

            User user2 = User.builder()
                    .email("jane@example.com")
                    .firstName("Jane")
                    .lastName("Doe")
                    .birthDate(LocalDate.of(1992, 8, 21))
                    .address("456 Oak St")
                    .phoneNumber("9876543210")
                    .build();

            // Save users to the database
            userRepository.saveAll(List.of(user1, user2));
        };
    }
}