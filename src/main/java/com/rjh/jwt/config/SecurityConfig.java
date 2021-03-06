package com.rjh.jwt.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;
import org.springframework.web.filter.CorsFilter;

import com.rjh.jwt.filter.Myfilter1;
import com.rjh.jwt.filter.Myfilter3;
import com.rjh.jwt.jwt.JwtAuthenticationfilter;
import com.rjh.jwt.jwt.JwtAuthorizationFilter;
import com.rjh.jwt.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	//빈으로 등록해놓았기 때문에 바로 불러올수 있음 
	private final CorsFilter corsFilter;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private CorsConfig corsConfig;
	
	 @Bean
	    public BCryptPasswordEncoder passwordEncoder() {
	        return new BCryptPasswordEncoder();
	    }

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		//http.addFilter(new Myfilter1()); //그냥 사용하면 오류가 발생함 securityFilter 시작전후에 걸어주어야함 
		//http.addFilterBefore(new Myfilter3(),  SecurityContextPersistenceFilter.class); //시큐리티가 동작하기전에 필터가 실행됨 
		//http.addFilterBefore(new Myfilter3(), BasicAuthenticationFilter.class); //어떤 시큐리티필터 전에 걸릴지 알아두어야함. 
		//지금은 BasicAuthenticationFilter전에 하겠다 설정. 시큐리티필터체인이  다 실행되고 내가 직접만든 필터가 동작함 
		http.csrf().disable();
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 세션을 사용하지 않겠다.
		.and()
		.addFilter(corsFilter) // @CrossOrigin 모델에 등록(인증없을때 ), 필터에 등록해주어야함 인증있을때사용 
		.formLogin().disable()	//폼로그인 안씀 
		.httpBasic().disable() // 
		.addFilter(new JwtAuthenticationfilter(authenticationManager())) //AuthenticationManager파라미터를 넘겨주어야함 
		.addFilter(new JwtAuthorizationFilter(authenticationManager(),userRepository))
		.authorizeRequests()
		.antMatchers("/api/v1/user/**")
		.access("hasRole('ROLE_USER') or hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
		.antMatchers("/api/v1/manager/**")
		.access(" hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
		.antMatchers("/api/v1/admin/**")
		.access("hasRole('ROLE_ADMIN')")
		.anyRequest().permitAll();
		/* .formLogin().disable()했기때문에 밑에꺼가 동작하지 않음 
		.and()
		.formLogin()
		.loginProcessingUrl("/login")
		*/
	}
}
