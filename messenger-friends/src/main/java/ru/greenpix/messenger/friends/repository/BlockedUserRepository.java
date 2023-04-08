package ru.greenpix.messenger.friends.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.greenpix.messenger.friends.entity.BlockedUser;
import ru.greenpix.messenger.friends.entity.Relationship;

public interface BlockedUserRepository extends JpaRepository<BlockedUser, Relationship> {
}