package com.rjh.jwt.filter;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Myfilter3 implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		
		//토큰 : 코스 => 인증 아니면 진입못하게 
		//토큰을 만들어주어야함. id,pw가 정상적으로 들어와서 로그인이 완료되면 토큰을 만들어주고 그걸 응답.
		//요청할때마다 header Authorization에 value값으로 토큰을 가지고 옴
		//그때 토큰이 넘어오면 이 토큰이 내가 만든토큰이 맞는지만 검증하면 됨.(RSA, HS256)
		if(req.getMethod().equals("POST")) {
			System.out.println("POST 요청됨");
			String headerAuth = req.getHeader("Authorization");			
			System.out.println(headerAuth);
			System.out.println("필터3");
			if(headerAuth.equals("COS")) {
				chain.doFilter(req, res);
				//인증이 되면 정확하게 이동을한다.
			}else {
				PrintWriter out = res.getWriter();
				out.print("인증안됨");
			}
		}

		
	}
	
}
