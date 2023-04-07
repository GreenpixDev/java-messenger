package ru.greenpix.messenger.user.service;

import ru.greenpix.messenger.user.model.JwtUser;

public interface JwtService {

    String generateToken(JwtUser user);

    boolean validateToken(String token);

    JwtUser getUser(String token);



}
