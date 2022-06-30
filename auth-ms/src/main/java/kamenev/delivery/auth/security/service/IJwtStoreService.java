package kamenev.delivery.auth.security.service;

import kamenev.delivery.auth.security.TokenPair;

import java.util.UUID;

public interface IJwtStoreService {

    void add(UUID uuid, TokenPair tokenPair);

    TokenPair get(UUID uuid);
}
