package com.whatsapp.config;

import java.util.Date;
import java.util.UUID;

import javax.crypto.SecretKey;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class TokenProvider {

	// Secret key for signing the JWT token
	SecretKey key = Keys.hmacShaKeyFor(JwtConstant.SECRET_KEY.getBytes());

	// Method to generate JWT token
	public String generateToken(Authentication authentication) {
		// Create the JWT token with claims
		String jwt = Jwts.builder()
				.setIssuer("WhatsApp clone")  // Issuer of the token
				.setIssuedAt(new Date())  // Issue time of the token
				.setExpiration(new Date(new Date().getTime() + 86400000))  // Expiration time (1 day)
				.claim("email", authentication.getName())  // Set email as a claim
				.claim("authorities", authentication.getAuthorities().toString())  // Set authorities as a claim
				.setId(generateUniqueJwtId())  // Add unique JWT ID
				.signWith(key)  // Sign the token with the secret key
				.compact();  // Compact the token into a string

		return jwt;
	}

	// Method to generate unique JWT ID using UUID
	private String generateUniqueJwtId() {
		return UUID.randomUUID().toString();  // Generates a unique ID for each JWT token
	}

	public String getEmailFromToken(String jwt) {
		// Remove "Bearer " prefix if present (optional)
		if (jwt.startsWith("Bearer ")) {
			jwt = jwt.substring(7);
		}

		// Parse the JWT and extract the claims
		Claims claims = Jwts.parserBuilder()
				.setSigningKey(key)  // Ensure the key matches what was used to sign the token
				.build()
				.parseClaimsJws(jwt)
				.getBody();

		// Extract email from the claims
		return String.valueOf(claims.get("email"));
	}

}
