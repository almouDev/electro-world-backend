package com.almou.dao;

import com.almou.metier.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RestResource;

@RestResource(path = "users")
public interface UserRepository extends JpaRepository<AppUser,Long> {
	AppUser findByEmail(String name);
}
