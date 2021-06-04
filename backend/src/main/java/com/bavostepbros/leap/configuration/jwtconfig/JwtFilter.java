package com.bavostepbros.leap.configuration.jwtconfig;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtFilter extends OncePerRequestFilter {

	private static Logger log = LoggerFactory.getLogger(JwtFilter.class);
	private JwtUtility jwtUtility;
	public JwtFilter(JwtUtility jwtUtility) {
		this.jwtUtility = jwtUtility;
	}

	@Override
	public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String authorizationHeader = request.getHeader("Authorization");
		if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
			String jwt = authorizationHeader.substring(7);
			String username = jwtUtility.extractUsername(jwt);
			if (jwtUtility.validateToken(jwt)) {
				Authentication authentication = jwtUtility.getAuthentication(username);
				if (authentication.isAuthenticated()) {
					SecurityContextHolder.getContext().setAuthentication(authentication);
				}
			}
		}
		filterChain.doFilter(request, response);
	}
}