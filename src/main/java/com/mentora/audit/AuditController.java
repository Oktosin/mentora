package com.mentora.audit;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = "audit")
@RequiredArgsConstructor
public class AuditController {

	private final AuditService auditService;

	@RequestMapping(value = "/activity", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE, method = {RequestMethod.POST, RequestMethod.GET})
	public List<ActivityLog> activity(HttpServletRequest request) {
		return auditService.activity(request);
	}
}
