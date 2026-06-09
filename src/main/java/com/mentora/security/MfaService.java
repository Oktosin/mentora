package com.mentora.security;

import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;

public interface MfaService {
	Map<String, Object> requestOtp(MfaRequest mfaRequest, HttpServletRequest httpServletRequest);
	Map<String, Object> verifyOtp(MfaVerifyRequest mfaVerifyRequest, HttpServletRequest httpServletRequest);
}
