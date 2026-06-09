package com.example.meal2.user;

import com.example.meal2.user.dto.AdminResetUserPasswordDTO;
import com.example.meal2.user.dto.AdminResetUserPasswordResponseDTO;
import com.example.meal2.user.dto.UserResetPasswordDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PutMapping(value="/user/reset-password", consumes={"application/json"})
    public ResponseEntity<?> userResetPassword(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody UserResetPasswordDTO userResetPasswordDTO
    ){
        userService.userResetPassword(user, userResetPasswordDTO);
        return new ResponseEntity<>(
                null,
                HttpStatus.OK
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(value="/user/admin/reset-user-password", produces={"application/json"}, consumes={"application/json"})
    public ResponseEntity<AdminResetUserPasswordResponseDTO> adminResetUserPassword(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody AdminResetUserPasswordDTO adminResetUserPasswordDTO
    ){
        return new ResponseEntity<>(
                userService.adminResetUserPassword(user, adminResetUserPasswordDTO),
                HttpStatus.OK
        );
    }
}
