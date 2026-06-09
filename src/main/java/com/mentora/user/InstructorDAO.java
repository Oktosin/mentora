package com.mentora.user;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface InstructorDAO extends JpaRepository<Instructor, UUID> {

	@Query("SELECT i FROM Instructor i WHERE i.userData = :userData")
	Instructor findInstructorByUserData(@Param("userData")UserData userData);
}
