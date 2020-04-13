package com.possible.ppmtool.security;

import com.possible.ppmtool.model.User;
import io.jsonwebtoken.*;
//import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.possible.ppmtool.security.SecurityConstants.*;

@Component
public class JwtTokenProvider {
    // Generate token
    public String generateToken(Authentication authentication) throws UnsupportedEncodingException {
        User user = (User)authentication.getPrincipal();  // this user is authenticated at this point
        Date rightNow = new Date(System.currentTimeMillis());

        Date expiryDate = new Date(rightNow.getTime() + EXPIRATION_TIME);

        String userId = Long.toString(user.getId());

        Map<String, Object> claims = new HashMap<>();
        claims.put("id", userId);
        claims.put("username", user.getUsername());
        claims.put("fullName", user.getFullName());

        /*
        *   Key key = Keys.secretKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
        *   SECRET.getBytes(StandardCharsets.UTF_8)
        *   SecretKey key = MacProvider.generateKey(SignatureAlgorithm.HS256);
            String base64Encoded = TextCodec.BASE64.encode(key.getEncoded());
        *
            Key key =MacProvider.generateKey();
            String keyB64 = javax.xml.DataTypeConverter.printBase64Binary(key.getEncoded());

            SecretKey key = MacProvider.generateKey(SignatureAlgorithm.HS512);
            String keyB64 = javax.xml.DataTypeConverter.printBase64Binary(SECRET)
            String base64Encoded = TextCodec.BASE64.encode(SECRET);
        *
        *
        * */

        //String keyB64= Base64.getEncoder().encodeToString(SECRET.getBytes("utf-8"));

        return Jwts.builder()
                .setSubject(userId)
                .setClaims(claims)
                .setIssuedAt(rightNow)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, SECRET )
                .compact();
    }

    // Validate Token

    public boolean validateToken(String token){
        try {
            Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token);
            return true;
        }catch (SignatureException ex){
            System.out.println("Invalid JWT Signature");
        }catch (MalformedJwtException ex){
            System.out.println("Invalid JWT token");
        }catch (ExpiredJwtException ex){
            System.out.println("Expired JWT token");
        }catch (UnsupportedJwtException ex){
            System.out.println("Unsupported JWT token");
        }catch (IllegalArgumentException ex){
            System.out.println("JWT claims string is empty");
        }
         return false;
    }

    // Get user Id out of the token
    public Long getUserIdFromToken(String token){
        Claims claims = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
        String id = (String)claims.get("id");
        return Long.parseLong(id);
    }
}
