package com.planetsystems.tela.api.ClockInOutConsumer.bean;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

@Configuration
public class ConsumerCacheConfig {

    @Value("${spring.data.redis.host}")
    String host;

    @Value("${spring.data.redis.port}")
    int port;

    @Bean
    public JedisConnectionFactory jedisConnectionFactory(){
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(host, port);
        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory(config);
        return jedisConnectionFactory;
    }


    @Bean
    public RedisTemplate< String , Object> redisTemplate(JedisConnectionFactory redisConnectionFactory){
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        GenericJackson2JsonRedisSerializer jackson2JsonRedisSerializer = new GenericJackson2JsonRedisSerializer();
        RedisTemplate< String , Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setEnableTransactionSupport(true);

        redisTemplate.setKeySerializer(stringRedisSerializer);
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);

        redisTemplate.setHashKeySerializer(stringRedisSerializer);
        redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);

        return redisTemplate;
    }




    @Bean
    @Primary
    public RedisCacheManager cacheManager() {
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig() //
//                .prefixCacheNameWith(this.getClass().getPackageName() + ".") //
                .disableCachingNullValues();

        return RedisCacheManager.builder(jedisConnectionFactory()) //
                .cacheDefaults(config) //
                .build();

    }

    @Bean("halfHourCacheManager")
    public RedisCacheManager halfHourCacheManager() {
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig() //
//                .prefixCacheNameWith(this.getClass().getPackageName() + ".") //
                .entryTtl(Duration.ofMinutes(30)) //
                .disableCachingNullValues();

        return RedisCacheManager.builder(jedisConnectionFactory()) //
                .cacheDefaults(config) //
                .build();
    }

    @Bean("hourCacheManager")
    public RedisCacheManager hourCacheManager() {
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig() //
//                .prefixCacheNameWith(this.getClass().getPackageName() + ".") //
                .entryTtl(Duration.ofHours(1)) //
                .disableCachingNullValues();

        return RedisCacheManager.builder(jedisConnectionFactory()) //
                .cacheDefaults(config) //
                .build();
    }

    @Bean("_24HourCacheManager")
    public RedisCacheManager _24HourCacheManager() {
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig() //
//                .prefixCacheNameWith(this.getClass().getPackageName() + ".") //
                .entryTtl(Duration.ofHours(1*24)) //
                .disableCachingNullValues();

        return RedisCacheManager.builder(jedisConnectionFactory()) //
                .cacheDefaults(config) //
                .build();
    }

    @Bean("weekCacheManager")
    public RedisCacheManager weekCacheManager() {
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig() //
//                .prefixCacheNameWith(this.getClass().getPackageName() + ".") //
                .entryTtl(Duration.ofDays(7)) //
                .disableCachingNullValues();

        return RedisCacheManager.builder(jedisConnectionFactory()) //
                .cacheDefaults(config) //
                .build();
    }


    @Bean("monthCacheManager")
    public RedisCacheManager monthCacheManager() {
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig() //
//                .prefixCacheNameWith(this.getClass().getPackageName() + ".") //
                .entryTtl(Duration.ofDays(30)) //
                .disableCachingNullValues();

        return RedisCacheManager.builder(jedisConnectionFactory()) //
                .cacheDefaults(config) //
                .build();
    }
}
