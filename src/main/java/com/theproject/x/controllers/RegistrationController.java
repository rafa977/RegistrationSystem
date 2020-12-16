package com.theproject.x.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.theproject.x.models.User.UserModel;
import com.theproject.x.response.RestBaseResponse;
import com.theproject.x.services.RegistrationService;

@RestController
@RequestMapping("RegistrationController")
public class RegistrationController {

	@Autowired
	private RegistrationService registrationService;

	@RequestMapping(value = "/register", method = RequestMethod.POST)
	@ResponseBody
	public RestBaseResponse<String> register(@RequestBody UserModel user,
			final HttpServletRequest request) throws Throwable {
		
		RestBaseResponse<String> response = new RestBaseResponse<String>();
		
		response = registrationService.keycloakRegistration(user);
		
		//response.setSuccess(true);
		
		return response;
	}

	@RequestMapping(value = "/usernameValid", method = RequestMethod.GET)
	@ResponseBody
	public RestBaseResponse<String> usernameValid(final HttpServletRequest request, @RequestParam("parameter") String parameter) throws Throwable{
		
		RestBaseResponse<String> response = new RestBaseResponse<>();
		boolean valid = true;
		
		valid = registrationService.usernameValidation(parameter);
		response.setSuccess(valid);
		
		return response;
	}
}
