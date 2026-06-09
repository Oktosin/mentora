package com.mentora.audit;

import java.util.List;

import org.springframework.stereotype.Service;

import com.mentora.common.TenantContext;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuditServiceImpl implements AuditService {

	private final ActivityLogRepository activityLogRepository;
	private final TenantContext tenantContext;

	@Override
	public List<ActivityLog> activity(HttpServletRequest httpServletRequest) {
		return activityLogRepository.findByTenantId(tenantContext.requireTenantId(httpServletRequest));
	}
}
