package com.mentora.audit;

import java.util.List;

import jakarta.servlet.http.HttpServletRequest;

public interface AuditService {
	List<ActivityLog> activity(HttpServletRequest httpServletRequest);
}
