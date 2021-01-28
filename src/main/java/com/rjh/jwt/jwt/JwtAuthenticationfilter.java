package com.rjh.jwt.jwt;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rjh.jwt.auth.PrincipalDetails;
import com.rjh.jwt.model.User;

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
		
		try {
			//1. username, password를 받아서
//			BufferedReader br = request.getReader();
//			String input = null;
//			while((input = br.readLine()) != null){
//				System.out.println(input);
//			}
		ObjectMapper om = new ObjectMapper();
		User user = om.readValue(request.getInputStream(), User.class);
		System.out.println(user);
//			System.out.println(request.getInputStream().toString()); 
		//토큰생성 
		UsernamePasswordAuthenticationToken authenticationToken = 
				new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
		
		//2. 정상인지 로그인 시도 authenticationManager로 로그인시도를 하면 principalDetailsService가 호출 loadUserByUsername이실행됨
		// => 함수가 정상적으로 실행되면 authentication가 리턴됨 => db에 usernamerhk password가 일치한다는것 
		// authentication에 내 로그인 정보가 담김 
		Authentication authentication = authenticationManager.authenticate(authenticationToken);
		//authentication객체가 세션영역에 저장됨 
		PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
		System.out.println(principalDetails.getUser().getUsername()); //=> 로그인이 되었다는 뜻 
		// 리턴의 이유는 권한관리를 serurity가 대신해주기 때문.
		//굳이 JWT토큰을 만들이유가 없지만 권한처리를 위해서 세션에 넣어줌 
		//3. PrincipalDetails를 세션에 담고 세션에 담지않으면 권한관리가 되지않음 
		//4. JWT토큰을 만들어서 응답해주면됨. 

		
		return authentication;
		
		} catch (IOException e) {
		
			e.printStackTrace();
		}
		System.out.println("==============================");
	
		return null;
	}
	
	//attemptAuthentication가 종료되고 실행되는 함수
	//JWT토큰을 만들어서 request요청한 사용자에게 JWT토큰을 response해주면됨.  attemptAuthentication함수 안에서 토큰을 만들필요없음 
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		//principalDetails 정보를 활용해서 JWT토큰을 만듦 
		PrincipalDetails principalDetails = (PrincipalDetails) authResult.getPrincipal();
		System.out.println("successfulAuthentication  실행됨 : 인증이 완료 되었다는 뜻");
		
		//RSA방식이 아닌 Hash암호방식 
		String jwtToken = JWT.create()
				.withSubject("rhj토큰") //토큰이름 
				.withExpiresAt(new Date(System.currentTimeMillis()+(60000*10))) //토큰만료시간 (현재시간 + 원하는 만료시간(단위:1/1000초))
				.withClaim("id", principalDetails.getUser().getId()) //비공개클레임. 내가 원하는 넣고싶은 키-밸류값을 넣을수 있음 
				.withClaim("username", principalDetails.getUser().getUsername())
				.sign(Algorithm.HMAC512("rjh")); //JwtProperties.SECRET 내 서버만 아는 고유한 값 
		
		response.addHeader("Authorization", "Bearer "+jwtToken); //사용자한테 키-밸류로 헤더에 담겨서 보내짐 
		/*http://localhost:8080/login 에  post로 정보를 담아 로그인 시도를 해보면 header에 이렇게 정보가 담김 
		Key: Authorization
		Value: Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.
		eyJzdWIiOiJyaGrthqDtgbAiLCJpZCI6MSwiZXhwIjoxNjExODE1Njg0LCJ1c2VybmFtZSI6InRlc3QxIn0.
		VNfgv9jideE7r-dy5E-ANzcd6oRxdZn8k5iOFDcpYrT0g0FBEDhg12auYmkv761gIsi9naVneX963WMC7yeIPA
		 */
		
		/*유저네임, 패스워드 로그인 정상 -> JWT토큰을 생성 -> 클라이언트 쪽으로 JWT토큰을 응답
		 *요청할때마다 JWT토큰을 가지고 요청 . 서버는 JWT토큰이 유효한지 판단하는 필터가 필요 
		 */
	}
	

}
