package com.mentora.user;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import com.mentora.common.BaseEntity;
import com.mentora.common.UserAuthority;
import com.mentora.common.UserRole;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "userInformation")
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserData extends BaseEntity {
	
	private static final long serialVersionUID = 1L;
	
	@Column(name = "tenantId", nullable = false)
	private UUID tenantId;
	
	@Column(name = "email", unique = true)
	private String email;
	
	@Column(name = "password")
	private String password;
	
	@ElementCollection(targetClass = UserAuthority.class, fetch = FetchType.EAGER)
	@CollectionTable(name = "userAuthorities", joinColumns = @JoinColumn(name = "userId"))
	@Column(name = "authority")
	@Enumerated(EnumType.STRING)
	@Builder.Default
	private Set<UserAuthority> authorities = new HashSet<>();	
	
	@Enumerated(EnumType.STRING)
	@Column(name = "userRole", nullable = false)
	private UserRole userRole;

}
