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

@ApplicationScoped
public class ChallengeClient {

    public Challenge createChallenge(CreateChallengeRequest createChallengeRequest) {
        HttpClient httpClient = HttpClientSingleton.getInstance();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(Endpoints.BASE_URL + Endpoints.CREATE_CHALLENGE))
                .header("Content-Type", MediaType.APPLICATION_JSON)
                .POST(HttpRequest.BodyPublishers.ofString(createChallengeRequest.toJson()))
                .build();

        HttpResponse<String> response = httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString()).join();
        if (response.statusCode() == 201) {
            try {
                return Challenge.fromJson(response.body(), Challenge.class);
            } catch (Exception e) {
                throw new RuntimeException("Failed to parse response", e);
            }
        }
        throw new RuntimeException(response.body());
    }

    public UpdateChallengeResponse updateChallenge(UpdateChallengeRequest updateChallengeRequest) {
        HttpClient httpClient = HttpClientSingleton.getInstance();
        HashMap<String,Object> params = new HashMap<>();
        params.put("challengerScore", updateChallengeRequest.getChallengerScore());
        params.put("challengedScore", updateChallengeRequest.getChallengedScore());
        String formData = HttpHelper.createFormUrlEncodedBody(params);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(Endpoints.BASE_URL + Endpoints.UPDATE_CHALLENGE))
                .header("Content-Type", MediaType.MULTIPART_FORM_DATA)
                .PUT(HttpRequest.BodyPublishers.ofString(formData))
                .build();
        HttpResponse<String> response = httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString()).join();
        if (response.statusCode() == 200) {
            try {
                return UpdateChallengeResponse.fromJson(response.body(), UpdateChallengeResponse.class);
            } catch (Exception e) {
                throw new RuntimeException("Failed to parse response", e);
            }
        }
        throw new RuntimeException(response.body());
    }

    public List<Challenge> getChallenges(HashMap<String, Object> params) {
        HttpClient httpClient = HttpClientSingleton.getInstance();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(Endpoints.BASE_URL + Endpoints.GET_CHALLENGES + HttpHelper.createQueryString(params)))
                .header("Content-Type", MediaType.APPLICATION_JSON)
                .GET()
                .build();
        HttpResponse<String> response = httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString()).join();
        if (response.statusCode() == 200) {
            try {
                ObjectMapper objectMapper = ObjectMapperSingleton.getInstance();
                List<String> challengesJsonList = objectMapper.readValue(response.body(), List.class);
                List<Challenge> challenges = new ArrayList<>();
                for (String challengeJson : challengesJsonList) {;
                    challenges.add(Challenge.fromJson(challengeJson, Challenge.class));
                }
                return challenges;
            } catch (Exception e) {
                throw new RuntimeException("Failed to parse response", e);
            }
        }
        throw new RuntimeException(response.body());
    }

    public Challenge acceptChallenge(String id) {
        HttpClient httpClient = HttpClientSingleton.getInstance();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(Endpoints.BASE_URL + Endpoints.injectStringIntoPath(Endpoints.ACCEPT_CHALLENGE, id)))
                .header("Content-Type", MediaType.APPLICATION_JSON)
                .method("PATCH", HttpRequest.BodyPublishers.noBody())
                .build();
        HttpResponse<String> response = httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString()).join();
        if (response.statusCode() == 200) {
            try {
                return Challenge.fromJson(response.body(), Challenge.class);
            } catch (Exception e) {
                throw new RuntimeException("Failed to parse response", e);
            }
        }
        throw new RuntimeException(response.body());
    }

    public void declineChallenge(String id) {
        HttpClient httpClient = HttpClientSingleton.getInstance();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(Endpoints.BASE_URL + Endpoints.injectStringIntoPath(Endpoints.DECLINE_CHALLENGE, id)))
                .header("Content-Type", MediaType.APPLICATION_JSON)
                .method("PATCH", HttpRequest.BodyPublishers.noBody())
                .build();
        HttpResponse<String> response = httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString()).join();
        if (response.statusCode() != 200) {
            throw new RuntimeException(response.body());
        }
    }

    public void deleteChallenge(String id) {
        HttpClient httpClient = HttpClientSingleton.getInstance();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(Endpoints.BASE_URL + Endpoints.injectStringIntoPath(Endpoints.DELETE_CHALLENGE, id)))
                .header("Content-Type", MediaType.APPLICATION_JSON)
                .DELETE()
                .build();
        HttpResponse<String> response = httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString()).join();
        if (response.statusCode() != 204) {
            throw new RuntimeException(response.body());
        }
    }
}


