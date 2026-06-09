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
public class StaffServiceImpl implements StaffService { 
	
	private final UserDataDAO userDAO;
	private final StaffDAO staffDAO;
	private final UtilService utilService;
	private final PasswordEncoder passwordEncoder;
	
	@Override
	public ResponseEntity<?> createStaff(RegistrationRequest registrationRequest) {
		Map<String, Object> response = new LinkedHashMap<>();
		
		UserData userData = new UserData();
		LocalDateTime presentDateTime = LocalDateTime.now();
		Set<UserAuthority> authorities = new HashSet<>(UserAuthority.getAllUserAuthorityEnumByString("STAFF"));
		
		userData.setEmail(registrationRequest.getEmail());
		userData.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
		userData.setTenantId(registrationRequest.getTenantId());
		userData.setUserRole(UserRole.STAFF);
		userData.setAuthorities(authorities);
		userData.setCreatedAT(presentDateTime);
		userData.setUpdatedAt(presentDateTime);
		userData = userDAO.saveAndFlush(userData);
		
		Staff staff = new Staff();
		
		staff.setUserData(userData);
		staff.setFirstName(registrationRequest.getFirstName());
		staff.setLastName(registrationRequest.getLastName());
		staff = staffDAO.saveAndFlush(staff);
		
		StaffRegistrationResponse staffRegistrationResponse = utilService.getBasicStaffInformation(userData, staff);
		response.put("StaffInfo", staffRegistrationResponse);
		
		return utilService.getResponse(response, HttpStatus.OK);
	}
	

}
