package com.mentora.security;

import java.util.Map;

import org.springframework.security.core.Authentication;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {

	Map<String, Object> userLogout(UserLogoutRequest userLogoutRequest, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication);

	Map<String, Object> userLogin(UserLoginRequest userLoginRequest, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication);

	Map<String, Object> refreshToken(RefreshTokenRequest refreshTokenRequest, HttpServletRequest httpServletRequest);

}
