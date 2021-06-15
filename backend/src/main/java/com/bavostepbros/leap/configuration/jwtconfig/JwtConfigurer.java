package com.bavostepbros.leap.configuration.jwtconfig;

import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class JwtConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

	private JwtUtility jwtUtility;

	public JwtConfigurer(JwtUtility jwtUtility) {
		this.jwtUtility = jwtUtility;
	}

	
	/** 
	 * @param http
	 */
	@Override
	public void configure(HttpSecurity http) {
		http.addFilterBefore(new JwtFilter(jwtUtility), UsernamePasswordAuthenticationFilter.class);
	}
}