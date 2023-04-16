package ru.greenpix.messenger.user.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.greenpix.messenger.user.entity.User;
import ru.greenpix.messenger.user.model.UserAttribute;

import java.util.Map;

public interface CustomizedUserRepository {

    Page<User> findAllWithFilters(Pageable pageable, Map<UserAttribute, Object> filters);

}
