package com.theproject.x.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.theproject.x.entities.Users;


public interface UsersRepository extends JpaRepository<Users, Long>{
	
	Users findByUserId(Long userId);
	
	Users findByKcUserId(String keycloakUserId);
	
	Users findByEmail(String email);

	Users findByUsername(String username);
	
	void deleteByUserId(Long userId);
	
}
