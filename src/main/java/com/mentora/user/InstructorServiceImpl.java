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
public class InstructorServiceImpl implements InstructorService {
	
	private final UserDataDAO userDAO;
	private final InstructorDAO instructorDAO;
	private final UtilService utilService;
	private final PasswordEncoder passwordEncoder;
	
	@Override
	public ResponseEntity<?> createInstructor(RegistrationRequest registrationRequest) {
		Map<String, Object> response = new LinkedHashMap<>();
		
		UserData userData = new UserData();
		LocalDateTime presentDateTime = LocalDateTime.now();
		Set<UserAuthority> authorities = new HashSet<>(UserAuthority.getAllUserAuthorityEnumByString("INSTRUCTOR"));
		
		userData.setEmail(registrationRequest.getEmail());
		userData.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
		userData.setTenantId(registrationRequest.getTenantId());
		userData.setUserRole(UserRole.INSTRUCTOR);
		userData.setAuthorities(authorities);
		userData.setCreatedAT(presentDateTime);
		userData.setUpdatedAt(presentDateTime);
		userData = userDAO.saveAndFlush(userData);
		
		Instructor instructor = new Instructor();
		
		instructor.setUserData(userData);
		instructor.setFirstName(registrationRequest.getFirstName());
		instructor.setLastName(registrationRequest.getLastName());
		instructor = instructorDAO.saveAndFlush(instructor);
		
		InstructorRegistrationResponse instructorRegistrationResponse = utilService.getBasicInstructorInformation(userData, instructor);
		response.put("InstructorInfo", instructorRegistrationResponse);
		
		return utilService.getResponse(response, HttpStatus.OK);
	}
	

}
