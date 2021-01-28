package com.rjh.jwt.auth;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.rjh.jwt.model.User;
import com.rjh.jwt.repository.UserRepository;

import lombok.RequiredArgsConstructor;

//http//localhost:8080/login 할때 동작해야하는데 우리는 .formLogin().disable() 했기때문에 동작하지 않음 
@Service
@RequiredArgsConstructor
public class PrincipalDetailService implements UserDetailsService{
	
	private final UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		System.out.println("PrincipalDetailService의 loadUserByUsername()");
		User userEntity = userRepository.findByUsername(username);
		System.out.println("userEntity : "+ userEntity);
		return new PrincipalDetails(userEntity);
	}

}
