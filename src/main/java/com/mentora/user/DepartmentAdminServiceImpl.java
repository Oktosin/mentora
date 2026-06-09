package com.mentora.user;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.mentora.common.UserAuthority;
import com.mentora.common.UserRole;
import com.mentora.common.UtilService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DepartmentAdminServiceImpl implements DepartmentAdminService {
	
	private final UserDataDAO userDAO;
	private final DepartmentAdminDAO departmentAdminDAO;
	private final UtilService utilService;
	private final PasswordEncoder passwordEncoder;
	
	@Override
	public ResponseEntity<?> createDepartmentAdmin(RegistrationRequest registrationRequest) {
		Map<String, Object> response = new LinkedHashMap<>();
		
		UserData userData = new UserData();
		LocalDateTime presentDateTime = LocalDateTime.now();
		Set<UserAuthority> authorities = new HashSet<>(UserAuthority.getAllUserAuthorityEnumByString("DEPARTMENT_ADMIN"));
		
		userData.setEmail(registrationRequest.getEmail());
		userData.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
		userData.setTenantId(registrationRequest.getTenantId());
		userData.setUserRole(UserRole.DEPARTMENT_ADMIN);
		userData.setAuthorities(authorities);
		userData.setCreatedAT(presentDateTime);
		userData.setUpdatedAt(presentDateTime);
		userData = userDAO.saveAndFlush(userData);
		
		DepartmentAdmin departmentAdmin = new DepartmentAdmin();
		
		departmentAdmin.setUserData(userData);
		departmentAdmin.setFirstName(registrationRequest.getFirstName());
		departmentAdmin.setLastName(registrationRequest.getLastName());
		departmentAdmin = departmentAdminDAO.saveAndFlush(departmentAdmin);
		
		DepartmentAdminRegistrationResponse departmentAdminRegistrationResponse = utilService.getBasicDepartmentAdminInformation(userData, departmentAdmin);
		response.put("OrganizationAdminInfo", departmentAdminRegistrationResponse);
		
		return utilService.getResponse(response, HttpStatus.OK);
	}

}
