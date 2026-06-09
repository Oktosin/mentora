package com.mentora.security;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.mentora.common.UserAuthority;
import com.mentora.common.UserRole;
import com.mentora.common.TenantContext;
import com.mentora.user.UserData;
import com.mentora.user.UserDataDAO;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final TokenService tokenService;
	private final UserDataDAO userDataDAO;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String authorization = request.getHeader("Authorization");
		if (authorization != null && authorization.startsWith("Bearer ")
				&& SecurityContextHolder.getContext().getAuthentication() == null) {
			authenticate(authorization.substring("Bearer ".length()), request);
		}
		filterChain.doFilter(request, response);
	}

	private void authenticate(String token, HttpServletRequest request) {
		try {
			Map<String, String> claims = tokenService.verifyToken(token, "access");
			UUID userId = UUID.fromString(claims.get("sub"));
			UserData userData = userDataDAO.findById(userId).orElse(null);
			if (userData == null || !userData.getTenantId().toString().equals(claims.get("tenantId"))) {
				return;
			}
			request.setAttribute(TenantContext.AUTHENTICATED_TENANT_ATTRIBUTE, userData.getTenantId());
			List<GrantedAuthority> authorities = authorities(userData.getUserRole(), userData.getAuthorities());
			UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userData.getEmail(), null, authorities);
			SecurityContextHolder.getContext().setAuthentication(authentication);
		} catch (IllegalArgumentException exception) {
			SecurityContextHolder.clearContext();
		}
	}

	private List<GrantedAuthority> authorities(UserRole role, Set<UserAuthority> userAuthorities) {
		return Stream.<GrantedAuthority>concat(
				Stream.of(new SimpleGrantedAuthority("ROLE_" + role.name())),
				userAuthorities.stream().map(authority -> new SimpleGrantedAuthority(authority.name()))).toList();
	}
}
