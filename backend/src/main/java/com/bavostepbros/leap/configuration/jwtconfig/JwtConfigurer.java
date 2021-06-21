package com.bavostepbros.leap.configuration.jwtconfig;

import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;

public class JwtConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

	@SuppressWarnings("unused")
	private JwtUtility jwtUtility;

	public JwtConfigurer(JwtUtility jwtUtility) {
		this.jwtUtility = jwtUtility;
	}

}