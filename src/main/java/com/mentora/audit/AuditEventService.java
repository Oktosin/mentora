package com.mentora.audit;

import java.time.Instant;
import java.util.UUID;

import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuditEventService {

	private final ActivityLogRepository activityLogRepository;

	public void log(UUID tenantId, UUID userId, String action, HttpServletRequest request) {
		if (tenantId == null) {
			return;
		}
		ActivityLog log = new ActivityLog();
		log.setTenantId(tenantId);
		log.setUserId(userId);
		log.setAction(action);
		log.setTimestamp(Instant.now());
		log.setIpAddress(request.getRemoteAddr());
		log.setDevice(request.getHeader("User-Agent"));
		activityLogRepository.save(log);
	}
}
