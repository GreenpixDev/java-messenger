package ru.greenpix.messenger.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.greenpix.messenger.user.entity.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID>, CustomizedUserRepository {

    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

}