package com.mentora.permission;

import java.util.UUID;

import com.mentora.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity(name = "permissions")
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class Permission extends BaseEntity {

	private static final long serialVersionUID = 1L;
	
	@Column(name = "tenantId", nullable = false)
	private UUID tenantId;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "resource")
	private String resource;
	
	@Column(name = "action")
	private String action;

}
