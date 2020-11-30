package com.theproject.x.util;

public class Utils {

	public boolean passwordValidation(String password, String re_password) {
		if(password.equals(re_password)) {
			return true;
		}
		
		return false;
	}
	
}
