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
import com.theproject.x.models.User.UserModel;
import com.theproject.x.models.keycloak.KeycloakAccessToken;
import com.theproject.x.response.RestAccessTokenResponse;
import com.theproject.x.response.RestBaseResponse;
import com.theproject.x.services.LoginService;
import com.theproject.x.services.SecurityService;

@RestController
@RequestMapping("LoginController")
public class LoginController {

	@Autowired
	private LoginService loginService;
	
	@Autowired
	private SecurityService securityService;
	
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	@ResponseBody
	public RestAccessTokenResponse login(@RequestBody UserModel user,
			final HttpServletRequest request) throws Exception {
		RestAccessTokenResponse response = new RestAccessTokenResponse();
		
		response = loginService.keycloakUserAccessToken(user.getUsername(), user.getPassword());
		
		return response;
	}
	
	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	@ResponseBody
	public RestBaseResponse<String> logout(final HttpServletRequest request) throws JsonMappingException, JsonProcessingException{
		
		RestBaseResponse<String> response = new RestBaseResponse<>();
		
		String userId = securityService.getAuthorizedUserId();
		response = loginService.keycloakUserLogout(userId);
		
		return response;
	}
	
	@GetMapping(path = "/")
	public String index() {
	    return "external";
	}
}
