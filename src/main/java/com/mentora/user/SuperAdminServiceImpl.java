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
public class SuperAdminServiceImpl implements SuperAdminService {
	
	private final UserDataDAO userDAO;
	private final SuperAdminDAO superAdminDAO;
	private final UtilService utilService;
	private final PasswordEncoder passwordEncoder;
	
	@Override
	public ResponseEntity<?> createSuperAdmin(RegistrationRequest registrationRequest) {
		Map<String, Object> response = new LinkedHashMap<>();
		
		UserData userData = new UserData();
		LocalDateTime presentDateTime = LocalDateTime.now();
		Set<UserAuthority> authorities = new HashSet<>(UserAuthority.getAllUserAuthorityEnumByString("SUPER_ADMIN"));
		
		userData.setEmail(registrationRequest.getEmail());
		userData.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
		userData.setTenantId(registrationRequest.getTenantId());
		userData.setUserRole(UserRole.SUPER_ADMIN);
		userData.setAuthorities(authorities);
		userData.setCreatedAT(presentDateTime);
		userData.setUpdatedAt(presentDateTime);
		userData = userDAO.saveAndFlush(userData);
		
		SuperAdmin superAdmin = new SuperAdmin();
		
		superAdmin.setUserData(userData);
		superAdmin.setFirstName(registrationRequest.getFirstName());
		superAdmin.setLastName(registrationRequest.getLastName());
		superAdmin = superAdminDAO.saveAndFlush(superAdmin);
		
		SuperAdminRegistrationResponse superAdminRegistrationResponse = utilService.getBasicSuperAdminInformation(userData, superAdmin);
		response.put("SuperAdminInfo", superAdminRegistrationResponse);
		
		return utilService.getResponse(response, HttpStatus.OK);
	}

}

