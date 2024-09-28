package com.didacto.infra.redis;

import com.didacto.common.ErrorDefineCode;
import com.didacto.config.exception.custom.BasicCustomException500;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;


@Repository
@Slf4j
@RequiredArgsConstructor
public class AuthRedisRepository {

    private final RedisTemplate<String, String> redisTemplate;

    @Value("${jwt.refresh_token_expired_at}")
    private Long REFRESH_TOKEN_EXPIRE_TIME;

    public void setRefreshAuthenticationInfo (Long memberId, String token) {
        try{
            String key = String.format("auth/refresh/%d", memberId);
            redisTemplate.opsForValue().set(key, token, REFRESH_TOKEN_EXPIRE_TIME, TimeUnit.MILLISECONDS);
        }
        catch(Exception e){
            throw new BasicCustomException500(ErrorDefineCode.REDIS_COMMAND_FAIL);
        }

    }

    public boolean validateRefreshToken (Long memberId, String token) {
        try{
            String key = String.format("auth/refresh/%d", memberId);
            String savedToken = redisTemplate.opsForValue().get(key);
            if(savedToken == null || !savedToken.equals(token)){
                return false;
            }
            else{
                return true;
            }
        }
        catch(Exception e){
            throw new BasicCustomException500(ErrorDefineCode.REDIS_COMMAND_FAIL);
        }
    }

}
