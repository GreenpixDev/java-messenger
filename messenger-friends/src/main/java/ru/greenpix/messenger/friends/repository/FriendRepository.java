package ru.greenpix.messenger.friends.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.greenpix.messenger.friends.entity.Friend;
import ru.greenpix.messenger.friends.entity.Relationship;

public interface FriendRepository extends JpaRepository<Friend, Relationship> {
}