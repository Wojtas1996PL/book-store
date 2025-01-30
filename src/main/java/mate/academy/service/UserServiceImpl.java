package mate.academy.service;

import lombok.RequiredArgsConstructor;
import mate.academy.dto.user.UserRegistrationRequestDto;
import mate.academy.dto.user.UserResponseDto;
import mate.academy.exception.RegistrationException;
import mate.academy.mapper.UserMapper;
import mate.academy.model.User;
import mate.academy.service.repository.user.UserRepository;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;
    private final UserRepository userRepository;

    @Override
    public UserResponseDto register(UserRegistrationRequestDto requestDto)
            throws RegistrationException {
        if (userRepository.findUserByEmail(requestDto.getEmail()).isPresent()) {
            throw new RegistrationException("User already exists, cannot register");
        }
        User user = new User();
        user.setEmail(requestDto.getEmail());
        user.setFirstName(requestDto.getFirstName());
        user.setLastName(requestDto.getLastName());
        user.setShippingAddress(requestDto.getShippingAddress());
        userRepository.save(user);
        return userMapper.toUserResponseDto(user);
    }
}
