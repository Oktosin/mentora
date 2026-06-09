package com.mentora.security;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.mentora.user.UserData;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TokenService {

	private static final String HMAC_ALGORITHM = "HmacSHA256";

	@Value("${mentora.security.token-secret:change-this-secret-in-production}")
	private String tokenSecret;

	@Value("${mentora.security.access-token-ttl-seconds:900}")
	private long accessTokenTtlSeconds;

	@Value("${mentora.security.refresh-token-ttl-seconds:604800}")
	private long refreshTokenTtlSeconds;

	public String createAccessToken(UserData userData) {
		return createToken(userData, "access", accessTokenTtlSeconds);
	}

	public String createRefreshToken(UserData userData) {
		return createToken(userData, "refresh", refreshTokenTtlSeconds);
	}

	public String createContentToken(UUID tenantId, String assetKey, long ttlSeconds) {
		Map<String, Object> payload = new LinkedHashMap<>();
		payload.put("typ", "content");
		payload.put("tenantId", tenantId.toString());
		payload.put("assetKey", assetKey);
		payload.put("exp", Instant.now().plusSeconds(ttlSeconds).getEpochSecond());
		return sign(payload);
	}

	public long getAccessTokenTtlSeconds() {
		return accessTokenTtlSeconds;
	}

	public long getRefreshTokenTtlSeconds() {
		return refreshTokenTtlSeconds;
	}

	public Map<String, String> verifyToken(String token, String expectedType) {
		String[] parts = token.split("\\.");
		if (parts.length != 3) {
			throw new IllegalArgumentException("Invalid token format");
		}
		String expectedSignature = hmac(parts[0] + "." + parts[1]);
		if (!MessageDigest.isEqual(expectedSignature.getBytes(StandardCharsets.UTF_8), parts[2].getBytes(StandardCharsets.UTF_8))) {
			throw new IllegalArgumentException("Invalid token signature");
		}
		Map<String, String> claims = decodeJson(parts[1]);
		if (!expectedType.equals(claims.get("typ"))) {
			throw new IllegalArgumentException("Invalid token type");
		}
		long expiresAt = Long.parseLong(claims.getOrDefault("exp", "0"));
		if (Instant.now().getEpochSecond() > expiresAt) {
			throw new IllegalArgumentException("Token expired");
		}
		return claims;
	}

	private String createToken(UserData userData, String tokenType, long ttlSeconds) {
		Map<String, Object> payload = new LinkedHashMap<>();
		payload.put("typ", tokenType);
		payload.put("sub", userData.getId().toString());
		payload.put("tenantId", userData.getTenantId().toString());
		payload.put("email", userData.getEmail());
		payload.put("role", userData.getUserRole().name());
		payload.put("exp", Instant.now().plusSeconds(ttlSeconds).getEpochSecond());
		return sign(payload);
	}

	private String sign(Map<String, Object> payload) {
		String header = encodeJson(Map.of("alg", "HS256", "typ", "JWT"));
		String body = encodeJson(payload);
		String signature = hmac(header + "." + body);
		return header + "." + body + "." + signature;
	}

	private String encodeJson(Map<String, Object> values) {
		StringBuilder json = new StringBuilder("{");
		boolean first = true;
		for (Map.Entry<String, Object> entry : values.entrySet()) {
			if (!first) {
				json.append(",");
			}
			first = false;
			json.append("\"").append(entry.getKey()).append("\":");
			Object value = entry.getValue();
			if (value instanceof Number || value instanceof Boolean) {
				json.append(value);
			} else {
				json.append("\"").append(String.valueOf(value).replace("\\", "\\\\").replace("\"", "\\\"")).append("\"");
			}
		}
		json.append("}");
		return Base64.getUrlEncoder().withoutPadding().encodeToString(json.toString().getBytes(StandardCharsets.UTF_8));
	}

	private String hmac(String value) {
		try {
			Mac mac = Mac.getInstance(HMAC_ALGORITHM);
			mac.init(new SecretKeySpec(tokenSecret.getBytes(StandardCharsets.UTF_8), HMAC_ALGORITHM));
			return Base64.getUrlEncoder().withoutPadding().encodeToString(mac.doFinal(value.getBytes(StandardCharsets.UTF_8)));
		} catch (Exception exception) {
			throw new IllegalStateException("Unable to sign token", exception);
		}
	}

	private Map<String, String> decodeJson(String encoded) {
		String json = new String(Base64.getUrlDecoder().decode(encoded), StandardCharsets.UTF_8).trim();
		Map<String, String> values = new HashMap<>();
		if (json.length() < 2) {
			return values;
		}
		String body = json.substring(1, json.length() - 1);
		if (body.isBlank()) {
			return values;
		}
		for (String item : body.split(",")) {
			String[] pair = item.split(":", 2);
			if (pair.length == 2) {
				values.put(unquote(pair[0]), unquote(pair[1]));
			}
		}
		return values;
	}

	private String unquote(String value) {
		String trimmed = value.trim();
		if (trimmed.startsWith("\"") && trimmed.endsWith("\"")) {
			return trimmed.substring(1, trimmed.length() - 1).replace("\\\"", "\"").replace("\\\\", "\\");
		}
		return trimmed;
	}
}
