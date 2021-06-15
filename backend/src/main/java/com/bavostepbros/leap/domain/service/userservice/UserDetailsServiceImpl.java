package com.bavostepbros.leap.domain.service.userservice;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.bavostepbros.leap.domain.model.User;
import com.bavostepbros.leap.persistence.UserDAL;
import com.bavostepbros.leap.domain.service.roleservice.RoleService;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private UserDAL userDAL;

	@Autowired
    private RoleService roleService;

	
	/** 
	 * @param email
	 * @return UserDetails
	 * @throws UsernameNotFoundException
	 */
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User user = userDAL.findByEmail(email);
		if (user == null) {
			throw new UsernameNotFoundException("Email " + email + " not found");
		}
		return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(),
				getGrantedAuthority(user));
	}

	
	/** 
	 * @param user
	 * @return Collection<GrantedAuthority>
	 */
	private Collection<GrantedAuthority> getGrantedAuthority(User user) {
		Collection<GrantedAuthority> authorities = new ArrayList<>();
		if (roleService.get(user.getRoleId()).getRoleName().equalsIgnoreCase("admin")) {
			authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
		}
		authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
		return authorities;
	}

}