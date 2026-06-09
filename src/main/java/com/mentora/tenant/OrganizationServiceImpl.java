package com.mentora.tenant;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.mentora.audit.AuditEventService;
import com.mentora.common.TenantContext;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrganizationServiceImpl implements OrganizationService {

	private final OrganizationRepository organizationRepository;
	private final TenantContext tenantContext;
	private final AuditEventService auditEventService;

	@Override
	public Organization createOrganization(Organization organization, HttpServletRequest httpServletRequest) {
		UUID tenantId = tenantContext.resolveTenantId(httpServletRequest, organization.getTenantId());
		organization.setTenantId(tenantId);
		organization.setActive(true);
		Organization created = organizationRepository.save(organization);
		auditEventService.log(tenantId, null, "ORG_CREATED", httpServletRequest);
		return created;
	}

	@Override
	public List<Organization> organizations(HttpServletRequest httpServletRequest) {
		return organizationRepository.findByTenantId(tenantContext.requireTenantId(httpServletRequest));
	}
}
