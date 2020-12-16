package com.theproject.x.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.theproject.x.entities.VerifToken;

public interface VerifTokenrRepository extends JpaRepository<VerifToken, String>{
	
	VerifToken findByToken(String token);
	
}
