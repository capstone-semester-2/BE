package capstone.demo.domain.emitter;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class EmitterRepository {
    private final Map<Long, Map<String, SseEmitter>> emitters = new ConcurrentHashMap<>();

    public SseEmitter save(Long userId, String emitterId, SseEmitter emitter) {
        emitters.computeIfAbsent(userId, key -> new ConcurrentHashMap<>())
                .put(emitterId, emitter);
        return emitter;
    }

    public Map<String, SseEmitter> getEmitters(Long userId) {
        return emitters.getOrDefault(userId, Collections.emptyMap());
    }

    public SseEmitter getEmitter(Long userId, String emitterId) {
        return emitters.getOrDefault(userId, Collections.emptyMap()).get(emitterId);
    }

    public void delete(Long userId, String emitterId) {
        Map<String, SseEmitter> userEmitters = emitters.get(userId);
        if (userEmitters != null) {
            userEmitters.remove(emitterId);
            if (userEmitters.isEmpty()) {
                emitters.remove(userId);
            }
        }
    }

    public void deleteAll(Long userId) {
        emitters.remove(userId);
    }
}
