package org.example.jUnit.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class UserServiceTest {

    @Test
    void test() {
        var userService = new UserService();
        var users = userService.getAll();
        assertTrue(users.isEmpty(), () -> "Users empty should be empty");
    }
}
