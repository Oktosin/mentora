package com.mentora.common;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

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


public interface UtilService {

	int pageSizeLimiter(int pageSize);

	@SuppressWarnings("rawtypes")
	ResponseEntity getResponse(Map<String, Object> map, HttpStatus httpStatus);

	StaffRegistrationResponse getBasicStaffInformation(UserData userData, Staff staff);

	SuperAdminRegistrationResponse getBasicSuperAdminInformation(UserData userData, SuperAdmin superAdmin);

	OrganizationAdminRegistrationResponse getBasicOrganizationAdminInformation(UserData userData, OrganizationAdmin organizationAdmin);

	DepartmentAdminRegistrationResponse getBasicDepartmentAdminInformation(UserData userData, DepartmentAdmin departmentAdmin);

	InstructorRegistrationResponse getBasicInstructorInformation(UserData userData, Instructor instructor);

	BasicUserInformation getBasicUserInformation(UserData userData);



	






}
