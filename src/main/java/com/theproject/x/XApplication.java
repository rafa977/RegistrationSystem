package com.theproject.x;

import org.keycloak.admin.client.resource.UsersResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import com.theproject.x.services.KeycloakService;

@SpringBootApplication
public class XApplication {

	@Autowired
	private KeycloakService keycloakUserService;

	public static void main(String[] args) {
		SpringApplication.run(XApplication.class, args);
	}

	@Bean
	public RestTemplate restTemplate() {
	    return new RestTemplate();
	}
	
	@Bean
	public UsersResource userResource() {
		keycloakUserService.keycloakAdminAccessToken();
		
		return keycloakUserService.keycloakUserServiceInitiation();
	}
	
}
