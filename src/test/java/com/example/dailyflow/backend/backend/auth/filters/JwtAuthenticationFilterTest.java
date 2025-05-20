package com.example.dailyflow.backend.backend.auth.filters;

import com.example.dailyflow.backend.backend.auth.TokenJwtConfig;
import com.example.dailyflow.backend.backend.models.entities.User;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class JwtAuthenticationFilterTest {

    private AuthenticationManager authenticationManager;
    private JwtAuthenticationFilter jwtFilter;

    @BeforeEach
    void setUp() {
        authenticationManager = mock(AuthenticationManager.class);
        jwtFilter = new JwtAuthenticationFilter(authenticationManager);
    }

    @Test
    void testAttemptAuthentication() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        User mockUser = new User();
        mockUser.setUsername("testuser");
        mockUser.setPassword("1234");

        byte[] userJson = new ObjectMapper().writeValueAsBytes(mockUser);
        when(request.getInputStream()).thenReturn(new DelegatingServletInputStream(userJson));

        Authentication expectedAuth = new UsernamePasswordAuthenticationToken("testuser", "1234");
        when(authenticationManager.authenticate(any())).thenReturn(expectedAuth);

        Authentication result = jwtFilter.attemptAuthentication(request, response);
        assertEquals(expectedAuth, result);
    }

    @Test
    void testSuccessfulAuthenticationAddsToken() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);

        PrintWriter writer = mock(PrintWriter.class);
        when(response.getWriter()).thenReturn(writer);

        UserDetails principal = new org.springframework.security.core.userdetails.User(
                "testuser", "1234", List.of());

        Authentication authResult = mock(Authentication.class);
        when(authResult.getPrincipal()).thenReturn(principal);

        jwtFilter.successfulAuthentication(request, response, chain, authResult);

        verify(response).addHeader(eq(TokenJwtConfig.HEADER_AUTHORITATION), startsWith(TokenJwtConfig.PREFIX_KEY));
        verify(response).setStatus(200);
        verify(response).setContentType("application/json");
        verify(writer).write(contains("token"));
        verify(writer).write(contains("testuser"));
    }

    @Test
    void testUnsuccessfulAuthenticationResponds401() throws IOException, ServletException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        PrintWriter writer = mock(PrintWriter.class);
        when(response.getWriter()).thenReturn(writer);

        AuthenticationException ex = mock(AuthenticationException.class);
        when(ex.getMessage()).thenReturn("Bad credentials");

        jwtFilter.unsuccessfulAuthentication(request, response, ex);

        verify(response).setStatus(401);
        verify(response).setContentType("application/json");
        verify(writer).write(contains("Error en la autenticacion"));
        verify(writer).write(contains("Bad credentials"));
    }

    // Utilidad para simular ServletInputStream con datos
    static class DelegatingServletInputStream extends jakarta.servlet.ServletInputStream {
        private final ByteArrayInputStream bais;

        DelegatingServletInputStream(byte[] data) {
            this.bais = new ByteArrayInputStream(data);
        }

        @Override
        public int read() throws IOException {
            return bais.read();
        }

        @Override
        public boolean isFinished() {
            return bais.available() == 0;
        }

        @Override
        public boolean isReady() {
            return true;
        }

        @Override
        public void setReadListener(jakarta.servlet.ReadListener readListener) {
            // No-op
        }
    }
}
