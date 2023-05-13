package ru.greenpix.messenger.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.greenpix.messenger.chat.entity.GroupChat;

import java.util.UUID;

public interface GroupChatRepository extends JpaRepository<GroupChat, UUID>, JpaSpecificationExecutor<GroupChat> {
}