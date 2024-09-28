package com.didacto.service.monitoring;

import com.didacto.common.ErrorDefineCode;
import com.didacto.config.exception.custom.BasicCustomException500;
import com.didacto.dto.monitoring.MonitoringImage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class MonitoringImageService {
    private final RedisTemplate<String, String> redisTemplate;

    private static final long MONITORING_IMAGE_EXPIRE_TIME = 60 * 1000L;

    public void upload(long lectureId, long memberId, String image) {
        try{
            String key = String.format("monitoring/%d/%d", lectureId, memberId);
            redisTemplate.opsForValue().set(key, image, MONITORING_IMAGE_EXPIRE_TIME, TimeUnit.MILLISECONDS);
        }
        catch(Exception e){
            throw new BasicCustomException500(ErrorDefineCode.REDIS_COMMAND_FAIL);
        }
    }

    public List<MonitoringImage> getImages(Long lectureId) {
        try{
            Set<String> keys = redisTemplate.keys("monitoring/" + lectureId + "/*");
            List<MonitoringImage> monitoringImages = new ArrayList<>();

            if (keys != null && !keys.isEmpty()) {
                for (String key : keys) {
                    // 키에서 memberId 추출 (monitoring/lectureId/memberId 형식)
                    String[] keyParts = key.split("/");
                    if (keyParts.length >= 3) {
                        Long memberId = Long.parseLong(keyParts[2]);  // 세 번째 요소가 memberId

                        // Redis에서 해당 key에 대한 image 가져오기
                        String image = redisTemplate.opsForValue().get(key);

                        monitoringImages.add(new MonitoringImage(lectureId, memberId, image));
                    } else {
                        throw new BasicCustomException500(ErrorDefineCode.REDIS_COMMAND_FAIL);
                    }
                }
            }

            return monitoringImages;
        }
        catch(Exception e){
            throw new BasicCustomException500(ErrorDefineCode.REDIS_COMMAND_FAIL);
        }
    }
}
