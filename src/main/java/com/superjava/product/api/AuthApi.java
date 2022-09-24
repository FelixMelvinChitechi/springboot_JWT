package com.superjava.product.api;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.superjava.jwt.JwtTokenUtil;
import com.superjava.user.User;

@RestController
public class AuthApi {
	@Autowired
	AuthenticationManager authManager;
	
	@Autowired
	JwtTokenUtil jwtUtil;
	
	@PostMapping("/auth/login")
	public ResponseEntity<?> login (@Valid @RequestBody AuthRequest request){
		
		try {
			
			Authentication authentication = authManager.authenticate(
					
					new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
							);
			//Importing user class
		User user = (User) authentication.getPrincipal();
		
		//create a new authResponse object
		String accessToken = "JWT access token here";
		AuthResponse response = new AuthResponse(user.getEmail(), accessToken);
		return ResponseEntity.ok(response);
		}catch(BadCredentialsException ex){
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
			 
		}
	
	}
	

}
