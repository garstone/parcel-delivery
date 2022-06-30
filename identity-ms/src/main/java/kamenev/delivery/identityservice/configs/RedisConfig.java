package kamenev.delivery.identityservice.configs;

import kamenev.delivery.identityservice.model.TokenPair;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

@Configuration
public class RedisConfig {

    @Bean
    public JedisConnectionFactory redisConnectionFactory() {
        return new JedisConnectionFactory();
    }

    @Bean
    public RedisTemplate<String, TokenPair> redisTemplate(JedisConnectionFactory connFactory) {
        RedisTemplate<String, TokenPair> template = new RedisTemplate<>();
        template.setConnectionFactory(connFactory);
        template.setHashValueSerializer(new Jackson2JsonRedisSerializer<>(TokenPair.class));
        template.setValueSerializer(new Jackson2JsonRedisSerializer<>(TokenPair.class));
        return template;
    }

//    @Bean
//    public RedisCacheConfiguration cacheConfiguration() {
//        return RedisCacheConfiguration.defaultCacheConfig()
//                .entryTtl(Duration.ofDays(3))
//                .disableCachingNullValues()
//                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(
//                        new GenericJackson2JsonRedisSerializer()));
//    }
//
//    @Bean(name = "JwtCache")
//    public CacheManager JwtCacheManager(JedisConnectionFactory connectionFactory,
//                                        RedisCacheConfiguration cacheConfiguration) {
//        RedisCacheManager cacheManager = RedisCacheManager.builder(connectionFactory)
//                .cacheDefaults(cacheConfiguration)
//                .build();
//        return cacheManager;
//    }

}
