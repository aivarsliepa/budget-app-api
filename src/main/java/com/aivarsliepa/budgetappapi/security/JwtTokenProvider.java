package com.aivarsliepa.budgetappapi.security;

import io.jsonwebtoken.*;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {
    @NonNull
    private JwtConfig jwtConfig;

    public String generateToken(Authentication authentication) {
        var userPrincipal = (UserPrincipal) authentication.getPrincipal();

        return generateTokenFromId(userPrincipal.getId());
    }

    public String generateTokenFromId(Long id) {
        var expiryDate = new Date(new Date().getTime() + jwtConfig.getExpiration());

        return Jwts.builder()
                   .setSubject(Long.toString(id))
                   .setIssuedAt(new Date())
                   .setExpiration(expiryDate)
                   .signWith(SignatureAlgorithm.HS512, jwtConfig.getSecret())
                   .compact();
    }

    public Long getUserIdFromJWT(String token) {
        var subject = Jwts.parser()
                          .setSigningKey(jwtConfig.getSecret())
                          .parseClaimsJws(token)
                          .getBody()
                          .getSubject();

        return Long.parseLong(subject);
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtConfig.getSecret()).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException ex) {
            log.debug("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            log.debug("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            log.debug("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            log.debug("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            log.debug("JWT claims string is empty.");
        }

        return false;
    }
}