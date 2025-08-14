package com.mrakaki.api.dtos;

public record AuthenticationResponse(String access_token, int expires_in, String refresh_token) { }
