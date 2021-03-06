package com.rjh.jwt.controller;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rjh.jwt.auth.PrincipalDetails;
import com.rjh.jwt.model.User;
import com.rjh.jwt.repository.UserRepository;

import lombok.RequiredArgsConstructor;



@RestController
@RequestMapping("api/v1")
@RequiredArgsConstructor
public class RestApiController {
	
	private final UserRepository userRepository;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@GetMapping("/home")
	public String home() {
		return "<h1>home</h1>";
	}
	@PostMapping("/token")
	public String token() {
		return "<h1>token</h1>";
	}

		@PostMapping("join")
		public String join(@RequestBody User user) {
			user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
			user.setRoles("ROLE_USER");
			userRepository.save(user);
			return "회원가입완료";

		}
		
		//user,manager,admin 접근가능 
		@GetMapping("user")
		public String user() {
			return "user";
		}
		
		//manager, admin
		@GetMapping("manager")
		public String manager() {
			return "manager";
		}
		
		//admin
		@GetMapping("admin")
		public String admin() {
			return "admin";
		}
		
}
