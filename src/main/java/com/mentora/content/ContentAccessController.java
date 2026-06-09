package com.mentora.content;

import java.util.Map;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = "content")
@RequiredArgsConstructor
public class ContentAccessController {

	private final ContentAccessService contentAccessService;

	@RequestMapping(value = "/{assetKey}/access", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE, method = {RequestMethod.POST, RequestMethod.GET})
	public Map<String, Object> contentAccess(@PathVariable String assetKey, HttpServletRequest request) {
		return contentAccessService.contentAccess(assetKey, request);
	}
}
