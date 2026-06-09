package com.mentora.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLogoutRequest {
	
	private String requestLogout;
	private String refreshToken;


}
