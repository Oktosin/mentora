package com.mentora.security;

import org.springframework.stereotype.Service;

import com.mentora.audit.AuditEventService;
import com.mentora.common.UserRole;
import com.mentora.common.UtilService;
import com.mentora.user.BasicUserInformation;
import com.mentora.user.DepartmentAdmin;
import com.mentora.user.DepartmentAdminDAO;
import com.mentora.user.Instructor;
import com.mentora.user.InstructorDAO;
import com.mentora.user.OrganizationAdmin;
import com.mentora.user.OrganizationAdminDAO;
import com.mentora.user.Staff;
import com.mentora.user.StaffDAO;
import com.mentora.user.SuperAdmin;
import com.mentora.user.SuperAdminDAO;
import com.mentora.user.UserData;
import com.mentora.user.UserDataDAO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.context.SecurityContextRepository;

import lombok.RequiredArgsConstructor;


@Service
@Transactional
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
	
	private final PasswordEncoder passwordEncoder;
	private final UserDataDAO userDataDAO;
	private final SuperAdminDAO superAdminDAO;
	private final OrganizationAdminDAO organizationAdminDAO;
	private final DepartmentAdminDAO departmentAdminDAO;
	private final InstructorDAO instructorDAO;
	private final StaffDAO staffDAO;
	private final UtilService utilService;
	private final TokenService tokenService;
	private final AuditEventService auditEventService;
	private final RefreshTokenRepository refreshTokenRepository;
	private final DeviceSessionRepository deviceSessionRepository;

	//REQUIRED SPRING SECURITY DEPENDENCY INJECTION FOR LOGIN AND LOGOUT OPERATION
	private final AuthenticationManager authenticationManager;
	private final SecurityContextRepository securityContextRepository; 
	private final SecurityContextHolderStrategy securityContextHolderStrategy;
	
	private final int MAXIMUM_INACTIVE_PERIOD_IN_MINUTES = 10;
	
	@Override
	public Map<String, Object> userLogout(UserLogoutRequest userLogoutRequest, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) {

		Map<String, Object> map = new LinkedHashMap<>();
	
		revokeRefreshToken(userLogoutRequest, httpServletRequest);
		boolean isUserLoggedOut = logoutUser(httpServletRequest, httpServletResponse, authentication);
		
		if (isUserLoggedOut) {
		
			map.put("systemStatusResponse", "You have been logged out successfully");

		} else {

			map.put("systemStatusResponse", "No active session found for logout operation");

		}

		return map;
	}    
	
	@Override
	public Map<String, Object> userLogin(UserLoginRequest userLoginRequest, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) {

		Map<String, Object> map = new LinkedHashMap<>();
		
		logoutUser(httpServletRequest, httpServletResponse, authentication);
		
		UserData userData = userDataDAO.findUserDataByEmail(userLoginRequest.getUserId());
		
		if (userData == null) {
			
			map.put("systemStatusResponse", "Invalid user");
			
		} else {
			
			boolean isValidated = passwordEncoder.matches(userLoginRequest.getPassword(), userData.getPassword());

			if (!isValidated) {

				map.put("systemStatusResponse", "Invalid user id or password");

			} else {
				
				//GET USER CREDENTIAL WRAPPED FOR AUTHENTICATION TOKEN
		        UsernamePasswordAuthenticationToken authenticationToken = UsernamePasswordAuthenticationToken.unauthenticated(userLoginRequest.getUserId(), userLoginRequest.getPassword());			        
		        	
			     Authentication newAuthentication = authenticationManager.authenticate(authenticationToken);
			       
			    if (newAuthentication.isAuthenticated()) {// IF USER IS AUTHENTICATED			        
			        
			        SecurityContext securityContext = securityContextHolderStrategy.createEmptyContext();

			        //SET SECURITY CONTEXT APPLICATION FROM AUTHENTICATION
			        securityContext.setAuthentication(newAuthentication);
			        securityContextHolderStrategy.setContext(securityContext);
			        
			        //SAVE THE AUTHENTICATION SECURITY CONTEXT
			        securityContextRepository.saveContext(securityContext, httpServletRequest, httpServletResponse);    
			        
			        //SET MAXIMUM INACTIVE INTERVAL TO INVALIDATE USER SESSION
			        //AN INTERVAL VALUE OF ZERO INDICATES THAT THE SESSION SHOULD NEVER TIMEOUT. 
			        httpServletRequest.getSession().setMaxInactiveInterval(MAXIMUM_INACTIVE_PERIOD_IN_MINUTES * 60);//CURRENTLY SET TO 10 MINUTES
			        
					LocalDateTime presentDateTime = LocalDateTime.now();
								
			        userData.setUpdatedAt(presentDateTime);
			        userDataDAO.saveAndFlush(userData);
			        
			        SuperAdmin	superAdmin = null;
			        OrganizationAdmin organizationAdmin = null;
			        DepartmentAdmin departmentAdmin = null;
			        Instructor instructor = null;
			        Staff staff = null;
			        
			        UserRole userRole = userData.getUserRole();
			        
			        if (userRole == UserRole.SUPER_ADMIN) {
			        	
			        	superAdmin = superAdminDAO.findSuperAdminByUserData(userData);
			        	
			        } else if (userRole == UserRole.ORGANIZATION_ADMIN) {
			        	
			        	organizationAdmin = organizationAdminDAO.findOrganizationAdminByUserData(userData);
			        	
			        } else if (userRole == UserRole.DEPARTMENT_ADMIN) {
			        	
			        	departmentAdmin = departmentAdminDAO.findDepartmentByUserData(userData);
			        	
			        } else if (userRole == UserRole.INSTRUCTOR) {
			        	
			        	instructor = instructorDAO.findInstructorByUserData(userData);
			        	
			        } else if (userRole == UserRole.STAFF) {
			        	
			        	staff = staffDAO.findStaffByUserData(userData);
			        }
			        
			        BasicUserInformation basicUserInformation = utilService.getBasicUserInformation(userData);
			        
			        if (basicUserInformation == null) {
			        	
			        	map.put("errorMessage", "user not found");
			        	
			        } else {
			        	
				        //SET USER LOGIN RESPONSE
						UserLoginResponse userLoginResponse = new UserLoginResponse();
						
						userLoginResponse.setBasicUserInformation(basicUserInformation);
						userLoginResponse.setLastLogin(presentDateTime);
						String accessToken = tokenService.createAccessToken(userData);
						String refreshToken = tokenService.createRefreshToken(userData);
						DeviceSession deviceSession = trackDeviceSession(userData, httpServletRequest);
						persistRefreshToken(userData, deviceSession, refreshToken);
						userLoginResponse.setAccessToken(accessToken);
						userLoginResponse.setRefreshToken(refreshToken);
						userLoginResponse.setAccessTokenExpiresInSeconds(tokenService.getAccessTokenTtlSeconds());
						auditEventService.log(userData.getTenantId(), userData.getId(), "USER_LOGIN_SUCCESS", httpServletRequest);
//						userLoginResponse.setRedirectUrl(uiRedirectUrl);
				        				        
				        //PUT RESPOSNE OBJECT INTO MAP
				        userLoginResponse.setMessage("Login successful");
						map.put("UserLoginResponse", userLoginResponse);			        	
			        	
			        } 
			   	        	  	
		        } else {
		        	
		        	map.put("systemStatusResponse", "Invalid login credentials");	
		        	
		        }
		       
			}
			
		}
					
		return map;
		
	}	

	@Override
	public Map<String, Object> refreshToken(RefreshTokenRequest refreshTokenRequest, HttpServletRequest httpServletRequest) {
		Map<String, Object> map = new LinkedHashMap<>();
		if (refreshTokenRequest == null || refreshTokenRequest.getRefreshToken() == null || refreshTokenRequest.getRefreshToken().isBlank()) {
			map.put("systemStatusResponse", "Refresh token is required");
			return map;
		}

		Map<String, String> claims;
		try {
			claims = tokenService.verifyToken(refreshTokenRequest.getRefreshToken(), "refresh");
		} catch (IllegalArgumentException exception) {
			map.put("systemStatusResponse", "Invalid refresh token");
			return map;
		}

		String tokenHash = hashToken(refreshTokenRequest.getRefreshToken());
		Optional<RefreshToken> existingRefreshToken = refreshTokenRepository.findByTokenHashAndActiveTrue(tokenHash);
		if (existingRefreshToken.isEmpty() || existingRefreshToken.get().getExpiresAt().isBefore(LocalDateTime.now())) {
			map.put("systemStatusResponse", "Refresh token is expired or revoked");
			return map;
		}

		RefreshToken refreshToken = existingRefreshToken.get();
		UserData userData = refreshToken.getUserData();
		if (!userData.getId().toString().equals(claims.get("sub")) || !userData.getTenantId().toString().equals(claims.get("tenantId"))) {
			map.put("systemStatusResponse", "Refresh token is invalid for user");
			return map;
		}

		refreshToken.setActive(false);
		refreshToken.setRotatedAt(LocalDateTime.now());
		refreshTokenRepository.save(refreshToken);

		String accessToken = tokenService.createAccessToken(userData);
		String newRefreshToken = tokenService.createRefreshToken(userData);
		DeviceSession deviceSession = refreshToken.getDeviceSession() == null ? trackDeviceSession(userData, httpServletRequest) : refreshToken.getDeviceSession();
		persistRefreshToken(userData, deviceSession, newRefreshToken);

		UserLoginResponse userLoginResponse = new UserLoginResponse();
		userLoginResponse.setBasicUserInformation(utilService.getBasicUserInformation(userData));
		userLoginResponse.setLastLogin(LocalDateTime.now());
		userLoginResponse.setAccessToken(accessToken);
		userLoginResponse.setRefreshToken(newRefreshToken);
		userLoginResponse.setAccessTokenExpiresInSeconds(tokenService.getAccessTokenTtlSeconds());
		userLoginResponse.setMessage("Token refreshed successfully");
		map.put("UserLoginResponse", userLoginResponse);
		auditEventService.log(userData.getTenantId(), userData.getId(), "REFRESH_TOKEN_ROTATED", httpServletRequest);
		return map;
	}

	private boolean logoutUser(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) {
		
		authentication = SecurityContextHolder.getContext().getAuthentication();
		
		if (!(authentication instanceof AnonymousAuthenticationToken)) {

			new SecurityContextLogoutHandler().logout(httpServletRequest, httpServletResponse, authentication);			
			
			return true;

		} 	
		
		return false;
		
	}

	private DeviceSession trackDeviceSession(UserData userData, HttpServletRequest request) {
		LocalDateTime presentDateTime = LocalDateTime.now();
		DeviceSession deviceSession = new DeviceSession();
		deviceSession.setTenantId(userData.getTenantId());
		deviceSession.setUserData(userData);
		deviceSession.setSessionId(request.getSession().getId());
		deviceSession.setIpAddress(getClientIpAddress(request));
		deviceSession.setDevice(request.getHeader("User-Agent"));
		deviceSession.setLastSeenAt(presentDateTime);
		deviceSession.setExpiresAt(presentDateTime.plusSeconds(tokenService.getRefreshTokenTtlSeconds()));
		deviceSession.setActive(true);
		return deviceSessionRepository.save(deviceSession);
	}

	private void persistRefreshToken(UserData userData, DeviceSession deviceSession, String token) {
		revokeActiveRefreshTokens(userData);
		RefreshToken refreshToken = new RefreshToken();
		refreshToken.setTenantId(userData.getTenantId());
		refreshToken.setUserData(userData);
		refreshToken.setDeviceSession(deviceSession);
		refreshToken.setTokenHash(hashToken(token));
		refreshToken.setExpiresAt(LocalDateTime.now().plusSeconds(tokenService.getRefreshTokenTtlSeconds()));
		refreshToken.setActive(true);
		refreshTokenRepository.save(refreshToken);
	}

	private void revokeActiveRefreshTokens(UserData userData) {
		List<RefreshToken> activeTokens = refreshTokenRepository.findByTenantIdAndUserDataAndActiveTrue(userData.getTenantId(), userData);
		LocalDateTime presentDateTime = LocalDateTime.now();
		activeTokens.forEach((token) -> {
			token.setActive(false);
			token.setRevokedAt(presentDateTime);
		});
		refreshTokenRepository.saveAll(activeTokens);
	}

	private void revokeRefreshToken(UserLogoutRequest userLogoutRequest, HttpServletRequest request) {
		if (userLogoutRequest != null && userLogoutRequest.getRefreshToken() != null && !userLogoutRequest.getRefreshToken().isBlank()) {
			refreshTokenRepository.findByTokenHashAndActiveTrue(hashToken(userLogoutRequest.getRefreshToken())).ifPresent((refreshToken) -> {
				refreshToken.setActive(false);
				refreshToken.setRevokedAt(LocalDateTime.now());
				refreshTokenRepository.save(refreshToken);
				auditEventService.log(refreshToken.getTenantId(), refreshToken.getUserData().getId(), "REFRESH_TOKEN_REVOKED", request);
			});
			return;
		}
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null && authentication.getName() != null) {
			UserData userData = userDataDAO.findUserDataByEmail(authentication.getName());
			if (userData != null) {
				revokeActiveRefreshTokens(userData);
				auditEventService.log(userData.getTenantId(), userData.getId(), "USER_LOGOUT", request);
			}
		}
	}

	private String hashToken(String token) {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			return Base64.getUrlEncoder().withoutPadding().encodeToString(digest.digest(token.getBytes(StandardCharsets.UTF_8)));
		} catch (Exception exception) {
			throw new IllegalStateException("Unable to hash token", exception);
		}
	}

	private String getClientIpAddress(HttpServletRequest request) {
		String forwardedFor = request.getHeader("X-Forwarded-For");
		if (forwardedFor != null && !forwardedFor.isBlank()) {
			return forwardedFor.split(",")[0].trim();
		}
		return request.getRemoteAddr();
	}

}
