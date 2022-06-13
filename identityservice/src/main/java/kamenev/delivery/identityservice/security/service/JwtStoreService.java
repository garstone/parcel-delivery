package kamenev.delivery.identityservice.security.service;

import kamenev.delivery.identityservice.model.TokenPair;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class JwtStoreService implements IJwtStoreService {

    private final RedisTemplate<String, TokenPair> template;

    @Value("${spring.redis.ttl-hours}")
    private final int TTL_HOURS;

    @Override
    public void add(final UUID uuid, final TokenPair tokenPair) {
        template.opsForValue().set(uuid.toString(), tokenPair);
        template.expire(uuid.toString(), Duration.ofHours(TTL_HOURS));
    }

    @Override
    public void delete(final UUID uuid) {
        template.opsForValue().getOperations().delete(uuid.toString());
    }

}
