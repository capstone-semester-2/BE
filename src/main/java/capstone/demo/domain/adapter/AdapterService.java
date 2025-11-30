package capstone.demo.domain.adapter;

import capstone.demo.domain.user.entity.User;
import capstone.demo.domain.voice.entity.VoiceModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdapterService {

    private final AdapterRepository adapterRepository;

    public void saveUsersAdapterId(User user, Long adapterNumber, VoiceModel voiceModel) {
        adapterRepository.save(Adapter.builder()
                        .user(user)
                        .adapterNumber(adapterNumber)
                        .voiceModel(voiceModel)
                        .build());
    }

    public Adapter findByUser(User user) {
        return adapterRepository.findByUserId(user.getId()).orElse(null);
    }
}
