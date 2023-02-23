package ru.dreamjob.repository;

import ru.dreamjob.model.User;

import java.util.Collection;
import java.util.Optional;

public interface UserRepository {

    Optional<User> save(User user);

    Optional<User> findByEmailAndPassword(String email, String password);

    boolean deleteByEmail(String email);

    Collection<User> findAll();
}
