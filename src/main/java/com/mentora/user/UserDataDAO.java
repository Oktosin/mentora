package com.mentora.user;


import java.util.Map;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface UserDataDAO extends JpaRepository <UserData, UUID> {
	
	@Query("SELECT u FROM UserData u WHERE u.email = :email")
	UserData findUserDataByEmail(@Param("email") String email);

	@Query("SELECT u FROM UserData u WHERE u.tenantId = :tenantId")
	Page<UserData> findByTenantId(UUID tenantId, Pageable pageable);

	Map<String, Object> findByTenantId(UUID tenantId);



}
