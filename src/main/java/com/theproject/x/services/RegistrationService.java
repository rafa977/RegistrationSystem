package com.theproject.x.services;

import java.util.ArrayList;
import java.util.Arrays;

import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.theproject.x.models.User.RegistrationUser;
import com.theproject.x.services.api.GatewayApiException;

@Service("registrationService")
@PropertySource({ "classpath:application.properties" })
public class RegistrationService {

	@Autowired
	private Environment env;

	@Autowired
	private KeycloakService keycloakSerivce;
	
	public ResponseEntity<String> keycloakRegistration(RegistrationUser registrationUser) {
		ResponseEntity<String> response = null;
		try {
			response = keycloakUserRegistration(registrationUser);
			if (response.getStatusCode().value() != 201) {
				throw new GatewayApiException("Something went wrong. Please try again.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	
	public ResponseEntity<String> keycloakUserRegistration(RegistrationUser user) {
		//get admin access token
		String accessToken = keycloakSerivce.keycloakAdminAccessToken();
		
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
		
//		
//		SecRegVerifData userTempData = new SecRegVerifData();
//		userTempData.setSUsername(user.getUsername());
//		userTempData.setSFirstName(user.getFirstName());
//		userTempData.setSLastName(user.getLastName());
//		userTempData.setSCountry(user.getCountry());
//		userTempData.setSMobile(user.getMobile());
//		
//		secRegVerifDataRepository.save(userTempData);
		
	    RestTemplate restTemplate = new RestTemplate();
	    HttpHeaders headers = new HttpHeaders();
	      headers.set("Content-Type", "application/json");
	      headers.set("Authorization", "Bearer " + accessToken);	      
	      headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
	   
	      
	      HttpEntity<UserRepresentation> entity2 = new HttpEntity<UserRepresentation>(newUser,headers);	  
	      ResponseEntity<String> response = restTemplate.exchange(env.getProperty("base.url.keycloak.register.user"), HttpMethod.POST, entity2, String.class);
	      
	      return response;
	}

	
	
}
