package com.example.meal2.auth;

import com.example.meal2.auth.dto.AuthenticationResponseDTO;
import com.example.meal2.config.JwtService;
import com.example.meal2.exception.GenericBadRequestException;
import com.example.meal2.exception.ResourceLimitException;
import com.example.meal2.exception.ResourceNotFoundException;
import com.example.meal2.user.Role;
import com.example.meal2.user.User;
import com.example.meal2.user.UserRepository;
import com.example.meal2.user.dto.UserCreationDTO;
import com.example.meal2.user.dto.UserSigninDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    @Value("${limits.users}")
    private Integer maxUsers;

    public void register(@Valid UserCreationDTO userCreationDTO) {
        userRepository.findByUsername(userCreationDTO.username()).ifPresentOrElse(
                user -> {
                    throw new IllegalArgumentException(
                            String.format("username: %s already exists", user.getUsername()));
                },
                () -> {
                    if(userRepository.count() >= maxUsers){
                        throw new ResourceLimitException(String.format("max %d users reached", maxUsers));
                    }
                    if(!userCreationDTO.password().equals(userCreationDTO.passwordConfirmation())){
                        throw new GenericBadRequestException("password and passwordConfirmation must match");
                    }
                    User user = new User(
                            userCreationDTO.username(),
                            passwordEncoder.encode(userCreationDTO.password()),
                            Role.USER
                    );
                    userRepository.save(user);
                }
        );
    }

    public AuthenticationResponseDTO authenticate(@Valid UserSigninDTO userSigninDTO) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        userSigninDTO.username(),
                        userSigninDTO.password()
                )
        );
        var user = userRepository.findByUsername(userSigninDTO.username())
                .orElseThrow(() -> new ResourceNotFoundException(String.format("username: %s not found", userSigninDTO.username())));
        return generateJwt(user);
    }

    private AuthenticationResponseDTO generateJwt(User user){
        Map<String, Object> claims = new HashMap<>();
        claims.put("uid", user.getId());
        claims.put("rle", user.getRole());
        var jwtToken = jwtService.generateToken(claims, user);
        return new AuthenticationResponseDTO(jwtToken);
    }
}
