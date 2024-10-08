package com.theproject.x.configuration;


import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver;
import org.keycloak.adapters.springsecurity.KeycloakSecurityComponents;
import org.keycloak.adapters.springsecurity.authentication.KeycloakAuthenticationProvider;
import org.keycloak.adapters.springsecurity.config.KeycloakWebSecurityConfigurerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;

@Configuration
@EnableWebSecurity
@ComponentScan(basePackageClasses = KeycloakSecurityComponents.class)
class SecurityConfig extends KeycloakWebSecurityConfigurerAdapter {
 
    @Autowired
    public void configureGlobal(
      AuthenticationManagerBuilder auth) throws Exception {
 
        KeycloakAuthenticationProvider keycloakAuthenticationProvider
          = keycloakAuthenticationProvider();
        keycloakAuthenticationProvider.setGrantedAuthoritiesMapper(
          new SimpleAuthorityMapper());
        auth.authenticationProvider(keycloakAuthenticationProvider);
    }
 
    @Bean
    public KeycloakSpringBootConfigResolver KeycloakConfigResolver() {
        return new KeycloakSpringBootConfigResolver();
    }
 
    @Bean
    @Override
    protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
        return new RegisterSessionAuthenticationStrategy(
          new SessionRegistryImpl());
    }
 
	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers(HttpMethod.OPTIONS, "/**");
		web.ignoring().antMatchers(HttpMethod.POST, "/LoginController/login");
//		web.ignoring().antMatchers(HttpMethod.POST, "/LoginController/deviceInfo");

		web.ignoring().antMatchers(HttpMethod.POST, "/RegistrationController/register");
		web.ignoring().antMatchers(HttpMethod.POST, "/LoginController/forgotPassword");	
		web.ignoring().antMatchers(HttpMethod.POST, "/LoginController/resetPassword");	
	}
	
	
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        super.configure(http);
			http.csrf().disable().authorizeRequests()
		.anyRequest().authenticated();
    }
}