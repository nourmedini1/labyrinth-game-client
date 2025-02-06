package com.algo.clients;

import com.algo.common.http.Endpoints;
import com.algo.common.singletons.HttpClientSingleton;
import com.algo.common.http.HttpHelper;
import com.algo.common.singletons.ObjectMapperSingleton;
import com.algo.models.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.MediaType;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class ChallengeClient {
    public Challenge createChallenge(CreateChallengeRequest request) {
        try {
            HttpClient httpClient = HttpClientSingleton.getInstance();
            ObjectMapper mapper = ObjectMapperSingleton.getInstance();

            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(Endpoints.BASE_URL + Endpoints.CREATE_CHALLENGE))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(mapper.writeValueAsString(request)))
                    .build();

            HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 201) {
                return mapper.readValue(response.body(), Challenge.class);
            }
            throw new RuntimeException("Creation failed: " + response.body());

        } catch (Exception e) {
            throw new RuntimeException("Challenge creation failed", e);
        }
    }

    public UpdateChallengeResponse updateChallenge(String challengeId, UpdateChallengeRequest updateRequest) {
        HttpClient httpClient = HttpClientSingleton.getInstance();

        try {
            // Generate a unique boundary
            String boundary = "Boundary-" + UUID.randomUUID();

            // Build multipart form data manually
            String formData = "--" + boundary + "\r\n" +
                    "Content-Disposition: form-data; name=\"challengerScore\"\r\n\r\n" +
                    updateRequest.getChallengerScore() + "\r\n" +
                    "--" + boundary + "\r\n" +
                    "Content-Disposition: form-data; name=\"challengedScore\"\r\n\r\n" +
                    updateRequest.getChallengedScore() + "\r\n" +
                    "--" + boundary + "--";

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(Endpoints.BASE_URL + Endpoints.UPDATE_CHALLENGE + "/" + challengeId))
                    .header("Content-Type", "multipart/form-data; boundary=" + boundary)
                    .PUT(HttpRequest.BodyPublishers.ofString(formData))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return UpdateChallengeResponse.fromJson(response.body(), UpdateChallengeResponse.class);
            }
            throw new RuntimeException("Update failed. Status: " + response.statusCode() + " Response: " + response.body());

        } catch (Exception e) {
            throw new RuntimeException("Challenge update failed", e);
        }
    }

    public List<Challenge> getChallenges(HashMap<String, Object> params) {
        HttpClient httpClient = HttpClientSingleton.getInstance();
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(Endpoints.BASE_URL + Endpoints.GET_CHALLENGES + HttpHelper.createQueryString(params)))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                ObjectMapper objectMapper = ObjectMapperSingleton.getInstance();
                JsonNode rootNode = objectMapper.readTree(response.body());
                JsonNode entitiesNode = rootNode.get("entities");

                return objectMapper.readValue(
                        entitiesNode.traverse(),
                        objectMapper.getTypeFactory().constructCollectionType(List.class, Challenge.class)
                );
            }
            throw new RuntimeException("Request failed: " + response.body());

        } catch (Exception e) {
            throw new RuntimeException("Error getting challenges", e);
        }
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


