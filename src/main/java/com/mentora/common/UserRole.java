package com.mentora.common;

import java.util.ArrayList;
import java.util.List;



public enum UserRole {
	
	SUPER_ADMIN,
	ORGANIZATION_ADMIN,
	DEPARTMENT_ADMIN,
	INSTRUCTOR,
	STAFF;
	
	
	private String userRole; 

    public String getUserRole() {
        return name();
    }
	   
    public static List<UserRole> getAllUserRoleEnumByString(String userRole) {
    	
        List<UserRole> roles = new ArrayList<>();
        
        if (userRole != null && !userRole.isEmpty()) {
        	

            String[] roleArray = userRole.split(","); 
            
        for (String s : roleArray) {
        	
        	try {
        		
        		
        		roles.add(UserRole.valueOf(s.trim().toUpperCase()));
        		
        	} catch (IllegalArgumentException e) {
        		// Ignore unknown role values from comma-delimited input.
        	}
        	
            }
        }
        
        return roles;
    }

}
