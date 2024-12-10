package com.cos.jwt.filter;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class MyFilter3 implements Filter{

	// id, password 가 정상적으로 들어오면 토큰을 만들어 줘야함
	// 요청할때 Authorization 에 value 값으로 토큰을 가져옴
	// 이때 토큰이 넘어오면 이 토큰이 내가만든 토큰이 맞는지만 검증하면 됨
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		// TODO Auto-generated method stub
		HttpServletRequest req = (HttpServletRequest)request;
		HttpServletResponse res = (HttpServletResponse)response;
		
		if(req.getMethod().equals("POST")) {
			System.out.println("Post 요청됨");
			String headerAuth = req.getHeader("Authorization");
			System.out.println(headerAuth);
			
			if(headerAuth.equals("tokenTest")) {  // Authorization 값이 tokenTest인 경우에만 아래 필터가 실행됨
				chain.doFilter(req, res);
			}
			else {
				PrintWriter out = res.getWriter();
				out.println("인증 안됨");
			}
		}
	}

}
