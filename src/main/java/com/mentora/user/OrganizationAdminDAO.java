package com.mentora.user;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrganizationAdminDAO extends JpaRepository <OrganizationAdmin, UUID> {
	
	@Query("SELECT o FROM OrganizationAdmin o WHERE o.userData = :userData")
	OrganizationAdmin findOrganizationAdminByUserData(@Param("userData")UserData userData);

}
