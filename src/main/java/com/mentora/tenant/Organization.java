package com.mentora.tenant;

import java.util.UUID;

import com.mentora.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity(name = "organizations")
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class Organization extends BaseEntity {
	
	private static final long serialVersionUID = 1L;
	
	@Column(name = "tenantId", nullable = false)
	private UUID tenantId;
	
	@Column(name = "name", nullable = false)
	private String name;
	
	@Column(name = "industry", nullable = false)
	private String industry;
	
	@Column(name = "email", nullable = false)
	private String email;

	private boolean active = true;

}
