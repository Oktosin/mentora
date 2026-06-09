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
public class OrganizationAdminServiceImpl implements OrganizationAdminService {
	
	private final UserDataDAO userDAO;
	private final OrganizationAdminDAO organizationAdminDAO;
	private final UtilService utilService;
	private final PasswordEncoder passwordEncoder;
	
	@Override
	public ResponseEntity<?> createOrganizationAdmin(RegistrationRequest registrationRequest) {
		Map<String, Object> response = new LinkedHashMap<>();
		
		UserData userData = new UserData();
		LocalDateTime presentDateTime = LocalDateTime.now();
		Set<UserAuthority> authorities = new HashSet<>(UserAuthority.getAllUserAuthorityEnumByString("ORGANIZATION_ADMIN"));
		
		userData.setEmail(registrationRequest.getEmail());
		userData.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
		userData.setTenantId(registrationRequest.getTenantId());
		userData.setUserRole(UserRole.ORGANIZATION_ADMIN);
		userData.setAuthorities(authorities);
		userData.setCreatedAT(presentDateTime);
		userData.setUpdatedAt(presentDateTime);
		userData = userDAO.saveAndFlush(userData);
		
		OrganizationAdmin organizationAdmin = new OrganizationAdmin();
		
		organizationAdmin.setUserData(userData);
		organizationAdmin.setFirstName(registrationRequest.getFirstName());
		organizationAdmin.setLastName(registrationRequest.getLastName());
		organizationAdmin = organizationAdminDAO.saveAndFlush(organizationAdmin);
		
		OrganizationAdminRegistrationResponse organizationAdminRegistrationResponse = utilService.getBasicOrganizationAdminInformation(userData, organizationAdmin);
		response.put("OrganizationAdminInfo", organizationAdminRegistrationResponse);
		
		return utilService.getResponse(response, HttpStatus.OK);
	}
	

}
