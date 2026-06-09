package com.example.meal2.user;

import com.example.meal2.user.dto.AdminResetUserPasswordDTO;
import com.example.meal2.user.dto.AdminResetUserPasswordResponseDTO;
import com.example.meal2.user.dto.UserResetPasswordDTO;

public interface UserService {
    AdminResetUserPasswordResponseDTO adminResetUserPassword(User user, AdminResetUserPasswordDTO adminResetUserPasswordDTO);
    void userResetPassword(User user, UserResetPasswordDTO userResetPasswordDTO);
}
