package capstone.demo.domain.user.service;

import capstone.demo.domain.user.entity.User;
import capstone.demo.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

}
