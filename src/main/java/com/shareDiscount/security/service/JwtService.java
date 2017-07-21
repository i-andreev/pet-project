package com.shareDiscount.security.service;

import com.shareDiscount.domains.UserParam;
import com.shareDiscount.security.exception.JwtTokenMalformedException;
import com.shareDiscount.security.model.AuthenticatedUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static java.time.ZoneOffset.UTC;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    public AuthenticatedUser getUserFromToken(String token){

        UserParam parsedUser = parseToken(token);

        if (parsedUser == null) {
            throw new JwtTokenMalformedException("JWT token is not valid");
        }

        List<GrantedAuthority> authorityList = AuthorityUtils.commaSeparatedStringToAuthorityList(parsedUser.getRole());

        return new AuthenticatedUser(parsedUser.getId(), parsedUser.getUserName(), token, authorityList);
    }

    /**
     * Generates a JWT token containing username as subject, and userId and role as additional claims. These properties are taken from the specified
     * User object. Tokens validity is infinite.
     *
     * @param u the user for which the token will be generated
     * @return the JWT token
     */
    public String generateToken(UserParam u) {
        Claims claims = Jwts.claims().setSubject(u.getUserName());
        claims.put("userId", u.getId() + "");
        claims.put("role", u.getRole());

//        Date expiration = new Date(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(5));

        return Jwts.builder()
                .setClaims(claims)
//                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    /**
     * Tries to parse specified String as a JWT token. If successful, returns UserParam object with username, id and role prefilled (extracted from token).
     * If unsuccessful (token is invalid or not containing all required user properties), simply returns null.
     *
     * @param token the JWT token to parse
     * @return the UserParam object extracted from specified token or null if a token is invalid.
     */
    public UserParam parseToken(String token) {
        UserParam u = null;

        try {
            Claims body = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();

            u = new UserParam();
            u.setUserName(body.getSubject());
            u.setId(Long.parseLong((String) body.get("userId")));
            u.setRole((String) body.get("role"));

        } catch (JwtException e) {
            e.printStackTrace();
        }
        return u;
    }
}
