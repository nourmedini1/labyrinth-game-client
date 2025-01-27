package com.algo.clients;

import com.algo.common.Endpoints;
import com.algo.common.HttpClientSingleton;
import com.algo.common.HttpHelper;
import com.algo.common.ObjectMapperSingleton;
import com.algo.models.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.MediaType;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@ApplicationScoped
public class ChallengeClient {

    public CompletableFuture<Challenge> createChallenge(CreateChallengeRequest createChallengeRequest) {
        HttpClient httpClient = HttpClientSingleton.getInstance();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(Endpoints.BASE_URL + Endpoints.CREATE_CHALLENGE))
                .header("Content-Type", MediaType.APPLICATION_JSON)
                .POST(HttpRequest.BodyPublishers.ofString(createChallengeRequest.toJson()))
                .build();

        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString()).thenApply(
                response -> {
                    if (response.statusCode() == 201) {
                        try {
                            return Challenge.fromJson(response.body(), Challenge.class);
                        } catch (Exception e) {
                            throw new RuntimeException("Failed to parse response", e);
                        }
                    }
                    throw new RuntimeException(response.body());
                }
        );
    }

    public CompletableFuture<UpdateChallengeResponse> updateChallenge(UpdateChallengeRequest updateChallengeRequest) {
        HttpClient httpClient = HttpClientSingleton.getInstance();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(Endpoints.BASE_URL + Endpoints.UPDATE_CHALLENGE))
                .header("Content-Type", "multipart/form-data; boundary=---boundary")
                .PUT(buildFormData(updateChallengeRequest))
                .build();
        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString()).thenApply(
                response -> {
                    if (response.statusCode() == 200) {
                        try {
                            return UpdateChallengeResponse.fromJson(response.body(), UpdateChallengeResponse.class);
                        } catch (Exception e) {
                            throw new RuntimeException("Failed to parse response", e);
                        }
                    }
                    throw new RuntimeException(response.body());
                }
        );
    }

    public CompletableFuture<List<Challenge>> getChallenges(HashMap<String, Object> params) {
        HttpClient httpClient = HttpClientSingleton.getInstance();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(Endpoints.BASE_URL + Endpoints.GET_CHALLENGES + HttpHelper.createQueryString(params)))
                .header("Content-Type", MediaType.APPLICATION_JSON)
                .GET()
                .build();
        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString()).thenApply(
                response -> {
                    if (response.statusCode() == 200) {
                        try {
                            ObjectMapper objectMapper = ObjectMapperSingleton.getInstance();
                            List<String> challengesJsonList = objectMapper.readValue(response.body(), List.class);
                            List<Challenge> challenges = new ArrayList<>();
                            for (String challengeJson : challengesJsonList) {
                                challenges.add(Challenge.fromJson(challengeJson, Challenge.class));
                            }
                            return challenges;
                        } catch (Exception e) {
                            throw new RuntimeException("Failed to parse response", e);
                        }
                    }
                    throw new RuntimeException(response.body());
                }
        );
    }

    public CompletableFuture<Challenge> acceptChallenge(String id) {
        HttpClient httpClient = HttpClientSingleton.getInstance();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(Endpoints.BASE_URL + Endpoints.injectStringIntoPath(Endpoints.ACCEPT_CHALLENGE, id)))
                .header("Content-Type", MediaType.APPLICATION_JSON)
                .method("PATCH", HttpRequest.BodyPublishers.noBody())
                .build();
        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString()).thenApply(
                response -> {
                    if (response.statusCode() == 200) {
                        try {
                            return Challenge.fromJson(response.body(), Challenge.class);
                        } catch (Exception e) {
                            throw new RuntimeException("Failed to parse response", e);
                        }
                    }
                    throw new RuntimeException(response.body());
                }
        );
    }

    public CompletableFuture<Boolean> declineChallenge(String id) {
        HttpClient httpClient = HttpClientSingleton.getInstance();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(Endpoints.BASE_URL + Endpoints.injectStringIntoPath(Endpoints.DECLINE_CHALLENGE, id)))
                .header("Content-Type", MediaType.APPLICATION_JSON)
                .method("PATCH", HttpRequest.BodyPublishers.noBody())
                .build();
        httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString()).thenApply(
                response -> {
                    if (response.statusCode() != 200) {
                        throw new RuntimeException(response.body());
                    }
                    return true;
                }
        );
        return null;
    }

    public CompletableFuture<Boolean> deleteChallenge(String id) {
        HttpClient httpClient = HttpClientSingleton.getInstance();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(Endpoints.BASE_URL + Endpoints.injectStringIntoPath(Endpoints.DELETE_CHALLENGE, id)))
                .header("Content-Type", MediaType.APPLICATION_JSON)
                .DELETE()
                .build();
        httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString()).thenApply(
                response -> {
                    if (response.statusCode() != 204) {
                        throw new RuntimeException(response.body());
                    }
                    return true;
                }
        );
        return null;
    }

    private HttpRequest.BodyPublisher buildFormData(UpdateChallengeRequest updateChallengeRequest) {
        List<byte[]> byteArrays = new ArrayList<>();
        if (updateChallengeRequest.getChallengerScore() != null) {
            byteArrays.add(("--boundary\r\nContent-Disposition: form-data; name=\"challengerScore\"\r\n\r\n" + updateChallengeRequest.getChallengerScore() + "\r\n").getBytes());
        }
        if (updateChallengeRequest.getChallengedScore() != null) {
            byteArrays.add(("--boundary\r\nContent-Disposition: form-data; name=\"challengedScore\"\r\n\r\n" + updateChallengeRequest.getChallengedScore() + "\r\n").getBytes());
        }
        byteArrays.add("--boundary--\r\n".getBytes());
        return HttpRequest.BodyPublishers.ofByteArrays(byteArrays);
    }
}


