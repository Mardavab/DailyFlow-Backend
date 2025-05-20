package com.example.dailyflow.backend.backend.auth.filters;

import com.example.dailyflow.backend.backend.auth.TokenJwtConfig;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.PrintWriter;
import java.security.Key;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class JwtValidationFilterTest {

    private JwtValidationFilter jwtValidationFilter;
    private AuthenticationManager authenticationManager;

    @BeforeEach
    void setUp() {
        authenticationManager = mock(AuthenticationManager.class);
        jwtValidationFilter = new JwtValidationFilter(authenticationManager);
        SecurityContextHolder.clearContext();
    }

    @Test
    void testDoFilterInternalWithValidToken() throws Exception {
        String username = "testuser";
        String token = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 3600000))
                .signWith(TokenJwtConfig.SECRET_KEY)
                .compact();

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);

        when(request.getHeader(TokenJwtConfig.HEADER_AUTHORITATION)).thenReturn(TokenJwtConfig.PREFIX_KEY + token);

        jwtValidationFilter.doFilterInternal(request, response, chain);

        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        assertEquals(username, SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        verify(chain, times(1)).doFilter(request, response);
    }

    @Test
    void testDoFilterInternalWithInvalidToken() throws Exception {
        String invalidToken = "invalid.token.value";

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);
        PrintWriter writer = mock(PrintWriter.class);

        when(request.getHeader(TokenJwtConfig.HEADER_AUTHORITATION)).thenReturn(TokenJwtConfig.PREFIX_KEY + invalidToken);
        when(response.getWriter()).thenReturn(writer);

        jwtValidationFilter.doFilterInternal(request, response, chain);

        verify(response).setStatus(403);
        verify(response).setContentType("application/json");
        verify(writer).write(contains("El token no es valido"));
        verify(chain, never()).doFilter(request, response);
    }

    @Test
    void testDoFilterInternalWithoutToken() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);

        when(request.getHeader(TokenJwtConfig.HEADER_AUTHORITATION)).thenReturn(null);

        jwtValidationFilter.doFilterInternal(request, response, chain);

        verify(chain).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }
}
