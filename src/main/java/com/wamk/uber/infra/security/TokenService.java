package com.wamk.uber.infra.security;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.wamk.uber.entities.user.User;

@Service
public class TokenService {

	@Value("${api.security.token.secret}")
	private String secret;
	
	public String generateToken(User user) {
		try {
			Algorithm algorithm = Algorithm.HMAC256(secret);
			String token = JWT.create()
					.withIssuer("servico-viagem")
					.withSubject(user.getLogin())
					.withExpiresAt(genExpirattionDate())
					.sign(algorithm);
			
			return token;
			
		} catch(JWTCreationException e) {
			throw new RuntimeException("Error while generating token", e);
		}
	}
	
	public String validateToken(String token) {
		try {
			Algorithm algorithm = Algorithm.HMAC256(secret);
			return JWT.require(algorithm)
					.withIssuer("servico-viagem")
					.build()
					.verify(token)
					.getSubject();
			
		} catch(JWTVerificationException e) {
			return "";
		}
	}
	
	private Instant genExpirattionDate() {
		return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
	}
}
