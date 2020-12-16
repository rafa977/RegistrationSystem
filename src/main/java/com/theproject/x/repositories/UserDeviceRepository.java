package com.theproject.x.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.theproject.x.entities.UserDevice;

public interface UserDeviceRepository extends JpaRepository<UserDevice, Long>{

	
}
