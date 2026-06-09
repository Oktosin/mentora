package com.mentora.user;

import java.util.UUID;

import com.mentora.common.UserRole;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationRequest {
	
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private UUID tenantId;
    private UserRole userRole;

}
