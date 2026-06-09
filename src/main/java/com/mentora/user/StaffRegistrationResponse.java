package com.mentora.user;

import com.mentora.common.UserRole;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StaffRegistrationResponse {
	
	 private String firstName;
	 private String lastName;
	 private String email;
	 private UserRole userRole;

}
