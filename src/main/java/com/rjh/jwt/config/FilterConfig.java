package com.rjh.jwt.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.rjh.jwt.filter.Myfilter1;
import com.rjh.jwt.filter.Myfilter2;

//시큐리티 필터체인에 필터를 거는것이 아니라 필터를 만들수있다 
@Configuration
public class FilterConfig {

	@Bean
	public FilterRegistrationBean<Myfilter1> filter1(){
		FilterRegistrationBean<Myfilter1> bean = new FilterRegistrationBean<>(new Myfilter1());
		bean.addUrlPatterns("/*"); //모든요청에서 다 해라 
		bean.setOrder(1); //순서 낮은번호가 필터중에서 가장먼저 실행됨 
		return bean; 
	}
	@Bean
	public FilterRegistrationBean<Myfilter2> filter2(){
		FilterRegistrationBean<Myfilter2> bean = new FilterRegistrationBean<>(new Myfilter2());
		bean.addUrlPatterns("/*"); 
		bean.setOrder(0); //순서 낮은번호가 필터중에서 가장먼저 실행됨 
		return bean; 
	}
}
