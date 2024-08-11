package com.didacto.infra.redis;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;


@Repository
@AllArgsConstructor
public class AuthRedisRepository {
    private final RedisTemplate<String, String> redisTemplate;

    @Value("${jwt.refresh_token_expired_at}")
    private static long REFRESH_TOKEN_EXPIRE_TIME;

    public void setRefreshAuthenticationInfo (Long memberId, String token) {
        String key = String.format("auth/refresh/%ld", memberId);
        redisTemplate.opsForValue().set(key, token, REFRESH_TOKEN_EXPIRE_TIME, TimeUnit.MINUTES);
    }

    public boolean validateRefreshToken (Long memberId, String token) {
        String key = String.format("auth/refresh/%ld", memberId);
        String savedToken = redisTemplate.opsForValue().get(key);
        if(savedToken == null || !savedToken.equals(token)){
            return false;
        }
        else{
            return true;
        }
    }

}
