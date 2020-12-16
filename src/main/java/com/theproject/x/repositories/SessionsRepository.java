package com.theproject.x.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.theproject.x.entities.Sessions;

@Repository
public interface SessionsRepository extends JpaRepository<Sessions, Long>{

	Optional<Sessions> findById(Long id);

}
