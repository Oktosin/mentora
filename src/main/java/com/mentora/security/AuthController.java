package com.mentora.security;

import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = "auth")
@RequiredArgsConstructor
public class AuthController {

	private final AuthService authService;
	private final MfaService mfaService;

	@RequestMapping(value = "login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE, method = {RequestMethod.POST, RequestMethod.GET})
	public Map<String, Object> login(@RequestBody UserLoginRequest request, HttpServletRequest httpRequest, HttpServletResponse httpResponse, Authentication authentication) {
		return authService.userLogin(request, httpRequest, httpResponse, authentication);
	}

	@RequestMapping(value = "logout", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE, method = {RequestMethod.POST, RequestMethod.GET})
	public Map<String, Object> logout(@RequestBody(required = false) UserLogoutRequest request, HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, Authentication authentication) {
		return authService.userLogout(request, httpRequest, httpResponse, authentication);
	}

	@RequestMapping(value = "refresh-token", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE, method = {RequestMethod.POST, RequestMethod.GET})
	public Map<String, Object> refreshToken(@RequestBody RefreshTokenRequest request, HttpServletRequest httpRequest) {
		return authService.refreshToken(request, httpRequest);
	}

	@RequestMapping(value = "mfa/request-otp", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE, method = {RequestMethod.POST, RequestMethod.GET})
	public Map<String, Object> requestOtp(@RequestBody MfaRequest request, HttpServletRequest httpRequest) {
		return mfaService.requestOtp(request, httpRequest);
	}

	@RequestMapping(value = "mfa/verify", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE, method = {RequestMethod.POST, RequestMethod.GET})
	public Map<String, Object> verifyOtp(@RequestBody MfaVerifyRequest request, HttpServletRequest httpRequest) {
		return mfaService.verifyOtp(request, httpRequest);
	}
}
