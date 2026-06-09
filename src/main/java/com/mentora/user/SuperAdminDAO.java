package com.mentora.user;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SuperAdminDAO extends JpaRepository <SuperAdmin, UUID> {
	
	@Query("SELECT s FROM SuperAdmin s WHERE s.userData = :userData")
	SuperAdmin findSuperAdminByUserData(@Param("userData")UserData userData);

}
