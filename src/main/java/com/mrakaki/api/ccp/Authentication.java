package com.mrakaki.api.ccp;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mrakaki.api.dtos.AuthenticationInformation;
import com.mrakaki.api.dtos.AuthenticationResponse;
import com.mrakaki.api.dtos.Response;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class Authentication {
    public static Response<AuthenticationInformation> ProcessCode(String code, String clientId, String clientSecret, String tokenUri) {
        try {
            var restClient = Common.GetRestClient("Authentication", Map.of(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE, HttpHeaders.AUTHORIZATION, basicAuthentication(clientId, clientSecret)));
            var body = "grant_type=authorization_code&code=" + code;
            var codeResponse = restClient.post().uri(tokenUri).body(body).retrieve().toEntity(AuthenticationResponse.class);
            if (codeResponse.getStatusCode() != HttpStatus.OK) {
                return new Response<>(null, Optional.of(new com.mrakaki.api.dtos.Error(codeResponse.getStatusCode().value(), "Error during authentication")), 0, 1, "");
            }
            var codeInfo = codeResponse.getBody();
            if (codeInfo == null) {
                return new Response<>(null, Optional.of(new com.mrakaki.api.dtos.Error(500, "Error during authentication")), 0, 1, "");
            }
            String[] chunks = codeInfo.access_token().split("\\.");

            Base64.Decoder decoder = Base64.getUrlDecoder();
            String payload = new String(decoder.decode(chunks[1]));

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode node = objectMapper.readTree(payload);
            int characterId = Integer.parseInt(node.get("sub").asText().split(":")[2]);
            String characterName = node.get("name").asText();
            return new Response<>(new AuthenticationInformation(characterId, characterName, codeInfo.access_token(), codeInfo.refresh_token()), Optional.empty(), 0, 1, "");
        } catch (Exception ex) {
            return new Response<>(null, Optional.of(new com.mrakaki.api.dtos.Error(500, ex.getMessage())), 0, 1, null);
        }
    }

    private static String GetAuthenticationUrl(String redirectUrl, String authorizationUrl, String clientId, Set<String> characterScopes, String state) {
        return authorizationUrl + "?response_type=code&redirect_uri=" + encodeValue(redirectUrl) + "&client_id=" + clientId + "&scope=" + encodeValue(String.join(",", characterScopes)) + "&state=" + state;
    }

    private static String basicAuthentication(String clientId, String clientSecret) {
        return "Basic " + Base64.getEncoder().encodeToString((clientId + ":" + clientSecret).getBytes());
    }

    private static String encodeValue(String value) {
        try {
            return URLEncoder.encode(value, StandardCharsets.UTF_8);
        } catch (Exception e) {
            return "";
        }
    }
}
