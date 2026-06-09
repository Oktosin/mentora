package com.mentora.security;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import com.mentora.common.UserAuthority;
import com.mentora.common.UserRole;
import com.mentora.user.UserData;
import com.mentora.user.UserDataDAO;

import lombok.RequiredArgsConstructor;


/**
 * 
 * UNDERSTANDING ROLES AND AUTHORITIES IN SPRING SECURITY
 *------------------------------------------------------------------------------------------------------------------------------ 
 *IN SPRING SECURITY, ROLES AND AUTHORITIES REPRESENT THE PERMISSIONS OR PRIVILEGES THAT CAN BE GRANTED TO A USER. 
 *ALTHOUGH BOTH SEEM THE SAME, HOWEVER, THERE IS A SUBTLE DIFFERENCE BETWEEN THE TWO. READ MORE DETAILS AFTER THE MENTHOD BELOW.
 *------------------------------------------------------------------------------------------------------------------------------
 *
 */

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
	
		private final UserDataDAO userDataDAO;
	
	    @Override
	    public UserDetails loadUserByUsername(String identifier) throws UsernameNotFoundException {
	    	
	    	UserData userData = userDataDAO.findUserDataByEmail(identifier); 
	    	if (userData == null) {
	    		throw new UsernameNotFoundException("User not found");
	    	}
	               
	        List<UserRole> userRoleList = UserRole.getAllUserRoleEnumByString(userData.getUserRole().getUserRole());
	        Set<UserAuthority> userAuthoritySet = userData.getAuthorities();
	        
	        //CONVERT ROLES INTO AUTHORITIES BECAUSE SPRING DOES NOT ALLOW THE USE OF .roles() AND .authorities() AT THE SAME TIME
	        //MANUALLY COMBINE ROLE AND AUTHORITY INTO A SINGLE GRANTED AUTHORITY LIST AND THEN ADD THE PREFIX ROLE_ TO ROLE TO NOTIFY SPRING OF THE DIFFERENCE
	        
	        List<GrantedAuthority> authorities = Stream.<GrantedAuthority>concat(userRoleList.stream().map(role -> new SimpleGrantedAuthority("ROLE_" + role.name())),
	                        													 userAuthoritySet.stream().map(auth -> new SimpleGrantedAuthority(auth.name()))).toList(); 
	        
	        String password = userData.getPassword();
	        
	        return new User(identifier, password, authorities);
	        
	    }
	    
	    

}
