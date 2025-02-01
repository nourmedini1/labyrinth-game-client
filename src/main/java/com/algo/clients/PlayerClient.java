package com.algo.clients;

import com.algo.common.Endpoints;
import com.algo.common.HttpClientSingleton;
import com.algo.common.HttpHelper;
import com.algo.common.ObjectMapperSingleton;
import com.algo.models.LoginRequest;
import com.algo.models.Player;
import com.algo.models.UpdatePlayerRequest;
import com.fasterxml.jackson.databind.JsonNode;
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
public class PlayerClient {

    public Player signIn(LoginRequest loginRequest) {
        HttpClient httpClient = HttpClientSingleton.getInstance();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(Endpoints.BASE_URL + Endpoints.SIGN_IN))
                .header("Content-Type", MediaType.APPLICATION_JSON)
                .POST(HttpRequest.BodyPublishers.ofString(loginRequest.toJson()))
                .build();
        return getPlayerResponse(httpClient, request);
    }

    public Player signUp(LoginRequest loginRequest) {
        HttpClient httpClient = HttpClientSingleton.getInstance();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(Endpoints.BASE_URL + Endpoints.SIGN_UP))
                .header("Content-Type", MediaType.APPLICATION_JSON)
                .POST(HttpRequest.BodyPublishers.ofString(loginRequest.toJson()))
                .build();
        HttpResponse<String> response = httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString()).join();
        if (response.statusCode() == 201) {
            try {
                return Player.fromJson(response.body(), Player.class);
            } catch (Exception e) {
                throw new RuntimeException("Failed to parse response", e);
            }
        }
        throw new RuntimeException(response.body());
    }

    public Player getPlayer(String id) {
        HttpClient httpClient = HttpClientSingleton.getInstance();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(Endpoints.BASE_URL + Endpoints.injectStringIntoPath(Endpoints.GET_PLAYER, id)))
                .header("Content-Type", MediaType.APPLICATION_JSON)
                .GET()
                .build();
        return getPlayerResponse(httpClient, request);
    }

    public Player getPlayerByName(String name) {
        HttpClient httpClient = HttpClientSingleton.getInstance();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(Endpoints.BASE_URL + Endpoints.injectStringIntoPath(Endpoints.GET_PLAYER_BY_NAME, name)))
                .header("Content-Type", MediaType.APPLICATION_JSON)
                .GET()
                .build();
        return getPlayerResponse(httpClient, request);
    }

    public Player updatePlayer(String id,UpdatePlayerRequest updatePlayerRequest) {
        HttpClient httpClient = HttpClientSingleton.getInstance();
        HashMap<String, Object> params = new HashMap<>();
        params.put("name", updatePlayerRequest.getName());
        params.put("score", updatePlayerRequest.getScore());
        String formData =  HttpHelper.createFormUrlEncodedBody(params);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(Endpoints.BASE_URL + Endpoints.injectStringIntoPath(Endpoints.UPDATE_PLAYER, id)))
                .header("Content-Type", MediaType.MULTIPART_FORM_DATA)
                .PUT(HttpRequest.BodyPublishers.ofString(formData))
                .build();
        return getPlayerResponse(httpClient, request);
    }

    public void deletePlayer(String id) {
        HttpClient httpClient = HttpClientSingleton.getInstance();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(Endpoints.BASE_URL + Endpoints.injectStringIntoPath(Endpoints.DELETE_PLAYER, id)))
                .header("Content-Type", MediaType.APPLICATION_JSON)
                .DELETE()
                .build();
        HttpResponse<String> response = httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString()).join();
        if (response.statusCode() != 204) {
            throw new RuntimeException(response.body());
        }
    }



    public List<Player> getPlayersSorted(int page, int size) throws Exception {
        HttpClient httpClient = HttpClientSingleton.getInstance();
        String url = Endpoints.BASE_URL + Endpoints.GET_PLAYERS_SORTED +
                "?page=" + page + "&size=" + size;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            try {
                ObjectMapper objectMapper = ObjectMapperSingleton.getInstance();
                JsonNode rootNode = objectMapper.readTree(response.body());
                JsonNode entitiesNode = rootNode.get("entities");

                List<Player> players = new ArrayList<>();
                if (entitiesNode != null && entitiesNode.isArray()) {
                    for (JsonNode playerNode : entitiesNode) {
                        Player player = objectMapper.treeToValue(playerNode, Player.class);
                        players.add(player);
                    }
                }
                return players;
            } catch (Exception e) {
                throw new RuntimeException("Failed to parse response", e);
            }
        }
        throw new RuntimeException(response.body());
    }



    private Player getPlayerResponse(HttpClient httpClient, HttpRequest request) {
        HttpResponse<String> response = httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString()).join();
        if (response.statusCode() == 200) {
            try {
                return Player.fromJson(response.body(), Player.class);
            } catch (Exception e) {
                throw new RuntimeException("Failed to parse response", e);
            }
        }
        throw new RuntimeException(response.body());
    }





}
