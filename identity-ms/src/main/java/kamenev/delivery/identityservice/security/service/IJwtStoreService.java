package kamenev.delivery.identityservice.security.service;

import kamenev.delivery.identityservice.model.TokenPair;

import java.util.UUID;

public interface IJwtStoreService {
    void add(UUID uuid, TokenPair tokenPair);

    void delete(UUID uuid);

    TokenPair pop(UUID uuid);
}
