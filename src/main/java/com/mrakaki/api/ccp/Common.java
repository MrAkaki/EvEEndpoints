package com.mrakaki.api.ccp;

import com.mrakaki.api.dtos.Error;
import com.mrakaki.api.dtos.Response;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

import java.util.*;

public class Common {
    private static Map<String, RestClient> restClient;
    private static String userAgent = "";
    private static String baseUrl = "https://esi.evetech.net/latest/";


    public static void SetUserAgent(String userAgent) {
        Common.userAgent = userAgent;
    }

    public static void SetBaseUrl(String baseUrl) {
        Common.baseUrl = baseUrl;
    }

    public static RestClient GetRestClient() {
        if (userAgent == null || userAgent.isEmpty()) {
            System.err.println("Common::userAgent is empty, please set a correct value with Common::SetUserAgent, check: https://developers.eveonline.com/docs/services/esi/best-practices/");
        }
        return GetRestClient("Standard", Map.of());
    }

    public static RestClient GetRestClient(String clientId, Map<String, String> options) {
        if (restClient == null) restClient = new HashMap<>();
        var rc = restClient.get(clientId);
        if (rc == null) {
            var rcb = RestClient.builder()
                    .baseUrl(baseUrl)
                    .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                    .defaultHeader("X-Compatibility-Date", "2020-01-01")
                    .defaultHeader("X-Tenant", "tranquility")
                    .defaultHeader("Accept-Language", "en")
                    .defaultHeader(HttpHeaders.USER_AGENT, userAgent)
                    .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
            options.forEach((k, v) -> {
                rcb.defaultHeader(k, v).build();
            });
            rc = rcb.build();
            restClient.put(clientId, rc);
        }
        return rc;
    }

    public static <T> Response<T> getSingleEntity(Class<T> type, String url) {
       return getSingleEntity(type,url,null);
    }

    public static <T> Response<T> getSingleEntity(Class<T> type, String url, String token) {
        var restClient = token ==null ? Common.GetRestClient() : Common.GetRestClient("WithToken"+token.hashCode(), Map.of(HttpHeaders.AUTHORIZATION, "Bearer " + token));
        var request = restClient.get().uri(url).retrieve().toEntity(type);
        var eTagList = request.getHeaders().get("ETag");
        var eTag = eTagList == null ? null : eTagList.getFirst();
        Response<T> response;
        if (request.getStatusCode() == HttpStatus.OK) {
            response = new Response<>(request.getBody(), Optional.empty(), 0, 1, eTag);
        } else {
            if (request.getStatusCode().equals(HttpStatus.NOT_MODIFIED)) {
                response = new Response<>(null, Optional.of(new com.mrakaki.api.dtos.Error(304, "Data not modified")), 0, 1, eTag);
            } else {
                response = new Response<>(null, Optional.of(new Error(request.getStatusCode().value(), HttpStatus.resolve(request.getStatusCode().value()).getReasonPhrase())), 0, 1, null);
            }
        }
        return response;
    }

    public static <T> Response<ArrayList<T>> getMultiEntity(String url, ParameterizedTypeReference<ArrayList<T>> typeRef) {
        return getMultiEntity(url, typeRef, null);
    }

    public static <T> Response<ArrayList<T>> getMultiEntity(String url, ParameterizedTypeReference<ArrayList<T>> typeRef, String token) {

        var restClient = token ==null ? Common.GetRestClient() : Common.GetRestClient("WithToken"+token.hashCode(), Map.of(HttpHeaders.AUTHORIZATION, "Bearer " + token));
        var request = restClient.get().uri(url).retrieve().toEntity(typeRef);
        var eTagList = request.getHeaders().get("ETag");
        var eTag = eTagList == null ? null : eTagList.getFirst();
        Response<ArrayList<T>> response;
        if (request.getStatusCode() == HttpStatus.OK) {
            response = new Response<>(request.getBody(), Optional.empty(), 0, 1, eTag);
        } else {
            if (request.getStatusCode().equals(HttpStatus.NOT_MODIFIED)) {
                response = new Response<>(null, Optional.of(new com.mrakaki.api.dtos.Error(304, "Data not modified")), 0, 1, eTag);
            } else {
                response = new Response<>(null, Optional.of(new Error(request.getStatusCode().value(), HttpStatus.resolve(request.getStatusCode().value()).getReasonPhrase())), 0, 1, null);
            }
        }
        return response;
    }
}
