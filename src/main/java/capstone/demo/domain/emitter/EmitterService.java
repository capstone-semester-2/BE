package capstone.demo.domain.emitter;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmitterService {

    private final ObjectMapper objectMapper;

    private final EmitterRepository emitterRepository;
    public SseEmitter connect(Long userId) {
        SseEmitter emitter = new SseEmitter(10 * 60 * 1000L);

        String emitterId = UUID.randomUUID().toString();

        emitter.onCompletion(() -> emitterRepository.delete(userId, emitterId));
        emitter.onTimeout(() -> emitterRepository.delete(userId, emitterId));
        emitter.onError(e -> emitterRepository.delete(userId, emitterId));

        emitterRepository.save(userId, emitterId, emitter);

        try {
            emitter.send(SseEmitter.event()
                    .name("connected")
                    .data(Map.of("emitterId", emitterId)));
        } catch (IOException e) {
            emitterRepository.delete(userId, emitterId);
        }

        return emitter;
    }

    public void sendToEmitter(Long userId, String emitterId, String eventName, Object data) {
        SseEmitter emitter = emitterRepository.getEmitter(userId, emitterId);

        if (emitter == null) return;

        try {
            Map<String, Object> payload = Map.of(
                    "event", eventName,
                    "data", data
            );
            log.info("SSE payload = {}", objectMapper.writeValueAsString(payload));

            emitter.send(SseEmitter.event()
                    .name(eventName)
                    .data(payload));
        } catch (IOException e) {
            emitterRepository.delete(userId, emitterId);
        }
    }

    // 브로드 캐스트 용
//    public void sendEvent(Long userId, String eventName, Object data) {
//        List<SseEmitter> emitters = emitterRepository.get(userId);
//
//        for (SseEmitter emitter : emitters) {
//            try {
//                emitter.send(SseEmitter.event().name(eventName).data(data));
//            } catch (IOException e) {
//                emitterRepository.delete(userId, emitter); // 끊긴 연결 정리
//            }
//        }
//    }
}
