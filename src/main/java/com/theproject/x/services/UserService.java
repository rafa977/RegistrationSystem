package com.theproject.x.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import com.theproject.x.entities.Users;
import com.theproject.x.models.User.UserModel;
import com.theproject.x.repositories.UsersRepository;

@Service("userService")
@PropertySource({ "classpath:application.properties" })
public class UserService {
	
	@Autowired
	private UsersRepository usersRepository;

	
	public Users getUserByKcId(String userId) {
		try {
			Users user = usersRepository.findByKcUserId(userId);
			return user;
		}catch(Exception e) {
			return null;
		}
		
	}

	public Users getUser(String username) {
		
		try {
			Users user = usersRepository.findByUsername(username);
			return user;
		}catch(Exception e) {
			return null;
		}
		
	}
	
	public boolean usernameValid(String username) {
		Users user = new Users();
		
		try {
			user = usersRepository.findByUsername(username);
			if(user != null) {
				return false;
			}
			
			return true;
			
		}catch(Exception e) {
			return false;
		}
	}
	
	public boolean userExists(UserModel user) {
		Users userEntity = new Users();
		
		userEntity = usersRepository.findByEmail(user.getEmail());
		
		if(userEntity==null) {
			return false;
		}else {
			return true;
		}
	}
	
	public Users userExistsParam(String parameter) {
		Users user = new Users();
		
		try {
			user = usersRepository.findByUsername(parameter);
			if(user != null) {
				return user;
			}
			user = usersRepository.findByEmail(parameter);
			if(user != null) {
				return user;
			}
			
			return null;
			
		}catch(Exception e) {
			return null;
		}
	}
	
}
