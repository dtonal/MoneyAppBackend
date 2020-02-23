package de.dtonal.moneykeeper.security;

import java.io.IOException;
import java.io.InputStream;
import java.security.Key;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.crypto.SecretKey;
import javax.ejb.Singleton;
import javax.ejb.Startup;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;

@Singleton
@Startup
public class JwtService {
	private Key key = null;

	public String createJws(String username) throws IOException {
		Key key = getKey();
		return Jwts.builder().setSubject(username).signWith(key).compact();
	}

	private Key getKey() throws IOException {
		if (key == null) {
			key = initKey();
		}
		return key;
	}

	@PostConstruct
	public void init() {
		try {
			initKey();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String validateToken(String token) throws SignatureException, ExpiredJwtException, UnsupportedJwtException,
			MalformedJwtException, IllegalArgumentException, IOException {
		Jws<Claims> parseClaimsJws = Jwts.parserBuilder().setSigningKey(getKey()).build().parseClaimsJws(token);
		return parseClaimsJws.getBody().getSubject();
	}

	private SecretKey initKey() throws IOException {
		String keyAsString = readJwtKey();
		byte[] decodedSecret = java.util.Base64.getDecoder().decode(keyAsString);
		return Keys.hmacShaKeyFor(decodedSecret);
	}

	private String readJwtKey() throws IOException {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		InputStream input = classLoader.getResourceAsStream("de/dtonal/moneykeeper/config/jwt.properties");
		Properties properties = new Properties();
		properties.load(input);
		return properties.getProperty("key");
	}
}
