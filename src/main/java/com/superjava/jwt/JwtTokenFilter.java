package com.superjava.jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.superjava.user.User;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {
	@Autowired
	private JwtTokenUtil jwtUtil;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		String header = request.getHeader("Authorization");
		System.out.println("Authorization header: " + header);
		
		if(!hasAuthorizationHeader(request)) {
		filterChain.doFilter(request, response);
		return;
		}
		
		String accessToken = getAccessToken(request);
		
		if(!jwtUtil.validateAccessToken(accessToken)) {
			filterChain.doFilter(request, response);
			return;
		}
		setAuthenticationContext(accessToken, request);
		filterChain.doFilter(request, response);	
		
		
	}
	
	private void setAuthenticationContext(String accessToken, HttpServletRequest request) {
	UserDetails userDetails = getUserDetails(accessToken);
	
	UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, null);
	authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
	SecurityContextHolder.getContext().setAuthentication(authentication);
	}
	
	private UserDetails getUserDetails(String accessToken) {
		User userDetails = new User();
		String[] subjectArray = jwtUtil.getSubject(accessToken).split(" , ");
		userDetails.setId(Integer.parseInt(subjectArray[0]));
		userDetails.setEmail(subjectArray[1]);
		return null;
	}
	
	
	private boolean hasAuthorizationHeader(HttpServletRequest request) {
		String header = request.getHeader("Authorization");
		System.out.println("Authorization header: " + header);
		if(ObjectUtils.isEmpty(header) || !header.startsWith("Bearer")) {
			return false;
		}
		
		return true;
	}
	
	
	private String getAccessToken(HttpServletRequest request) {
		String header = request.getHeader("Authorization");
		String token = header.split(" ")[1].trim();
		System.out.println("access token: " + token);
		
		
		return token;
	}

}
