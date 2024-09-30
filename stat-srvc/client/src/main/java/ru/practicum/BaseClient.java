package ru.practicum;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.lang.Nullable;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

public class BaseClient {
    protected final RestTemplate rest;

    public BaseClient(RestTemplate rest) {
        this.rest = rest;
    }

    protected <T> ResponseEntity<Object> post(String path, @Nullable Map<String, Object> parameters, T body) {
        return makeAndSendRequest(HttpMethod.POST, path, parameters, body);
    }

    protected <T> ResponseEntity<Object> post(String path, T body) {
        return makeAndSendRequest(HttpMethod.POST, path, null, body);
    }

    protected List<HitStatDto> get(String path, @Nullable Map<String, Object> parameters) {
        return makeAndSendGetRequest(HttpMethod.GET, path, parameters);
    }

    private List<HitStatDto> makeAndSendGetRequest(HttpMethod method, String path,
                                                                   @Nullable Map<String, Object> parameters) {
        ParameterizedTypeReference<List<HitStatDto>> typeReference = new ParameterizedTypeReference<List<HitStatDto>>() {};

        try {
            if (parameters != null) {
                var explorewithmeServerResponse = rest.exchange(path, method, null, typeReference, parameters);
                return explorewithmeServerResponse.getBody();
            } else {
                var explorewithmeServerResponse = rest.exchange(path, method, null, typeReference);
                return explorewithmeServerResponse.getBody();
            }
        } catch (HttpStatusCodeException e) {
            throw new RuntimeException();
        }
    }

    private <T> ResponseEntity<Object> makeAndSendRequest(HttpMethod method, String path,
                                                          @Nullable Map<String, Object> parameters, @Nullable T body) {
        HttpEntity<T> requestEntity = new HttpEntity<>(body, defaultHeaders());

        ResponseEntity<Object> explorewithmeServerResponse;

        try {
            if (parameters != null) {
                explorewithmeServerResponse = rest.exchange(path, method, requestEntity, Object.class, parameters);
            } else {
                explorewithmeServerResponse = rest.exchange(path, method, requestEntity, Object.class);
            }
        } catch (HttpStatusCodeException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsByteArray());
        }
        return prepareClientResponse(explorewithmeServerResponse);
    }

    private HttpHeaders defaultHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        return headers;
    }

    private static ResponseEntity<Object> prepareClientResponse(ResponseEntity<Object> response) {

        if (response.getStatusCode().is2xxSuccessful()) {
            return response;
        }

        ResponseEntity.BodyBuilder responseBuilder = ResponseEntity.status(response.getStatusCode());

        if (response.hasBody()) {
            return responseBuilder.body(response.getBody());
        }

        return responseBuilder.build();
    }
}
