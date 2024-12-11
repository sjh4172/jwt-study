package com.cos.jwt.config.auth;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.cos.jwt.model.User;
import com.cos.jwt.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService{
	 
	private final UserRepository userRepository;
	
	@Override 
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		System.out.println("** loadUserByUsername 실행됨 **");
		User userEntity = userRepository.findByUsername(username); 
		
		return new PrincipalDetails(userEntity);
	}
}
