package com.theproject.x.services;

import java.net.URI;
import java.util.Calendar;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.theproject.x.entities.Sessions;
import com.theproject.x.entities.UserDevice;
import com.theproject.x.entities.Users;
import com.theproject.x.entities.VerifToken;
import com.theproject.x.models.DeviceInfoModel;
import com.theproject.x.models.keycloak.KeycloakAccessToken;
import com.theproject.x.repositories.SessionsRepository;
import com.theproject.x.repositories.VerifTokenrRepository;
import com.theproject.x.response.RestAccessTokenResponse;
import com.theproject.x.response.RestBaseResponse;
import com.theproject.x.services.api.GatewayApiException;

@Service("loginService")
@PropertySource({ "classpath:application.properties" })
public class LoginService {

	@Autowired
	private Environment env;

	@Autowired
	private KeycloakService keycloakService;
	
	@Autowired
	private UserProfileApiService userProfileApiService;
	
	@Autowired
	private VerifTokenrRepository verifTokenRepo;
	
	@Autowired
	private UserService userService;

	@Autowired
	private SessionsRepository sessionRepo;
	
    @Autowired 
    private SecurityService securityService;

	public static final String TOKEN_INVALID = "invalidToken";
	public static final String TOKEN_EXPIRED = "expired";
	public static final String TOKEN_VALID = "valid";

	
	public RestAccessTokenResponse keycloakUserAccessToken(String username, String password) throws Exception {		
	    RestTemplate restTemplate = new RestTemplate();	    
	    URI uriKeycloak = URI.create(env.getProperty("base.url") + env.getProperty("base.url.keycloak.access.token"));
	    RestAccessTokenResponse response = new RestAccessTokenResponse();		
	    KeycloakAccessToken accessToken = new KeycloakAccessToken();
	    
	    HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.add("Content-Type", "application/x-www-form-urlencoded");
        
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
		map.add("username", username);
		map.add("password", password);
		map.add("client_id", env.getProperty("user.client"));
		map.add("grant_type", "password");
		map.add("client_secret", env.getProperty("admin.user.keycloak.client.secret"));
		ResponseEntity<KeycloakAccessToken> tokenResponse = null;
		
		try {
			  HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, headers);
			  tokenResponse = restTemplate.exchange(uriKeycloak, HttpMethod.POST, entity, KeycloakAccessToken.class);
		      response.setAccessToken(tokenResponse.getBody());
		      response.setSuccess(true);

			  return response;
	      }
	    	catch (final HttpClientErrorException e) {
	    		response.setSuccess(false);
	    		response.setMessage("Unauthorized. Wrong Username or Password");
	    		return response;
	    	}
	}
	
	public RestBaseResponse<String> keycloakUserLogout(String userId) throws JsonMappingException, JsonProcessingException {		
	    RestTemplate restTemplate = new RestTemplate();	    
	    String adminAccessToken = KeycloakService.adminAccessToken;
	    
		URI uriKeycloak = URI.create(env.getProperty("base.url") + env.getProperty("base.url.keycloak.update.user") + "/" + userId + "/logout");
		RestBaseResponse<String> response = new RestBaseResponse<String>();		
		HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
	    headers.set("Authorization", "Bearer " + adminAccessToken);	      
        headers.add("Content-Type", "application/x-www-form-urlencoded");
        
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
		map.add("id", userId);
		map.add("realm", env.getProperty("keycloak.realm"));
		
		try {
			  HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, headers);
			  restTemplate.exchange(uriKeycloak, HttpMethod.POST, entity, KeycloakAccessToken.class);
		      response.setSuccess(true);
		      return response;

	      }
	    	catch (final HttpClientErrorException e) {
	    		response.setSuccess(false);
	    		response.setMessage("Something went wrong. Please try again.");
    
			    return response;
	    	}
	}
	
	public void saveSession(DeviceInfoModel deviceInfo) {
		ObjectMapper objectMapper = new ObjectMapper();
		UserDevice userDevice = objectMapper.convertValue(deviceInfo, new TypeReference<UserDevice>() {});
		
		String userId = securityService.getAuthorizedUserId();
		Users dbUser = userService.getUserByKcId(userId);
		
		Sessions session = new Sessions();
		session.setDeviceInfoId(userDevice);
		Calendar today = Calendar.getInstance();
        today.set(Calendar.HOUR_OF_DAY, 0);
        
        session.setTimestamp(today.getTime());
        session.setUserId(dbUser.getUserId());
        session.setOpen(1);
        
		sessionRepo.save(session);
		
	}
	
	
	public void createRequestForChangePassword(String parameter) throws Throwable {
		Users userExists = userService.userExistsParam(parameter);
		
		if(userExists != null) {
			final String token = UUID.randomUUID().toString();
			createResetPasswordToken(userExists.getUsername(), token);
		}
		
	}
	
	public void createResetPasswordToken(String username, String token) {
		Calendar today = Calendar.getInstance();
        today.set(Calendar.HOUR_OF_DAY, 0);
        
		VerifToken verifToken = new VerifToken(token, username);
		verifToken.setTimestamp(today.getTime());
		
		verifTokenRepo.save(verifToken);
	}
	
	
	public String validatePasswordResetToken(String token, String username) throws GatewayApiException {
		
	
		VerifToken passToken = verifTokenRepo.findByToken(token);
		
		if (passToken == null || !username.equals(passToken.getUsername())) {
			return TOKEN_INVALID;
		}

		final Calendar cal = Calendar.getInstance();
		if ((passToken.getExpiredAt().getTime() - cal.getTime().getTime()) <= 0) {
			return TOKEN_EXPIRED;
		}

		return TOKEN_VALID;
	}
	
	
}
