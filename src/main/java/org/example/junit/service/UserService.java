package org.example.junit.service;

import org.example.junit.dto.User;

import java.util.*;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

public class UserService {

    private final List<User> users = new ArrayList<>();

    public List<User> getAll() {
        return users;
    }

    public void add(User... users) {
        this.users.addAll(Arrays.asList(users));
    }

    public Optional<User> login(String name, String password) {
            return users
                    .stream()
                    .filter(user -> user.getName().equals(name))
                    .filter(user -> user.getPassword().equals(password))
                    .findFirst();
    }

    public Map<Integer, User> getALLConvertedById() {
        return users.stream().collect(toMap(User::getId, identity()));
    }
}
