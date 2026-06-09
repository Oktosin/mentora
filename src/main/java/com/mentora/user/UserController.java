package com.mentora.user;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.mentora.common.RequestPayload;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = "user")
@RequiredArgsConstructor
public class UserController {

	private final UserManagementService userManagementService;

	@RequestMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE, method = {RequestMethod.POST, RequestMethod.GET})
	public ResponseEntity<?> register(@RequestBody RegistrationRequest request, HttpServletRequest httpRequest) {
		return userManagementService.register(request, httpRequest);
	}

	@RequestMapping(value = "/find-users", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE, method = {RequestMethod.POST, RequestMethod.GET})
	public ResponseEntity<?> getAllUsers(HttpServletRequest httpServletRequest, @RequestBody RequestPayload requestPayload) {
		return userManagementService.getAllUsers(httpServletRequest, requestPayload);
	}
}
