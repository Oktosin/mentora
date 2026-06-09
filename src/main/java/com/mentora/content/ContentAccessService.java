package com.mentora.content;

import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;

public interface ContentAccessService {
	Map<String, Object> contentAccess(String assetKey, HttpServletRequest httpServletRequest);
}
