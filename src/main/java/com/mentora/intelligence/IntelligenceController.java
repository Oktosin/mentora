package com.mentora.intelligence;

import java.util.List;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class IntelligenceController {

	private final IntelligenceService intelligenceService;

	@RequestMapping(value = "/feedback", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE, method = {RequestMethod.POST, RequestMethod.GET})
	public AnonymousPost feedback(@RequestBody FeedbackRequest feedback, HttpServletRequest request) {
		return intelligenceService.feedback(feedback, request);
	}

	@RequestMapping(value = "/find-feedback", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE, method = {RequestMethod.POST, RequestMethod.GET})
	public List<AnonymousPost> feedback(HttpServletRequest request) {
		return intelligenceService.feedback(request);
	}

	@RequestMapping(value = "/recommendations", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE, method = {RequestMethod.POST, RequestMethod.GET})
	public List<Recommendation> recommendations(HttpServletRequest request) {
		return intelligenceService.recommendations(request);
	}
}
