package com.mentora.user;

import org.springframework.http.ResponseEntity;

public interface InstructorService {

	ResponseEntity<?> createInstructor(RegistrationRequest registrationRequest);

}
