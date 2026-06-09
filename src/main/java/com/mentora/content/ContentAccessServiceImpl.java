package com.mentora.content;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.mentora.audit.AuditEventService;
import com.mentora.common.TenantContext;
import com.mentora.security.TokenService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ContentAccessServiceImpl implements ContentAccessService {

	private final TokenService tokenService;
	private final TenantContext tenantContext;
	private final AuditEventService auditEventService;

	@Override
	public Map<String, Object> contentAccess(String assetKey, HttpServletRequest httpServletRequest) {
		UUID tenantId = tenantContext.requireTenantId(httpServletRequest);
		Map<String, Object> response = new LinkedHashMap<>();
		response.put("tenantId", tenantId);
		response.put("assetKey", assetKey);
		response.put("accessMode", "SIGNED_TOKEN");
		response.put("contentToken", tokenService.createContentToken(tenantId, assetKey, 300));
		response.put("expiresAt", Instant.now().plusSeconds(300));
		response.put("policy", "Token-based, tenant-scoped content access. Use contentToken to sign CDN/storage URLs.");
		auditEventService.log(tenantId, null, "CONTENT_ACCESS_TOKEN_CREATED", httpServletRequest);
		return response;
	}
}
