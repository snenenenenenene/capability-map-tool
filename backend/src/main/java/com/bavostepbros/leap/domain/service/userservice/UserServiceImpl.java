package com.bavostepbros.leap.domain.service.userservice;

import java.util.List;
import java.util.Random;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.bavostepbros.leap.domain.model.Role;
import com.bavostepbros.leap.domain.model.User;
import com.bavostepbros.leap.domain.service.roleservice.RoleService;
import com.bavostepbros.leap.persistence.UserDAL;

@Service
@Transactional
public class UserServiceImpl implements UserService {
	@Autowired
	private UserDAL userDAL;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private RoleService roleService;

	@PostConstruct
	private void init() {
		User user = save("super_admin", "super_admin", "super_admin", 1);				
		User viewingUser = save("viewing_user", "viewing_user", "viewing_user", 4);
	}

	@Override
	public User save(String username, String password, String email, Integer roleId) {
		User user = userDAL.save(new User(username, passwordEncoder.encode(password), email));
		Role role = roleService.get(roleId);
		user.addRole(role);
		userDAL.save(user);
    	return user;
	}

	@Override
	public User get(Integer id) {
		return userDAL.findById(id).get();
	}

	
	@Override
	public User getByEmail(String email) {
		return userDAL.findByEmail(email);
	}
	
	@Override
	public List<User> getAll() {
		return userDAL.findAll();
	}
	
	@Override
	public User update(Integer userId, String username, String password, String email, Integer roleId) {
		User user = userDAL.save(new User(userId, username, passwordEncoder.encode(password), email));
		Role role = roleService.get(roleId);
		user.addRole(role);
		userDAL.save(user);
    	return user;
	}

	@Override
	public void delete(Integer id) {
		userDAL.deleteById(id);
	}	

	@Override
	public Authentication authenticate(String email, String password) throws AuthenticationException {
		return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
	}

	@Override
	public boolean existsById(Integer id) {
		return userDAL.findById(id).isPresent();
	}

	@Override
	public boolean existsByUsername(String username){
		return !(userDAL.findByUsername(username) == null);
	}

	@Override
	public boolean existsByEmail(String email){
		return !(userDAL.findByEmail(email) == null);
	}

	@Override
	public String generatePassword(){
		String generatedPassword;

		int lowerLimit = 48; 
  	    int higherLimit = 122; 
	    int passwordLength = 10;
	    Random random = new Random();

	    generatedPassword = random.ints(lowerLimit, higherLimit + 1)
	      .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
	      .limit(passwordLength)
	      .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
	      .toString();
		
		return generatedPassword;
	}

}