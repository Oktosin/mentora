package com.mentora.tenant;

import java.util.List;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = "orgs")
@RequiredArgsConstructor
public class OrganizationController {

	private final OrganizationService organizationService;

	@RequestMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE, method = {RequestMethod.POST, RequestMethod.GET})
	public Organization createOrganization(@RequestBody Organization organization, HttpServletRequest request) {
		return organizationService.createOrganization(organization, request);
	}

	@RequestMapping(value = "/find-all", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE, method = {RequestMethod.POST, RequestMethod.GET})
	public List<Organization> organizations(HttpServletRequest request) {
		return organizationService.organizations(request);
	}
}
