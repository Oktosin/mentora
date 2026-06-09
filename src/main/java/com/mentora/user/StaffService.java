package com.mentora.user;

import org.springframework.http.ResponseEntity;

public interface StaffService {

	ResponseEntity<?> createStaff(RegistrationRequest registrationRequest);

}
