package com.almou.dao;

import com.almou.metier.AppRole;
import org.springframework.data.jpa.repository.JpaRepository;
public interface RoleRepository extends JpaRepository<AppRole,Long> {
	AppRole findByName(String roleName);
}