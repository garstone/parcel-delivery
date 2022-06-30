package kamenev.delivery.auth.security.service;

import kamenev.delivery.auth.exceptions.exception.UnauthorizedException;
import kamenev.delivery.auth.security.TokenPair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.UUID;

@Service
public class JwtStoreService implements IJwtStoreService {

    private final RedisTemplate<String, TokenPair> template;

    @Autowired
    public JwtStoreService(RedisTemplate<String, TokenPair> template) {
        this.template = template;
    }

    @Value("${spring.redis.ttl-hours}")
    private int TTL_HOURS;

    @Override
    public void add(final UUID uuid, final TokenPair tokenPair) {
        template.opsForValue().set(uuid.toString(), tokenPair);
        template.expire(uuid.toString(), Duration.ofHours(TTL_HOURS));
    }

    @Override
    public TokenPair get(final UUID uuid) {
        var tokenPair = template.opsForValue().get(uuid);
        if (tokenPair == null) {
            throw new UnauthorizedException();
        }
        return tokenPair;
    }

}
