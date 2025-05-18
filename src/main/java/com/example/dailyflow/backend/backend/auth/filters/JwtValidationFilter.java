package com.example.dailyflow.backend.backend.auth.filters;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import static com.example.dailyflow.backend.backend.auth.TokenJwtConfig.*;


public class JwtValidationFilter extends BasicAuthenticationFilter{
 public JwtValidationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
                
                String header = request.getHeader(HEADER_AUTHORITATION);
                
                if (header == null || !header.startsWith(PREFIX_KEY)) {
                    chain.doFilter(request, response);
                    return;
                }

                String token = header.replace(PREFIX_KEY, "");

                try{

                   Claims claims = Jwts.parser().verifyWith(SECRET_KEY).build().parseClaimsJws(token).getBody();

                   String username = claims.getSubject();
                   
                    List<GrantedAuthority> authorities = new ArrayList<>();
                    authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(username,null,  authorities);
                    SecurityContextHolder.getContext().setAuthentication(authentication); 
                    chain.doFilter(request, response);
                } catch(JwtException e){
                    Map<String, String> body = new HashMap<>();
                    body.put("error", e.getMessage());
                    body.put("message", "El token no es valido");
                    response.getWriter().write(new ObjectMapper().writeValueAsString(body));
                    response.setStatus(403);
                    response.setContentType("application/json");
                }
    }
}
