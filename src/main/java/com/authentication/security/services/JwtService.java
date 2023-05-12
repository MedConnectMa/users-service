package com.authentication.security.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;


@Service
public class JwtService {
    private static final String SECRET_KEY = "743777217A25432A462D4A614E645267556B58703273357638782F413F442847";

    public String extractUserName(String token) {
        return extractClaim(token, Claims::getSubject); //the subject is the userName
    }
    public String generateToken(UserDetails userDetails){
        return generateToken(new HashMap<>(), userDetails); // generate a token based on user details only, hashmap have an key values empty
    }
    public String generateToken(
            Map<String, Object> extraClaims, // Transforms user claims to object
            UserDetails userDetails // user information
    ){
        return Jwts
                .builder()
                .setClaims(extraClaims)// authorization, role..,
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }
    public boolean isTokenValid(String token, UserDetails userDetails){
        final String userName = extractUserName(token);
        return (userName.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver){ // claimsResolver get Claims var and return T var
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims); // return claims in the form of a T var
    }
    private Claims extractAllClaims(String token){
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token) //Analyse
                .getBody(); // get Token Claims
    }
    private Key getSignInKey(){
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY); // Transforms the SECRET_KEY character string into an array of bytes
        return Keys.hmacShaKeyFor(keyBytes); // Create a signing key

    }

}
