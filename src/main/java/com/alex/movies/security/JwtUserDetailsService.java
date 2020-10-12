package com.alex.movies.security;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.alex.movies.model.Person;
import com.alex.movies.repository.UserRepository;

@Service
public class JwtUserDetailsService implements UserDetailsService, UserService {

	@Autowired
	private final UserRepository userRepository;

	@Autowired
	private PasswordEncoder bcryptEncoder;

	public JwtUserDetailsService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) {
		Person person = userRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException(String.format("USER_NOT_FOUND '%s'.", username)));
		return new User(person.getUsername(), person.getPassword(), new ArrayList<>());
	}

	private List<SimpleGrantedAuthority> getAuthority() {
		return Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN"));
	}

	public List<Person> findAll() {
		List<Person> list = new ArrayList<>();
		userRepository.findAll().iterator().forEachRemaining(list::add);
		return list;
	}

	@Override
	public boolean delete(String id)  {
		if (userRepository.findById(id).isEmpty()) {
			return false;
		}
		userRepository.deleteById(id);
		return true;

	}

	@Override
	public Person findOne(String username) {
		return userRepository.findByUsername(username).get();
	}

	@Override
	public Person findById(String id) {
		Optional<Person> optionalUser = userRepository.findById(id);
		return optionalUser.isPresent() ? optionalUser.get() : null;
	}

	@Override
	public boolean update(Person person, String id) {
		Person user = findById(id);
		if (user==null) {
			return false;
		}
		if (user != null) {
			BeanUtils.copyProperties(person, user, "password");
			userRepository.save(user);
		}
		return true;

	}

	@Override
	public boolean save(Person user) {
		if(userRepository.findByUsername(user.getUsername()).isPresent()) {
			return false;
		}
		Person newUser = new Person();
		newUser.setUsername(user.getUsername());
		newUser.setFirstName(user.getFirstName());
		newUser.setLastName(user.getLastName());
		newUser.setPassword(bcryptEncoder.encode(user.getPassword()));
		newUser.setAge(user.getAge());
		userRepository.save(newUser);
		return true;
	}

}
