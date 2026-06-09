package com.mentora.security;

import java.time.LocalDateTime;

import com.mentora.user.BasicUserInformation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginResponse {
	
	private BasicUserInformation basicUserInformation;
	private LocalDateTime lastLogin;
	private String message;
	private String accessToken;
	private String refreshToken;
	private long accessTokenExpiresInSeconds;


}
