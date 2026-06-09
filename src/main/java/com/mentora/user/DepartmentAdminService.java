package com.mentora.user;

import org.springframework.http.ResponseEntity;

public interface DepartmentAdminService {

	ResponseEntity<?> createDepartmentAdmin(RegistrationRequest registrationRequest);

}
