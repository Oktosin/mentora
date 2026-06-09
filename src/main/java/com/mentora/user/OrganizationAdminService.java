package com.mentora.user;

import org.springframework.http.ResponseEntity;

public interface OrganizationAdminService {

	ResponseEntity<?> createOrganizationAdmin(RegistrationRequest registrationRequest);

}
