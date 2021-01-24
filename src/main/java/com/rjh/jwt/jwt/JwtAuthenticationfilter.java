package com.rjh.jwt.jwt;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;

//스프링 시큐리티에서 UsernamePasswordAuthenticationFilter가 있음 
// /login이라고 요청해서 username,과 pasword 로 전송(post) 했을때 동작 

@RequiredArgsConstructor
public class JwtAuthenticationfilter extends UsernamePasswordAuthenticationFilter{
	private final AuthenticationManager authenticationManager;
	
// /login 요청을 하면 로그인 시도를 위해서실행되는 함수 	
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		System.out.println("JwtAuthenticationfilter: 로그인 시도");
		return super.attemptAuthentication(request, response);
	}
	
	//1. username, password를 받아서
	//2. 정상인지 로그인 시도 authenticationManager로 로그인시도를 하면 principalDetailsService가 호출 loadUserByUsername이실행됨  
	//3. PrincipalDetails를 세션에 담고 세션에 담지않으면 권한관리가 되지않음 
	//4. JWT토큰을 만들어서 응답해주면됨. 

}
