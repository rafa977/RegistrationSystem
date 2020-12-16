package com.theproject.x.services;

import java.net.URI;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
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
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.theproject.x.models.Password;
import com.theproject.x.models.UpdatePasswordException;
import com.theproject.x.models.keycloak.KeycloakAccessToken;
import com.theproject.x.response.RestBaseResponse;

@Service("keycloakService")
@PropertySource({ "classpath:application.properties" })
public class KeycloakService {

	public static String adminAccessToken;
	
	@Autowired
	private Environment env;
	
	@Autowired
	RestTemplate restTemplate;
	
	private Keycloak keycloak;


//	@Autowired
	private UsersResource usersResource;

	private UserResource userResource;

	private UserRepresentation user;

	CredentialRepresentation credential;
	
	public UsersResource keycloakUserServiceInitiation() {
		keycloak = KeycloakBuilder.builder().serverUrl(env.getProperty("keycloak.auth-server-url"))
				.realm(env.getProperty("keycloak.realm")).username(env.getProperty("admin.user.keycloak.username"))
				.password(env.getProperty("admin.user.keycloak.password"))
				.clientId(env.getProperty("admin.user.keycloak.client_id"))
				.resteasyClient(new ResteasyClientBuilder().connectionPoolSize(10).build()).build();
		usersResource = keycloak.realm(env.getProperty("keycloak.realm")).users();

		return usersResource;

	}

	public UserRepresentation returnUserFromUsername(String username) {
		List<UserRepresentation> list = usersResource.search(username);
		UserRepresentation user = list.get(0);
		return user;
	}

	public void deleteKeycloakUser(String userId) {
		keycloak.realm("testrealm").users().get(userId).remove();	
	}
	
	public String keycloakAdminAccessToken() {		
	    RestTemplate restTemplate = new RestTemplate();
	    
		URI uriKeycloak = URI.create(env.getProperty("base.url") + env.getProperty("admin.base.url.keycloak.access.token"));
		
		HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.add("Content-Type", "application/x-www-form-urlencoded");
        
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
		map.add("username", env.getProperty("admin.user.keycloak.username"));
		map.add("password", env.getProperty("admin.user.keycloak.password"));
		map.add("client_id", env.getProperty("admin.user.keycloak.client_id"));
		map.add("grant_type", "password");
		map.add("credentials", "true");
		map.add("scope", "openid");
        
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, headers);
        
        ResponseEntity<KeycloakAccessToken> tokenResponse = restTemplate.exchange(uriKeycloak, HttpMethod.POST, entity,
        		KeycloakAccessToken.class);
        
        adminAccessToken = tokenResponse.getBody().getAccessToken();
        return adminAccessToken;
   	}

	public RestBaseResponse<String> keycloakHttpPost(HttpMethod method, boolean isEntity, String adminAccessToken, URI uriKeycloak, MultiValueMap<String, String> map){
		RestBaseResponse<String> response = new RestBaseResponse<String>();		
		ResponseEntity<KeycloakAccessToken> tokenResponse = null;

		HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);	      
        headers.add("Content-Type", "application/x-www-form-urlencoded");
        if(adminAccessToken != null) {
        	 headers.set("Authorization", "Bearer " + adminAccessToken);
        }
        
        try {
        	  if(isEntity) {
				  HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, headers);
				  tokenResponse = restTemplate.exchange(uriKeycloak, method, entity, KeycloakAccessToken.class);
				  
			      response.setData(tokenResponse.getBody().getAccessToken());
        	  }else {
        		  HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, headers);
        		  restTemplate.exchange(uriKeycloak, method, entity, String.class);
        	  }
        	  
			  response.setSuccess(true);
		      return response;

	      } catch (final HttpClientErrorException e) {
    		response.setSuccess(false);
    		return response;
    	}
	}
	
	public RestBaseResponse<String> keycloakUserPassReset(String id, Password password) throws JsonMappingException, JsonProcessingException{
		ObjectMapper objectMapper = new ObjectMapper();	
		String accessToken = keycloakAdminAccessToken();
		HashMap<String, String> userUpdateValues = new HashMap<String, String>();
		
		userUpdateValues.put("type","password");
		userUpdateValues.put("temporary","false");
		userUpdateValues.put("value", password.getMatchNewPassword());
		
		Gson valuesJson = new Gson();
		String json = valuesJson.toJson(userUpdateValues);
	
	    RestTemplate restTemplate = new RestTemplate();
	    HttpHeaders headers = new HttpHeaders();
	      headers.set("Content-Type", "application/json");
	      headers.set("Authorization", "Bearer " + accessToken);	      
	      headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON)); 
	      RestBaseResponse<String> response = new RestBaseResponse<String>();

	      String url = env.getProperty("base.url") + env.getProperty("base.url.keycloak.update.user") + "/" + id + "/reset-password";
	      try {
				HttpEntity<String> entity2 = new HttpEntity<String>(json,headers);	  
				restTemplate.exchange(url, HttpMethod.PUT, entity2, String.class);
				response.setMessage("Password Changed Successfully.");
				response.setSuccess(true);
				return response;

	      } catch (final HttpClientErrorException e) {
	    		
				UpdatePasswordException exc = objectMapper.readValue(e.getResponseBodyAsString(), UpdatePasswordException.class);
				
				response.setSuccess(false);
				response.setMessage(exc.getError_description());
				return response;
	    	}
	}
	
}
