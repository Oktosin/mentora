package com.mentora.user;

import org.springframework.http.ResponseEntity;

public interface SuperAdminService {

	ResponseEntity<?> createSuperAdmin(RegistrationRequest registrationRequest);

}
