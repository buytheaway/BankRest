package com.bankrest.dto;
public record LoginRequest(String username, String password) {}
public record JwtResponse(String token) {}
