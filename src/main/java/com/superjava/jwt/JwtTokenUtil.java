package com.superjava.jwt;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.superjava.user.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

@Component
public class JwtTokenUtil {
	@SuppressWarnings("unused")
	private static final long EXPIRE_DATE = 24 * 60 * 60 * 1000; //24 hours

	private static final long Expire_Duration = 0;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(JwtTokenUtil.class);
	
	@Value("${app.jwt.secret}")
	private String secretKey;

	public String GenerateAccessToken(User user) {
		
		return Jwts.builder()
				.setSubject(user.getId() + "," + user.getEmail())
				.setIssuer("CodeJava")
				.setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + Expire_Duration))
				.signWith(SignatureAlgorithm.HS512, secretKey)
				.compact();
	}
	//validating web token
	public boolean validateAccessToken(String token) {
		try {
		Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);  
		
		return true;
		
		} catch(ExpiredJwtException ex) {
			LOGGER.error("JWT expired", ex);
			
		}catch(IllegalArgumentException ex) {
			LOGGER.error("Token is empty, null , or has whitespace only", ex);
		}catch(MalformedJwtException ex) {
			LOGGER.error("JWT is invalid", ex);
		}catch(UnsupportedJwtException ex) {
			LOGGER.error("Jwt is not supported", ex);
		}catch(SignatureException ex) {
			LOGGER.error("signature validation failed", ex);
		}
		
		return false;
	}
	public String getSubject(String token) {
		return parseClaims(token).getSubject();
	}
	private Claims parseClaims(String token) {
		return Jwts.parser()
				.setSigningKey(secretKey)
				.parseClaimsJws(token)
				.getBody();
		
	}
}

