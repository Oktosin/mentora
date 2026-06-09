package com.mentora.common;

import java.util.UUID;

import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TenantContext {

	public static final String TENANT_HEADER = "X-Tenant-Id";
	public static final String AUTHENTICATED_TENANT_ATTRIBUTE = "mentora.authenticatedTenantId";

	public UUID requireTenantId(HttpServletRequest request) {
		String value = request.getHeader(TENANT_HEADER);
		Object authenticatedTenantId = request.getAttribute(AUTHENTICATED_TENANT_ATTRIBUTE);
		if (value == null || value.isBlank()) {
			if (authenticatedTenantId instanceof UUID tenantId) {
				return tenantId;
			}
			throw new IllegalArgumentException("Missing X-Tenant-Id header");
		}
		UUID tenantId = UUID.fromString(value);
		if (authenticatedTenantId instanceof UUID authenticated && !authenticated.equals(tenantId)) {
			throw new IllegalArgumentException("Tenant header does not match authenticated tenant");
		}
		return tenantId;
	}

	public UUID resolveTenantId(HttpServletRequest request, UUID fallbackTenantId) {
		String value = request.getHeader(TENANT_HEADER);
		Object authenticatedTenantId = request.getAttribute(AUTHENTICATED_TENANT_ATTRIBUTE);
		if (value != null && !value.isBlank()) {
			UUID tenantId = UUID.fromString(value);
			if (authenticatedTenantId instanceof UUID authenticated && !authenticated.equals(tenantId)) {
				throw new IllegalArgumentException("Tenant header does not match authenticated tenant");
			}
			return tenantId;
		}
		if (fallbackTenantId != null) {
			if (authenticatedTenantId instanceof UUID authenticated && !authenticated.equals(fallbackTenantId)) {
				throw new IllegalArgumentException("Request tenant does not match authenticated tenant");
			}
			return fallbackTenantId;
		}
		if (authenticatedTenantId instanceof UUID tenantId) {
			return tenantId;
		}
		throw new IllegalArgumentException("Missing tenant id");
	}
}
