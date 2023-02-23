package ru.dreamjob.service;

import ru.dreamjob.model.User;

import java.util.Optional;

public interface UserService {
    Optional<User> save(User user);

    Optional<User> findByEmailAndPassword(String email, String password);
}
