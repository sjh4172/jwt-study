package com.cos.jwt.config.jwt;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Date;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.cos.jwt.config.auth.PrincipalDetails;
import com.cos.jwt.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

// /login 요청을 해서 user 정보를 전송하면 해당 필터가 동작
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter{
	private final AuthenticationManager authenticationManager;
	
	// /login 요청을 하면 로그인 시도를 위해서 실행되는 함수
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		System.out.println("** attemptAuthentication 실행됨 **");
		try {

			// 1. username, password 를 받아옴
			ObjectMapper om = new ObjectMapper();
			User user = om.readValue(request.getInputStream(), User.class);	// 자동으로 User에 매핑해줌
			System.out.println(user);

			
			// 2. 정상인지 Login 시도, authenticationManager로 로그인 시도를 하면 PrincipalDetailsService가 호출됨 (loadUserByUsername())			
			UsernamePasswordAuthenticationToken authenticationToken = 
					new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());

			Authentication authentication = 
					authenticationManager.authenticate(authenticationToken);
			
			PrincipalDetails principalDetails = (PrincipalDetails)authentication.getPrincipal();
			System.out.println("로그인 완료됨: " + principalDetails.getUser().getUsername());
			
			// 3. PrincipalDetailsService를 세션에 담고 (권한 관리를 위해)
			return authentication;
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	// attemptAuthentication 실행 후 인증이 정상적으로 되었으면 successfulAuthentication 함수가 실행됨
	// 4. JWT 토큰을 만들어서 request 요청한 사용자에게 JWT 토큰을 response
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		System.out.println("** successfulAuthentication 실행됨, 로그인이 완료되었다는 뜻 **");
		PrincipalDetails principalDetails = (PrincipalDetails)authResult.getPrincipal();
		
		// Hash 암호 방식
		String jwtToken = JWT.create()
				.withSubject("tokenName")		// 토큰 이름 (자유롭게 설정)
				.withExpiresAt(new Date(System.currentTimeMillis()+(60000*30)))  // 만료 시간
				.withClaim("id", principalDetails.getUser().getUsername())
				.withClaim("password", principalDetails.getUser().getPassword())
				.sign(Algorithm.HMAC512("sjh"));	// 서버마다 있는 고요한 값 (자유롭게 설정)
		
		response.addHeader("Authorization", "Bearer "+ jwtToken);
	}
}	
