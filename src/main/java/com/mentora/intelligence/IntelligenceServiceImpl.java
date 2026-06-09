package com.mentora.intelligence;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.mentora.audit.AuditEventService;
import com.mentora.common.TenantContext;
import com.mentora.learning.Course;
import com.mentora.learning.CourseRepository;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class IntelligenceServiceImpl implements IntelligenceService {

	private final AnonymousPostRepository anonymousPostRepository;
	private final RecommendationRepository recommendationRepository;
	private final CourseRepository courseRepository;
	private final TenantContext tenantContext;
	private final AuditEventService auditEventService;

	@Override
	public AnonymousPost feedback(FeedbackRequest requestPayload, HttpServletRequest httpServletRequest) {
		UUID tenantId = tenantContext.requireTenantId(httpServletRequest);
		AnonymousPost post = new AnonymousPost();
		post.setTenantId(tenantId);
		post.setChallenge(requestPayload.challenge());
		post.setCategory(classify(requestPayload.challenge()));
		post.setSubmittedAt(Instant.now());
		post = anonymousPostRepository.save(post);

		Recommendation recommendation = buildRecommendation(tenantId, post);
		recommendationRepository.save(recommendation);
		auditEventService.log(tenantId, null, "ANONYMOUS_FEEDBACK_SUBMITTED", httpServletRequest);
		return post;
	}

	private Recommendation buildRecommendation(UUID tenantId, AnonymousPost post) {
		Recommendation recommendation = new Recommendation();
		recommendation.setTenantId(tenantId);
		recommendation.setAnonymousPost(post);
		recommendation.setCategory(post.getCategory());
		recommendation.setRecommendationType("COURSE_OR_SOP");
		Course matchingCourse = courseRepository.findByTenantId(tenantId).stream()
				.filter((course) -> course.getTags() != null && course.getTags().toUpperCase().contains(post.getCategory()))
				.findFirst()
				.orElse(null);
		if (matchingCourse != null) {
			recommendation.setTitle(matchingCourse.getTitle());
			recommendation.setRecommendedResourceId(matchingCourse.getId().toString());
			recommendation.setRationale("Matched workforce feedback category to course tags.");
		} else {
			recommendation.setTitle("Review content tagged " + post.getCategory());
			recommendation.setRationale("No direct course match found; route to SOP/expert review.");
		}
		return recommendation;
	}

	@Override
	public List<AnonymousPost> feedback(HttpServletRequest httpServletRequest) {
		return anonymousPostRepository.findByTenantId(tenantContext.requireTenantId(httpServletRequest));
	}

	@Override
	public List<Recommendation> recommendations(HttpServletRequest httpServletRequest) {
		return recommendationRepository.findByTenantId(tenantContext.requireTenantId(httpServletRequest));
	}

	private String classify(String text) {
		String lower = text == null ? "" : text.toLowerCase();
		if (lower.contains("policy") || lower.contains("compliance") || lower.contains("approval")) {
			return "COMPLIANCE_ISSUE";
		}
		if (lower.contains("slow") || lower.contains("process") || lower.contains("delay")) {
			return "PROCESS_INEFFICIENCY";
		}
		return "KNOWLEDGE_GAP";
	}
}
