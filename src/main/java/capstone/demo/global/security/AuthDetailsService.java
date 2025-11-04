package capstone.demo.global.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import capstone.demo.domain.user.entity.User;
import capstone.demo.domain.user.repository.UserRepository;
import capstone.demo.global.apiPayload.code.status.ErrorStatus;
import capstone.demo.global.apiPayload.exception.GeneralException;
import capstone.demo.global.apiPayload.exception.handler.NotFoundHandler;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        try {
            Long id = Long.valueOf(userId);
            User user = userRepository.findById(id)
                    .orElseThrow(() -> new NotFoundHandler(ErrorStatus.NOT_FOUND_USER));
            return new AuthDetails(user);
        } catch (NumberFormatException e){
            throw new GeneralException(ErrorStatus.TOKEN_INVALID);
        }
    }
}
