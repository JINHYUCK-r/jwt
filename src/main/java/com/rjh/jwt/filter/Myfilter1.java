package com.rjh.jwt.filter;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class Myfilter1 implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		System.out.println("필터1");
		/* 이렇게 쓰면 프린트하고 프로그램이 종료되어버림 
		PrintWriter out = response.getWriter();
		out.print("안녕");
		*/
		chain.doFilter(request, response);
		
	}
	
}
