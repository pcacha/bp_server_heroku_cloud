package cz.zcu.students.cacha.bp_server.security;

import cz.zcu.students.cacha.bp_server.domain.User;
import io.jsonwebtoken.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static cz.zcu.students.cacha.bp_server.security.SecurityConstants.EXPIRATION_TIME;
import static cz.zcu.students.cacha.bp_server.security.SecurityConstants.SECRET;

/**
 * Class providing utilities for jwt operations
 */
@Component
public class JwtTokenProvider {

    /**
     * Generates new jwt login token
     * @param authentication authentication context
     * @return new jwt
     */
    public String generateToken(Authentication authentication) {
        // create jwt params
        User user = (User) authentication.getPrincipal();
        Date now = new Date(System.currentTimeMillis());
        Date expireDate = new Date(now.getTime() + EXPIRATION_TIME);
        String userId = Long.toString(user.getId());
        String createdAt = Long.toString(user.getCreatedAt().getTime());

        // insert params in map that is encoded into jwt
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", userId);
        claims.put("username", user.getUsername());
        claims.put("email", user.getEmail());
        claims.put("createdAt", createdAt);
        claims.put("isTranslator", user.isTranslator());
        claims.put("isInstitutionOwner", user.isInstitutionOwner());
        claims.put("isAdmin", user.isAdmin());

        // generate new jwt
        return Jwts.builder()
                .setSubject(userId)
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .compact();
    }

    /**
     * Gets whether given token is valid
     * @param token jwt
     * @return whether given token is valid
     */
    public boolean validateToken(String token) {
        try {
            // try parse token to find out validity
            Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token);
            return true;
        }
        catch(SignatureException ex) {
            System.out.println("Invalid JWT Signature");
        }
        catch(MalformedJwtException ex) {
            System.out.println("Invalid JWT token");
        }
        catch(ExpiredJwtException ex) {
            System.out.println("Expired JWT token");
        }
        catch(UnsupportedJwtException ex) {
            System.out.println("Unsupported JWT token");
        }
        catch(IllegalArgumentException ex) {
            System.out.println("JWT claims string is empty");
        }

        return false;
    }

    /**
     * Gets user id from given token
     * @param token jwt
     * @return user id
     */
    public Long getUserIdFromJWT(String token) {
        // get claims from token
        Claims claims = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
        // get id from claims
        return Long.parseLong((String) claims.get("id"));
    }
}
