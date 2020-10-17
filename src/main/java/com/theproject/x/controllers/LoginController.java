package com.theproject.x.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.theproject.x.models.User.RegistrationUser;
import com.theproject.x.response.RestBaseResponse;
import com.theproject.x.services.LoginService;

@RestController
@RequestMapping("LoginController")
public class LoginController {

	@Autowired
	private LoginService loginService;
	
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	@ResponseBody
	public RestBaseResponse<String> login(@RequestBody RegistrationUser user,
			final HttpServletRequest request) throws JsonMappingException, JsonProcessingException {
		RestBaseResponse<String> keycloakAccessTokenResponse = loginService.keycloakUserAccessToken(user.getUsername(), user.getPassword());
		
		return keycloakAccessTokenResponse;
	}
	
	@GetMapping(path = "/")
	public String index() {
	    return "external";
	}
}
