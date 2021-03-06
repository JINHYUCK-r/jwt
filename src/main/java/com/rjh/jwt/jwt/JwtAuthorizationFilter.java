package com.rjh.jwt.jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.rjh.jwt.auth.PrincipalDetails;
import com.rjh.jwt.model.User;
import com.rjh.jwt.repository.UserRepository;

//시큐리티가 filter가지고 있는데 그 필터중에 BasicAuthenticationFilter라는 것이 있음
//권한이나 인증이 필요한 특정주소를 요청했을대 위 필터를 무조건 타게 되어 있음 
//만약에 권한이나 인증이 필요한 주소가 아니라면 이 필터를 안탐
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {
	
	private UserRepository userRepository;

	public JwtAuthorizationFilter(AuthenticationManager authenticationManager, UserRepository userRepository ) {
		super(authenticationManager);
		this.userRepository = userRepository;
		
	}

	//인증이나 권한이 필요한 주소요청이 있을때 해당필터를 타게됨
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		//super.doFilterInternal(request, response, chain);
		System.out.println("인증이나 권한이 필요한 주소 요청이 됨");
		
		String jwtHeader = request.getHeader("Authorization");
		System.out.println("jwtHeader : "+jwtHeader);
		
		//header가 있는지 확인 
		if(jwtHeader == null || !jwtHeader.startsWith("Bearer")) {
			chain.doFilter(request, response);
			return;
		}
		
		//JWT토큰을 검증해서 정상적인 사용자인지 확인
		String jwtToken = request.getHeader("Authorization").replace("Bearer ", "");
		String username = JWT.require(Algorithm.HMAC512("rjh")).build().verify(jwtToken).getClaim("username").asString(); //서명 
												//JwtAuthenticationfilter에서 정의해놓은 시크릿코드 : 	rjh 				withClaim에 적어놓은 username
		
		//서명이 제대로 됨 
		if(username !=null) {
			User userEntity = userRepository.findByUsername(username);
			System.out.println(userEntity.getUsername());
			PrincipalDetails principlaDetails = new PrincipalDetails(userEntity);
			//강제로 Authentication객체 만들기 
			//Jwt토큰 서명을 통해서 서명이 정상이면 Authentication객체를 만들어준다. 
			Authentication authentication = new UsernamePasswordAuthenticationToken(principlaDetails, null, principlaDetails.getAuthorities());
			
			//강제로 시큐리티에 접근하여 Authentication 객체를 저장 
			SecurityContextHolder.getContext().setAuthentication(authentication);
		}
		chain.doFilter(request, response);
		
	}
	
}
