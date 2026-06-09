package com.mentora.common;

import java.util.ArrayList;
import java.util.List;



public enum UserAuthority {
	
	SUPER_ADMIN,
	ORGANIZATION_ADMIN,
	DEPARTMENT_ADMIN,
	INSTRUCTOR,
	STAFF,
	MANAGE_USERS,
	MANAGE_COURSES,
	MANAGE_ASSESSMENTS,
	VIEW_ANALYTICS,
	SUBMIT_FEEDBACK;

	private String userAuthority; 

    public String getUserAuthority() {
        return name();
    }
	   
    public static List<UserAuthority> getAllUserAuthorityEnumByString(String userAuthority) {
    	
        List<UserAuthority> roles = new ArrayList<>();
        
        if (userAuthority != null && !userAuthority.isEmpty()) {
        	
            String[] roleArray = userAuthority.split(","); 
            
        for (String s : roleArray) {
        	
        	try {
        		
        		roles.add(UserAuthority.valueOf(s.trim().toUpperCase())); 
        		
        	} catch (IllegalArgumentException e) {
        		// Ignore unknown authority values from comma-delimited input.
        	}
        	
            }
        }
        
        return roles;
    }	

}
	

