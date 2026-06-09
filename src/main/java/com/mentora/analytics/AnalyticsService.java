package com.mentora.analytics;

import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;

public interface AnalyticsService {
	Map<String, Object> analytics(HttpServletRequest httpServletRequest);
}
