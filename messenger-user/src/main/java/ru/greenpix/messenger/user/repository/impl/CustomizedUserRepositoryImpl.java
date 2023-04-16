package ru.greenpix.messenger.user.repository.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import ru.greenpix.messenger.common.specification.BaseSpecification;
import ru.greenpix.messenger.user.entity.User;
import ru.greenpix.messenger.user.model.UserAttribute;
import ru.greenpix.messenger.user.repository.CustomizedUserRepository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class CustomizedUserRepositoryImpl extends SimpleJpaRepository<User, UUID> implements CustomizedUserRepository {

    public CustomizedUserRepositoryImpl(EntityManager entityManager) {
        super(User.class, entityManager);
    }

    @Override
    public Page<User> findAllWithFilters(Pageable pageable, Map<UserAttribute, Object> filters) {
       List<Specification<User>> spec = filters.entrySet().stream()
                .map(e -> BaseSpecification.equal(e.getKey().getAttribute(), e.getValue()))
                .collect(Collectors.toList());

        return findAll(BaseSpecification.all(spec), pageable);
    }
}
