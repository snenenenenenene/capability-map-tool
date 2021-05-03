package com.bavostepbros.leap.configuration;

import java.util.Arrays;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

@Configuration
public class CorsConfig {
	
	/*
	 * @Value("${allowedOrigin}") private String allowedOrigin;
	 * 
	 * @Bean public CorsWebFilter corsWebFilter() { final CorsConfiguration
	 * corsConfig = new CorsConfiguration();
	 * corsConfig.setAllowedOrigins(Collections.singletonList(allowedOrigin));
	 * corsConfig.setMaxAge(3600L);
	 * corsConfig.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
	 * corsConfig.addAllowedHeader("*"); corsConfig.setAllowCredentials(true);
	 * 
	 * final UrlBasedCorsConfigurationSource source = new
	 * UrlBasedCorsConfigurationSource(); source.registerCorsConfiguration("/**",
	 * corsConfig);
	 * 
	 * return new CorsWebFilter(source); }
	 */
}
