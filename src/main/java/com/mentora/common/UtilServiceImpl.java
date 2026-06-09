package com.mentora.common;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.mentora.user.StaffRegistrationResponse;
import com.mentora.user.SuperAdmin;
import com.mentora.user.SuperAdminRegistrationResponse;
import com.mentora.user.BasicUserInformation;
import com.mentora.user.DepartmentAdmin;
import com.mentora.user.DepartmentAdminRegistrationResponse;
import com.mentora.user.Instructor;
import com.mentora.user.InstructorRegistrationResponse;
import com.mentora.user.OrganizationAdmin;
import com.mentora.user.OrganizationAdminRegistrationResponse;
import com.mentora.user.Staff;
import com.mentora.user.UserData;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class UtilServiceImpl implements UtilService {
	
	@Override
	public int pageSizeLimiter(int pageSize) {
		
		if (pageSize > 50) {
			
			return 50;
			
		} else if (pageSize < 1) {
			
			pageSize = 1;
		}
		
		return pageSize;
		
	}
	
	
	@SuppressWarnings("rawtypes")
	@Override
	public ResponseEntity getResponse(Map<String, Object> response, HttpStatus httpStatus) {
		return ResponseEntity.status(httpStatus).body(response);		
	}	

	

	@Override
	public StaffRegistrationResponse getBasicStaffInformation(UserData userData, Staff staff) {
		
		 if(userData != null) {
			
			return StaffRegistrationResponse.builder()
					.email(userData.getEmail())
					.firstName(staff.getFirstName())
					.lastName(staff.getLastName())
					.userRole(userData.getUserRole())
					.build();
					
		} else { 
			
			return null;
			
			}
		 
	} 

	@Override
	public SuperAdminRegistrationResponse getBasicSuperAdminInformation(UserData userData, SuperAdmin superAdmin) {
		
		 if(userData != null) {
			
			return SuperAdminRegistrationResponse.builder()
					.email(userData.getEmail())
					.firstName(superAdmin.getFirstName())
					.lastName(superAdmin.getLastName())
					.userRole(userData.getUserRole())
					.build();
					
		} else { 
			
			return null;
			
			}
	
	}
	
	@Override
	public OrganizationAdminRegistrationResponse getBasicOrganizationAdminInformation(UserData userData, OrganizationAdmin organizationAdmin) {
		
		 if(userData != null) {
			
			return OrganizationAdminRegistrationResponse.builder()
					.email(userData.getEmail())
					.firstName(organizationAdmin.getFirstName())
					.lastName(organizationAdmin.getLastName())
					.userRole(userData.getUserRole())
					.build();
					
		} else { 
			
			return null;
			
			}
	
	}
	
	@Override
	public DepartmentAdminRegistrationResponse getBasicDepartmentAdminInformation(UserData userData, DepartmentAdmin departmentAdmin) {
		
		 if(userData != null) {
			
			return DepartmentAdminRegistrationResponse.builder()
					.email(userData.getEmail())
					.firstName(departmentAdmin.getFirstName())
					.lastName(departmentAdmin.getLastName())
					.userRole(userData.getUserRole())
					.build();
					
		} else { 
			
			return null;
			
			}
	
	}
	
	@Override
	public InstructorRegistrationResponse getBasicInstructorInformation(UserData userData, Instructor instructor) {
		
		 if(userData != null) {
			
			return InstructorRegistrationResponse.builder()
					.email(userData.getEmail())
					.firstName(instructor.getFirstName())
					.lastName(instructor.getLastName())
					.userRole(userData.getUserRole())
					.build();
					
		} else { 
			
			return null;
			
			}
	
	}
	
	@Override
	public BasicUserInformation getBasicUserInformation(UserData userData) {
		
		 if(userData != null) {
			
			return BasicUserInformation.builder()
					.email(userData.getEmail())
					.userRole(userData.getUserRole())
					.build();
					
		} else { 
			
			return null;
			
			}
	
	}
}

	






