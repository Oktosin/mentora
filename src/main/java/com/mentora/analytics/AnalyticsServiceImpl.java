package com.mentora.analytics;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.mentora.audit.ActivityLogRepository;
import com.mentora.common.TenantContext;
import com.mentora.intelligence.AnonymousPostRepository;
import com.mentora.intelligence.RecommendationRepository;
import com.mentora.learning.EnrollmentRepository;
import com.mentora.learning.LessonProgressRepository;
import com.mentora.user.UserDataDAO;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AnalyticsServiceImpl implements AnalyticsService {

	private final UserDataDAO userDataDAO;
	private final AnonymousPostRepository anonymousPostRepository;
	private final RecommendationRepository recommendationRepository;
	private final ActivityLogRepository activityLogRepository;
	private final EnrollmentRepository enrollmentRepository;
	private final LessonProgressRepository lessonProgressRepository;
	private final TenantContext tenantContext;

	@Override
	public Map<String, Object> analytics(HttpServletRequest request) {
		UUID tenantId = tenantContext.requireTenantId(request);
		Map<String, Object> analytics = new LinkedHashMap<>();
		analytics.put("users", userDataDAO.findByTenantId(tenantId).size());
		analytics.put("anonymousFeedback", anonymousPostRepository.countByTenantId(tenantId));
		analytics.put("recommendations", recommendationRepository.countByTenantId(tenantId));
		analytics.put("activityEvents", activityLogRepository.countByTenantId(tenantId));
		analytics.put("enrollments", enrollmentRepository.countByTenantId(tenantId));
		analytics.put("completedEnrollments", enrollmentRepository.countByTenantIdAndCompletedAtIsNotNull(tenantId));
		analytics.put("completedLessons", lessonProgressRepository.countByTenantIdAndCompletedTrue(tenantId));
		return analytics;
	}
}
