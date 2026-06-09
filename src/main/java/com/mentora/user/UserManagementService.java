package com.mentora.user;

import org.springframework.http.ResponseEntity;

import com.mentora.common.RequestPayload;

import jakarta.servlet.http.HttpServletRequest;

public interface UserManagementService {
	ResponseEntity<?> register(RegistrationRequest registrationRequest, HttpServletRequest request);
	ResponseEntity<?> getAllUsers(HttpServletRequest httpServletRequest, RequestPayload requestPayload);
}
