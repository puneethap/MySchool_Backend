package com.schoolmanagement.schoolmanagement.service;

import com.schoolmanagement.schoolmanagement.entity.Erole;
import com.schoolmanagement.schoolmanagement.entity.Role;
import com.schoolmanagement.schoolmanagement.entity.User;
import com.schoolmanagement.schoolmanagement.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class UserServiceImplTest {

    private Long id;
    private String username;

    private String email;

    private String password;

    private User user;

    @Autowired
    UserService userService;

    @MockBean
    UserRepository userRepository;

    @BeforeEach
    void setup() {
        id = 1L;
        username = "username";
        email = "abc@xyz.com";
        password = "password";

        Role role = new Role(1, Erole.ROLE_ADMIN);
        Set<Role> roles = new HashSet<>();
        roles.add(role);

        user = new User(id, username, email, password, roles);
    }

    @Test
    void findByUsername() {
        when(userRepository.findByUsername(username)).thenReturn(user);

        User fetchedUser = userService.findByUsername(username);

        assertEquals(user, fetchedUser);
    }

    @Test
    void findByEmail() {
        when(userRepository.findByEmail(email)).thenReturn(user);

        User fetchedUser = userService.findByEmail(email);

        assertEquals(user, fetchedUser);
    }

    @Test
    void findById() {
        when(userRepository.findById(id)).thenReturn(Optional.ofNullable(user));

        User fetchedUser = userService.findById(id);

        assertEquals(user, fetchedUser);
    }

    @Test
    void save() {
        when(userRepository.save(user)).thenReturn(user);

        User savedUser = userService.save(user);

        assertEquals(user, savedUser);
    }
}