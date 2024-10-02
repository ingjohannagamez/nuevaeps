package com.ingeneo.logistica.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ingeneo.logistica.model.Role;

public interface RoleRepository  extends JpaRepository<Role, Long> {

	Role findByName(String name);
}