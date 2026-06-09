package com.mentora.security;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.mentora.audit.AuditEventService;
import com.mentora.user.UserData;
import com.mentora.user.UserDataDAO;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MfaServiceImpl implements MfaService {

	private static final int OTP_EXPIRY_MINUTES = 10;

	private final UserDataDAO userDataDAO;
	private final MfaChallengeRepository mfaChallengeRepository;
	private final PasswordEncoder passwordEncoder;
	private final AuditEventService auditEventService;
	private final SecureRandom secureRandom = new SecureRandom();

	@Override
	public Map<String, Object> requestOtp(MfaRequest mfaRequest, HttpServletRequest httpServletRequest) {
		Map<String, Object> response = new LinkedHashMap<>();
		response.put("systemStatusResponse", "If the account exists, an MFA challenge has been created");
		if (mfaRequest == null || mfaRequest.getUserId() == null || mfaRequest.getUserId().isBlank()) {
			return response;
		}
		UserData userData = userDataDAO.findUserDataByEmail(mfaRequest.getUserId());
		if (userData == null) {
			return response;
		}
		String otp = String.format("%06d", secureRandom.nextInt(1000000));
		MfaChallenge challenge = new MfaChallenge();
		challenge.setTenantId(userData.getTenantId());
		challenge.setUserData(userData);
		challenge.setOtpHash(passwordEncoder.encode(otp));
		challenge.setChannel(mfaRequest.getChannel() == null ? "EMAIL" : mfaRequest.getChannel());
		challenge.setExpiresAt(LocalDateTime.now().plusMinutes(OTP_EXPIRY_MINUTES));
		challenge.setUsed(false);
		mfaChallengeRepository.save(challenge);
		auditEventService.log(userData.getTenantId(), userData.getId(), "MFA_CHALLENGE_CREATED", httpServletRequest);
		response.put("deliveryStatus", "MFA_DELIVERY_PROVIDER_NOT_CONFIGURED");
		response.put("expiresInMinutes", OTP_EXPIRY_MINUTES);
		return response;
	}

	@Override
	public Map<String, Object> verifyOtp(MfaVerifyRequest mfaVerifyRequest, HttpServletRequest httpServletRequest) {
		Map<String, Object> response = new LinkedHashMap<>();
		if (mfaVerifyRequest == null || mfaVerifyRequest.getUserId() == null || mfaVerifyRequest.getOtp() == null) {
			response.put("systemStatusResponse", "User id and OTP are required");
			return response;
		}
		UserData userData = userDataDAO.findUserDataByEmail(mfaVerifyRequest.getUserId());
		if (userData == null) {
			response.put("systemStatusResponse", "Invalid MFA challenge");
			return response;
		}
		MfaChallenge challenge = mfaChallengeRepository
				.findByTenantIdAndUserDataAndUsedFalseOrderByCreatedATDesc(userData.getTenantId(), userData, PageRequest.of(0, 1))
				.stream()
				.findFirst()
				.orElse(null);
		if (challenge == null || challenge.getExpiresAt().isBefore(LocalDateTime.now())
				|| !passwordEncoder.matches(mfaVerifyRequest.getOtp(), challenge.getOtpHash())) {
			response.put("systemStatusResponse", "Invalid or expired MFA challenge");
			return response;
		}
		challenge.setUsed(true);
		challenge.setVerifiedAt(LocalDateTime.now());
		mfaChallengeRepository.save(challenge);
		auditEventService.log(userData.getTenantId(), userData.getId(), "MFA_CHALLENGE_VERIFIED", httpServletRequest);
		response.put("systemStatusResponse", "MFA verified successfully");
		return response;
	}
}
