package ru.greenpix.messenger.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.greenpix.messenger.auth.entity.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

}