package com.mentora.intelligence;

import java.util.List;

import jakarta.servlet.http.HttpServletRequest;

public interface IntelligenceService {
	AnonymousPost feedback(FeedbackRequest feedback, HttpServletRequest httpServletRequest);
	List<AnonymousPost> feedback(HttpServletRequest httpServletRequest);
	List<Recommendation> recommendations(HttpServletRequest httpServletRequest);
}
