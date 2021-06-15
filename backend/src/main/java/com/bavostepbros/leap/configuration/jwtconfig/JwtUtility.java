package com.bavostepbros.leap.configuration.jwtconfig;

import java.io.Serializable;
import java.util.Base64;
import java.util.Date;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import com.bavostepbros.leap.domain.model.Role;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtUtility implements Serializable {

	private static final long serialVersionUID = 2569800841756370596L;

	@Autowired
	private UserDetailsService userDetailsService;

	// TODO put in aplication.properties
	private String SECRETKEY = "leap-groep-5";
	private final long VALIDITYTIME = 12 * 3600000; // 12 hours

	@PostConstruct
	protected void init() {
		SECRETKEY = Base64.getEncoder().encodeToString(SECRETKEY.getBytes());
	}


	
	/** 
	 * @param username
	 * @param role
	 * @return String
	 */
	public String createToken(String username, Role role) {
		Claims claims = Jwts.claims().setSubject(username);
		claims.put("auth", role);

		Date now = new Date();
		return Jwts.builder().setClaims(claims).setIssuedAt(now)
				.setExpiration(new Date(now.getTime() + VALIDITYTIME))
				.signWith(SignatureAlgorithm.HS256, SECRETKEY).compact();
	}

	
	/** 
	 * @param username
	 * @return Authentication
	 */
	public Authentication getAuthentication(String username) {
		UserDetails userDetails = userDetailsService.loadUserByUsername(username);
		return new UsernamePasswordAuthenticationToken(userDetails.getUsername(), userDetails.getPassword(),
				userDetails.getAuthorities());
	}

	
	/** 
	 * @param jwt
	 * @return Claims
	 */
	public Claims getClaimsFromToken(String jwt) {
		return Jwts.parser().setSigningKey(SECRETKEY).parseClaimsJws(jwt).getBody();
	}

	
	/** 
	 * @param jwt
	 * @return String
	 */
	public String extractUsername(String jwt) {
		return getClaimsFromToken(jwt).getSubject();
	}

	
	/** 
	 * @param jwt
	 * @return Date
	 */
	public Date extractExpirationDate(String jwt) {
		return getClaimsFromToken(jwt).getExpiration();
	}

	
	/** 
	 * @param jwt
	 * @return Boolean
	 */
	public Boolean isExpired(String jwt) {
		return extractExpirationDate(jwt).before(new Date());
	}

	
	/** 
	 * @param jwt
	 * @return Boolean
	 */
	public Boolean validateToken(String jwt) {
		UserDetails userDetails = userDetailsService.loadUserByUsername(extractUsername(jwt));
		return userDetails.getUsername().equals(extractUsername(jwt)) && !isExpired(jwt);
	}
}