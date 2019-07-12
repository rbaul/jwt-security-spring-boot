package com.github.rbaul.spring.boot.security.services;

import com.github.rbaul.spring.boot.security.config.JwtSecurityProperties;
import com.github.rbaul.spring.boot.security.domain.model.Role;
import com.github.rbaul.spring.boot.security.domain.model.User;
import com.github.rbaul.spring.boot.security.domain.repository.UserRepository;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtProvider{

    private String secretKey;
    private long validityInMilliseconds;
    private String rolesKey;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    public JwtProvider(JwtSecurityProperties.JwtSecurityTokenProperties jwtSecurityTokenProperties) {
        this.secretKey = Base64.getEncoder().encodeToString(jwtSecurityTokenProperties.getSecretKey().getBytes());
        this.validityInMilliseconds = jwtSecurityTokenProperties.getExpiration();
        this.rolesKey = jwtSecurityTokenProperties.getRolesKey();
    }

    /**
     * Create JWT string given username and roles.
     *
     * @param username
     * @param roles
     * @return jwt string
     */
    public String createToken(String username, List<Role> roles) {
        Claims claims = Jwts.claims().setSubject(username);
        claims.put(rolesKey, roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getAuthority()))
                .collect(Collectors.toList()));
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
        } catch (ExpiredJwtException e) {
            log.debug("Token expired");
            String username = getUsername(token);
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new IllegalArgumentException("User not exist"));
//            user.ge
            // TODO: update time
            return false;
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
     * Get the roles from the token string
     *
     * @param token jwt
     * @return username
     */
    public List<GrantedAuthority> getRoles(String token) {
        List<Map<String, String>>  roleClaims = Jwts.parser().setSigningKey(secretKey)
                .parseClaimsJws(token).getBody().get(rolesKey, List.class);
        return roleClaims.stream().map(roleClaim ->
                new SimpleGrantedAuthority(roleClaim.get("authority")))
                .collect(Collectors.toList());
    }
}