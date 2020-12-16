package com.theproject.x.services;

import java.util.ArrayList;
import java.util.Arrays;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.theproject.x.models.User.UserModel;
import com.theproject.x.response.RestBaseResponse;

@Service("registrationService")
@PropertySource({ "classpath:application.properties" })
public class RegistrationService {

	@Autowired
	private Environment env;

	@Autowired
	private KeycloakService keycloakSerivce;
	
	@Autowired
	private UserProfileApiService userProfileApiService;
	
	Log logger = LogFactory.getLog(RegistrationService.class);

	public void deleteKeycloakUser(String userId) {
		keycloakSerivce.deleteKeycloakUser(userId);
	}
	
	public RestBaseResponse<String> keycloakRegistration(UserModel registrationUser) throws Throwable {
		RestBaseResponse<String> response = new RestBaseResponse<String>();
		RestBaseResponse<String> userResponse = new RestBaseResponse<String>();

		try {
			response = keycloakUserRegistration(registrationUser);
			if(response.isSuccess()) {
				UserRepresentation user = keycloakSerivce.returnUserFromUsername(registrationUser.getEmail());
				
				userResponse = userProfileApiService.saveToDB(user);
				if(!(userResponse.isSuccess())) {
					this.deleteKeycloakUser(user.getId());
					return userResponse;
				}
			}
			
			return response;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}
	
	
	public RestBaseResponse<String> keycloakUserRegistration(UserModel user) {
		//get admin access token
		String accessToken = keycloakSerivce.keycloakAdminAccessToken();
		
		RestBaseResponse<String> response = new RestBaseResponse<String>();
		
		UserRepresentation newUser = new UserRepresentation();
		CredentialRepresentation credentials = new CredentialRepresentation();
		credentials.setType(CredentialRepresentation.PASSWORD);
		credentials.setValue(user.getPassword());
		credentials.setTemporary(false);

		newUser.setUsername(user.getUsername());
		newUser.setEmail(user.getEmail());
		newUser.setFirstName(user.getFirstName());
		newUser.setLastName(user.getLastName());
		newUser.setEmailVerified(false);
		newUser.setEnabled(true);
		newUser.setCredentials(new ArrayList<>());
		newUser.getCredentials().add(credentials);		
		
		
	    RestTemplate restTemplate = new RestTemplate();
	    HttpHeaders headers = new HttpHeaders();
	      headers.set("Content-Type", "application/json");
	      headers.set("Authorization", "Bearer " + accessToken);	      
	      headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
	   
	   try {
		   
	      HttpEntity<UserRepresentation> entity2 = new HttpEntity<UserRepresentation>(newUser,headers);	  
	      ResponseEntity<String> responseEntity = restTemplate.exchange(env.getProperty("base.url") + env.getProperty("base.url.keycloak.register.user"), HttpMethod.POST, entity2, String.class);

  		  HttpStatus statusCode = responseEntity.getStatusCode();
	  		if(statusCode.value() != 204 && statusCode.value() != 201) {
				response.setSuccess(false);
				response.setData(responseEntity.getBody());
			}
	  		
	  		return response;
	  		
	   } catch(HttpClientErrorException e) {
	    	logger.debug(e.getMessage());
	    	response.setSuccess(false);
	    	response.setMessage(e.getStatusText());

	    	if(e.getRawStatusCode() == 409) {
   			response.setSuccess(false);
   			response.setMessage("This email is already in use.");
   		}
	    	return response;
	    }
	}

	public boolean usernameValidation(String parameter) throws Throwable {
		Boolean userExists = false;
		userExists = userProfileApiService.checkIfUserExists(parameter);
		
		return userExists;
	}
	
}
