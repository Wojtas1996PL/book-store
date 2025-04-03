package mate.academy.service;

import java.util.HashSet;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import mate.academy.dto.user.UserRegistrationRequestDto;
import mate.academy.dto.user.UserResponseDto;
import mate.academy.exception.RegistrationException;
import mate.academy.exception.RoleNotFoundException;
import mate.academy.mapper.UserMapper;
import mate.academy.model.Role;
import mate.academy.model.RoleName;
import mate.academy.model.User;
import mate.academy.service.repository.role.RoleRepository;
import mate.academy.service.repository.user.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Override
    public UserResponseDto register(UserRegistrationRequestDto requestDto)
            throws RegistrationException {
        if (userRepository.findUserByEmail(requestDto.getEmail()).isPresent()) {
            log.warn("User with email {} already exists, registration aborted",
                    requestDto.getEmail());
            throw new RegistrationException("User already exists, cannot register");
        }
        User user = new User();
        user.setEmail(requestDto.getEmail());
        user.setFirstName(requestDto.getFirstName());
        user.setLastName(requestDto.getLastName());
        user.setShippingAddress(requestDto.getShippingAddress());
        user.setPassword(passwordEncoder.encode(requestDto.getPassword()));
        user.setRoles(createUserRolesSet());
        return userMapper.toUserResponseDto(userRepository.save(user));
    }

    private Set<Role> createUserRolesSet() {
        Role userRole = roleRepository.getRoleByName(RoleName.ROLE_USER).orElse(null);
        if (userRole == null) {
            log.warn("User role does not exist in database. Registration cannot proceed.");
            throw new RoleNotFoundException("Required role is missing. Contact support.");
        }
        Set<Role> roles = new HashSet<>();
        roles.add(userRole);
        return roles;
    }
}
