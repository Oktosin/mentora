package com.mentora.intelligence;

import java.util.List;

import jakarta.servlet.http.HttpServletRequest;

public interface IntelligenceService {
	AnonymousPost feedback(FeedbackRequest feedback, HttpServletRequest request);
	List<AnonymousPost> feedback(HttpServletRequest request);
	List<Recommendation> recommendations(HttpServletRequest request);
}
