package capstone.demo.global.redis;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import capstone.demo.global.apiPayload.code.status.ErrorStatus;
import capstone.demo.global.apiPayload.exception.GeneralException;

import java.time.Duration;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedisService {
    private final RedisTemplate<String, Object> redisTemplate;

    public void setValue(String key, String value, long ttlMillis) {
        try {
            ValueOperations<String, Object> values = redisTemplate.opsForValue();
            values.set(key, value, Duration.ofMillis(ttlMillis));
        } catch (Exception e) {
            log.error("Redis set 오류 — key: {}, ttl: {}, 예외: {}", key, ttlMillis, e.toString(), e);
            throw new GeneralException(ErrorStatus.REDIS_ERROR);
        }
    }

    public String getValue(String key) {
        try {
            ValueOperations<String, Object> values = redisTemplate.opsForValue();
            if (values.get(key) == null) {
                return "";
            }
            return values.get(key).toString();
        } catch (Exception e) {
            throw new GeneralException(ErrorStatus.REDIS_ERROR);
        }
    }

    public void deleteValue(String key) {
        try {
            redisTemplate.delete(key);
        } catch (Exception e) {
            throw new GeneralException(ErrorStatus.REDIS_ERROR);
        }
    }

    public boolean checkExistsValue(String key) {
        try {
            return redisTemplate.hasKey(key);
        } catch (Exception e) {
            throw new GeneralException(ErrorStatus.REDIS_ERROR);
        }
    }
}
