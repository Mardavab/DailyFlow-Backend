package com.example.dailyflow.backend.backend.auth;

import java.security.Key;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

public class TokenJwtConfig {
    
     public final static Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    public final static String PREFIX_TOKEN = "Bearer ";
    public final static String HEADER_AUTHORIZATION = "Authorization";
}
