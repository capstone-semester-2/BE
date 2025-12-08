package capstone.demo.domain.adapter;

import capstone.demo.domain.user.entity.User;
import capstone.demo.domain.voice.entity.VoiceModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        System.out.println("user.getId() = " + user.getId());
        return adapterRepository.findByUserId(user.getId()).orElse(null);
    }

    @Transactional
    public void deleteByUser(User user) {
        adapterRepository.deleteByUserId(user.getId());
    }
}
