package com.example.meal2.user;

import com.example.meal2.exception.GenericBadRequestException;
import com.example.meal2.exception.GenericForbiddenException;
import com.example.meal2.exception.ResourceNotFoundException;
import com.example.meal2.user.dto.AdminResetUserPasswordDTO;
import com.example.meal2.user.dto.AdminResetUserPasswordResponseDTO;
import com.example.meal2.user.dto.UserResetPasswordDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }


    @Override
    public AdminResetUserPasswordResponseDTO adminResetUserPassword(User user, AdminResetUserPasswordDTO adminResetUserPasswordDTO) {
        if (user.getRole() != Role.ROLE_ADMIN){
            throw new GenericForbiddenException("user is not an admin");
        }
        User u = userRepository.findByUsername(adminResetUserPasswordDTO.username())
                .orElseThrow(() -> new ResourceNotFoundException(String.format("username: %s not found", adminResetUserPasswordDTO.username())));
        UUID uuid = UUID.randomUUID();
        String uuidString = uuid.toString();
        u.setPassword(passwordEncoder.encode(uuidString));
        userRepository.save(u);
        return new AdminResetUserPasswordResponseDTO(uuidString);
    }

    @Override
    public void userResetPassword(User user, UserResetPasswordDTO userResetPasswordDTO) {
        /*
        if (!user.getPassword().equals(passwordEncoder.encode(userResetPasswordDTO.oldPassword()))){
            throw new GenericBadRequestException("incorrect password");
        }
        */
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getUsername(),
                        userResetPasswordDTO.oldPassword()
                )
        );  // throws error if wrong password
        if(!userResetPasswordDTO.password().equals(userResetPasswordDTO.passwordConfirmation())){
            throw new GenericBadRequestException("password and passwordConfirmation must match");
        }
        user.setPassword(passwordEncoder.encode(userResetPasswordDTO.password()));
        userRepository.save(user);
    }
}
