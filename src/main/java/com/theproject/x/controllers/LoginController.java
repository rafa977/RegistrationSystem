package com.theproject.x.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.theproject.x.entities.Users;
import com.theproject.x.models.DeviceInfoModel;
import com.theproject.x.models.Password;
import com.theproject.x.models.User.UserModel;
import com.theproject.x.models.keycloak.KeycloakAccessToken;
import com.theproject.x.response.RestAccessTokenResponse;
import com.theproject.x.response.RestBaseResponse;
import com.theproject.x.services.KeycloakService;
import com.theproject.x.services.LoginService;
import com.theproject.x.services.SecurityService;
import com.theproject.x.services.UserService;

@RestController
@RequestMapping("LoginController")
public class LoginController {

	@Autowired
	private LoginService loginService;
	
	@Autowired
	private SecurityService securityService;
	
	@Autowired
	private KeycloakService keycloakService;
	
	@Autowired
	private UserService userService;
	
	public static final String TOKEN_INVALID = "invalidToken";
	public static final String TOKEN_EXPIRED = "expired";
	public static final String TOKEN_VALID = "valid";

	
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	@ResponseBody
	public RestAccessTokenResponse login(@RequestBody UserModel user,
			final HttpServletRequest request) throws Exception {
		RestAccessTokenResponse response = new RestAccessTokenResponse();
		
		response = loginService.keycloakUserAccessToken(user.getUsername(), user.getPassword());
		
		return response;
	}
	
	
	@RequestMapping(value = "/deviceInfo", method = RequestMethod.POST)
	@ResponseBody
	public RestAccessTokenResponse deviceInfo(@RequestBody DeviceInfoModel deviceInfo,
			final HttpServletRequest request) throws Exception {
		RestAccessTokenResponse response = new RestAccessTokenResponse();
		
		loginService.saveSession(deviceInfo);
		
		
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
	
	@RequestMapping(value = "/forgotPassword", method = RequestMethod.GET)
	@ResponseBody
	public RestBaseResponse<String> forgotPassword(final HttpServletRequest request, @RequestParam("parameter") String parameter) throws Throwable{
		
		RestBaseResponse<String> response = new RestBaseResponse<>();
		loginService.createRequestForChangePassword(parameter);
		
		response.setMessage("An email was sent to your email account.");
		return response;
	}
	
	@RequestMapping(value = "/resetPassword", method = RequestMethod.POST)
	@ResponseBody
	public RestBaseResponse<String> resetPassword(final HttpServletRequest request, @RequestBody final UserModel user) throws Throwable{
		
		RestBaseResponse<String> response = new RestBaseResponse<>();
		String tokenValidation = loginService.validatePasswordResetToken(user.getToken(), user.getUsername());
		
		if (tokenValidation.equals(TOKEN_VALID)) {
			Users dbUser = userService.getUser(user.getUsername());
			
        	Password pass = new Password();
            pass.setNewPassword(user.getPassword());
            pass.setMatchNewPassword(user.getMatchPassword());

            response = keycloakService.keycloakUserPassReset(dbUser.getkcUserId(), pass);
                        
        } else {
            response.setMessage(tokenValidation);
            response.setSuccess(false);
        }
        return response;
    }
	
}
