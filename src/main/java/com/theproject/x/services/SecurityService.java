package com.theproject.x.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.keycloak.KeycloakPrincipal;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 * @author r.kiloudis
 *
 */
@Service
public class SecurityService {
	
    @Autowired 
    SecurityService securityService;
	
    public String getAuthorizedUserId() {
    	KeycloakAuthenticationToken authentication = (KeycloakAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
    	
        KeycloakPrincipal user = (KeycloakPrincipal) authentication.getPrincipal();
        return user.getName();
    }

    public String getAuthorizedUsername() {
    	KeycloakAuthenticationToken authentication = (KeycloakAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        KeycloakPrincipal user = (KeycloakPrincipal) authentication.getPrincipal();
                
        return user.getKeycloakSecurityContext().getToken().getPreferredUsername();
    }

    public List<String> getAuthorizedRoles() {
        List<String> roles = new ArrayList<>();
        
    	KeycloakAuthenticationToken authentication = (KeycloakAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
            
        Collection<String> grantedAuthorities = authentication.getAccount().getRoles();
        for (String grantedAuthority : grantedAuthorities) {
            roles.add(grantedAuthority);
        }
       return roles;
    }
    
    
    void revokeTokens(String username) {
    	
    }
    
}
