package com.mentora.user;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StaffDAO extends JpaRepository <Staff, UUID> {
	
	@Query("SELECT s FROM Staff s WHERE s.userData = :userData")
	Staff findStaffByUserData(@Param("userData")UserData userData);

}
