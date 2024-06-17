package org.safehouse.apigateway.filter.jwt;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

	@Value("${jwt.secret}")
	private String secret;

	private Key key;

	@PostConstruct
	public void init(){
		this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
	}

	public Claims extractAllClaimsFromToken(String token) {
		return Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
	}

	private boolean isTokenExpired(String token) {
		return this.extractAllClaimsFromToken(token).getExpiration().before(new Date());
	}

	public boolean isInvalid(String token) {
		return this.isTokenExpired(token);
	}

}
