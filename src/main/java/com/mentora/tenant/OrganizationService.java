package com.mentora.tenant;

import java.util.List;

import jakarta.servlet.http.HttpServletRequest;

public interface OrganizationService {
	Organization createOrganization(Organization organization, HttpServletRequest httpServletRequest);
	List<Organization> organizations(HttpServletRequest httpServletRequest);
}
