package com.mrakaki.api.dtos;

public record AuthenticationInformation(int characterId, String characterName, String accessToken, String refreshToken) {
}
