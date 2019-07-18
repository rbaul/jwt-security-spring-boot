package com.github.rbaul.spring.boot.security.services;

import com.github.rbaul.spring.boot.security.config.JwtSecurityProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtProvider {

    private final String secretKey;
    private final long validityInMilliseconds;
    private final String rolesKey;
    private final String privilegesKey;

    @Autowired
    public JwtProvider(JwtSecurityProperties.JwtSecurityTokenProperties jwtSecurityTokenProperties) {
        this.secretKey = Base64.getEncoder().encodeToString(jwtSecurityTokenProperties.getSecretKey().getBytes());
        this.validityInMilliseconds = jwtSecurityTokenProperties.getExpiration();
        this.rolesKey = jwtSecurityTokenProperties.getClaims().getRolesKey();
        this.privilegesKey = jwtSecurityTokenProperties.getClaims().getPrivilegesKey();
    }

    /**
     * Create JWT string given username and granted authorities.
     *
     * @param username       username
     * @param roleNames      roles
     * @param privilegeNames All privilege accesses supported by user
     * @return jwt string
     */
    public String createToken(String username, Set<String> roleNames, Collection<String> privilegeNames) {
        Claims claims = Jwts.claims().setSubject(username);
        claims.put(rolesKey, roleNames);
        claims.put(privilegesKey, privilegeNames);
        Date now = new Date();
        Date expiresAt = new Date(now.getTime() + validityInMilliseconds);
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiresAt)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    /**
     * Validate the JWT String
     *
     * @param token JWT string
     * @return true if valid, false otherwise
     */
    public boolean isValidToken(String token) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * Get the username from the token string
     *
     * @param token jwt
     * @return username
     */
    public String getUsername(String token) {
        return Jwts.parser().setSigningKey(secretKey)
                .parseClaimsJws(token).getBody().getSubject();
    }

    /**
     * Get the Expiration time from the token string
     *
     * @param token JWT string
     * @return Expiration time
     */
    public Date getExpirationTime(String token) {
        return Jwts.parser().setSigningKey(secretKey)
                .parseClaimsJws(token).getBody().getExpiration();
    }

    /**
     * Get the roles from the token string
     *
     * @param token JWT string
     * @return privilege accesses
     */
    public List<GrantedAuthority> getGrantedAuthorities(String token) {
        List<String> privilegeClaims = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .get(privilegesKey, List.class);
        return privilegeClaims.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

}