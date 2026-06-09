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
	public Organization createOrganization(Organization organization, HttpServletRequest request) {
		UUID tenantId = tenantContext.resolveTenantId(request, organization.getTenantId());
		organization.setTenantId(tenantId);
		organization.setActive(true);
		Organization created = organizationRepository.save(organization);
		auditEventService.log(tenantId, null, "ORG_CREATED", request);
		return created;
	}

	@Override
	public List<Organization> organizations(HttpServletRequest request) {
		return organizationRepository.findByTenantId(tenantContext.requireTenantId(request));
	}
}
