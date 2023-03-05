package com.fadlurahmanf.starter.jwt.handler;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JWTTokenUtil implements Serializable {
    private static final long serialVersionUID = -2550185165626007488L;
    public static final long ACCESS_TOKEN_VALIDITY = 60;
    public static final long REFRESH_TOKEN_VALIDITY = 3;

    @Value("${jwt.secret}")
    private String secret;

    @Autowired
    private JWTUserDetailService jwtUserDetailService;

    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public String getIssuerFromToken(String token){
        return getClaimFromToken(token, Claims::getIssuer);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    public Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return doGenerateToken(claims, userDetails.getUsername());
    }

    private String doGenerateToken(Map<String, Object> claims, String subject) {
        return Jwts.builder().setClaims(claims).setIssuer("ACCESS_TOKEN").setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + (ACCESS_TOKEN_VALIDITY * 1000)))
                .signWith(SignatureAlgorithm.HS512, secret).compact();
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        final String issuer = getIssuerFromToken(token);
        return username.equals(userDetails.getUsername()) &&
                !isTokenExpired(token) &&
                issuer.equals("ACCESS_TOKEN");
    }

    public String generateRefreshToken(UserDetails userDetails){
        Map<String, Object> claims = new HashMap<>();
        claims.put("TYPE", "REFRESH_TOKEN");
        claims.put("USERNAME", userDetails.getUsername());
        claims.put("PASSWORD", userDetails.getPassword());
        return doGenerateRefreshToken(claims, userDetails.getUsername());
    }

    private String doGenerateRefreshToken(Map<String, Object> claims, String subject){
        return Jwts.builder().setClaims(claims).setIssuer("REFRESH_TOKEN").setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + (REFRESH_TOKEN_VALIDITY * 1000)))
                .signWith(SignatureAlgorithm.HS512, secret).compact();
    }

    public Boolean validateRefreshToken(String token) {
        final String username = getUsernameFromToken(token);
        final String issuer = getIssuerFromToken(token);
        final UserDetails userDetails = jwtUserDetailService.loadUserByUsername(username);
        return username.equals(userDetails.getUsername()) &&
                issuer.equals("REFRESH_TOKEN") &&
                !isTokenExpired(token);
    }
}
