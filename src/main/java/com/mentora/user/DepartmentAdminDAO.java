package com.mentora.user;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentAdminDAO extends JpaRepository <DepartmentAdmin, UUID> {
	
	@Query("SELECT d FROM DepartmentAdmin d WHERE d.userData = :userData")
	DepartmentAdmin findDepartmentByUserData(@Param("userData")UserData userData);

}
