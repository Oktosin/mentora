package com.mentora.user;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.mentora.common.RequestPayload;
import com.mentora.common.TenantContext;
import com.mentora.common.UserRole;
import com.mentora.common.UtilService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserManagementServiceImpl implements UserManagementService {

	private final SuperAdminService superAdminService;
	private final OrganizationAdminService organizationAdminService;
	private final DepartmentAdminService departmentAdminService;
	private final InstructorService instructorService;
	private final StaffService staffService;
	private final UserDataDAO userDataDAO;
	private final TenantContext tenantContext;
	private final UtilService utilService;

	@Override
	public ResponseEntity<?> register(RegistrationRequest registrationRequest, HttpServletRequest httpServletRequest) {
		registrationRequest.setTenantId(tenantContext.resolveTenantId(httpServletRequest, registrationRequest.getTenantId()));
		UserRole role = registrationRequest.getUserRole() == null ? UserRole.STAFF : registrationRequest.getUserRole();
		if (role == UserRole.SUPER_ADMIN) {
			return superAdminService.createSuperAdmin(registrationRequest);
		}
		if (role == UserRole.ORGANIZATION_ADMIN) {
			return organizationAdminService.createOrganizationAdmin(registrationRequest);
		}
		if (role == UserRole.DEPARTMENT_ADMIN) {
			return departmentAdminService.createDepartmentAdmin(registrationRequest);
		}
		if (role == UserRole.INSTRUCTOR) {
			return instructorService.createInstructor(registrationRequest);
		}
		return staffService.createStaff(registrationRequest);
	}

	
	@Override
	public ResponseEntity<?> getAllUsers(HttpServletRequest httpServletRequest, RequestPayload requestPayload) {
		
		Map<String, Object> response = new LinkedHashMap<>();
		
		UUID tenantId = tenantContext.requireTenantId(httpServletRequest);
		int pageNumber = requestPayload.getPageNumber();
		int pageSize = utilService.pageSizeLimiter(requestPayload.getPageSize());
		
		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		Page<UserData> page = userDataDAO.findByTenantId(tenantId, pageable);
		
		Page<BasicUserInformation> basicUserInformation = page.map(user -> BasicUserInformation.builder()
				.email(user.getEmail())
				.userRole(user.getUserRole())
				.build());
				
		
		response.put("users", basicUserInformation.getContent());
		response.put("currentPage", basicUserInformation.getNumber());
		response.put("totalPages", basicUserInformation.getTotalPages());
		response.put("totalElements", basicUserInformation.getTotalElements());
		
		return utilService.getResponse(response, HttpStatus.OK);
	}
}
