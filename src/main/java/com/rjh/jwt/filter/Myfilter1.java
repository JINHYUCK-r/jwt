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

public class Myfilter1 implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		System.out.println("필터1");
		chain.doFilter(request, response);		
		/*
		이렇게 쓰면 프린트하고 프로그램이 종료되어버림 
		PrintWriter out = response.getWriter();
		out.print("안녕");
		*/
		
		
	}
	
}
