package com.authentication.security.config;

import com.authentication.security.services.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter { //filter for every request
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;


        if(authHeader == null){ //the header of a token
            filterChain.doFilter(request, response); //let the request pass
            return;
        }
        jwt = authHeader.substring(7);

        //Validation process
        userEmail = jwtService.extractUserName(jwt); //get user name from jwt token
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication()==null){ //username exist and the user not login
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail); //call the methode who get the user from database
            //if the username exist and the token not expired
            if (jwtService.isTokenValid(jwt, userDetails)){
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null, //authorities
                        userDetails.getAuthorities()
                );//store user authentication info
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                //
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request, response);
    }
}
