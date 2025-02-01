package com.algo.clients;

import com.algo.common.Endpoints;
import com.algo.common.HttpClientSingleton;
import com.algo.common.HttpHelper;
import com.algo.common.ObjectMapperSingleton;
import com.algo.models.LoginRequest;
import com.algo.models.Player;
import com.algo.models.UpdatePlayerRequest;
import jakarta.ws.rs.core.MediaType;


import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;


public class PlayerClient {

    public CompletableFuture<Player> signIn(LoginRequest loginRequest) {
        HttpClient httpClient = HttpClientSingleton.getInstance();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(Endpoints.BASE_URL + Endpoints.SIGN_IN))
                .header("Content-Type", MediaType.APPLICATION_JSON)
                .POST(HttpRequest.BodyPublishers.ofString(loginRequest.toJson()))
                .build();
        return getPlayerResponse(httpClient, request);
    }

    public CompletableFuture<Player> signUp(LoginRequest loginRequest) {
        HttpClient httpClient = HttpClientSingleton.getInstance();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(Endpoints.BASE_URL + Endpoints.SIGN_UP))
                .header("Content-Type", MediaType.APPLICATION_JSON)
                .POST(HttpRequest.BodyPublishers.ofString(loginRequest.toJson()))
                .build();
        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString()).thenApply(
                response -> {
                    if (response.statusCode() == 201) {
                        try {
                            return Player.fromJson(response.body(), Player.class);
                        } catch (Exception e) {
                            throw new RuntimeException("Failed to parse response", e);
                        }
                    }
                    throw new RuntimeException(response.body());
                }
        );
    }

    public CompletableFuture<Player> getPlayer(String id) {
        HttpClient httpClient = HttpClientSingleton.getInstance();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(Endpoints.BASE_URL + Endpoints.injectStringIntoPath(Endpoints.GET_PLAYER, id)))
                .header("Content-Type", MediaType.APPLICATION_JSON)
                .GET()
                .build();
        return getPlayerResponse(httpClient, request);
    }

    public CompletableFuture<Player> getPlayerByName(String name) {
        HttpClient httpClient = HttpClientSingleton.getInstance();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(Endpoints.BASE_URL + Endpoints.injectStringIntoPath(Endpoints.GET_PLAYER_BY_NAME, name)))
                .header("Content-Type", MediaType.APPLICATION_JSON)
                .GET()
                .build();
        return getPlayerResponse(httpClient, request);
    }

    public CompletableFuture<Player> updatePlayer(String id, UpdatePlayerRequest updatePlayerRequest) {
        HttpClient httpClient = HttpClientSingleton.getInstance();
        HashMap<String, Object> params = new HashMap<>();
        params.put("name", updatePlayerRequest.getName());
        params.put("score", updatePlayerRequest.getScore());
        String formData =  HttpHelper.createFormUrlEncodedBody(params);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(Endpoints.BASE_URL + Endpoints.injectStringIntoPath(Endpoints.UPDATE_PLAYER, id)))
                .header("Content-Type", "multipart/form-data; boundary=---boundary")
                .PUT(buildFormData(updatePlayerRequest))
                .build();
        return getPlayerResponse(httpClient, request);
    }

    public CompletableFuture<Boolean> deletePlayer(String id) {
        HttpClient httpClient = HttpClientSingleton.getInstance();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(Endpoints.BASE_URL + Endpoints.injectStringIntoPath(Endpoints.DELETE_PLAYER, id)))
                .header("Content-Type", MediaType.APPLICATION_JSON)
                .DELETE()
                .build();
        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString()).thenApply(
                response -> {
                    if (response.statusCode() == 204) {
                        return true;
                    }
                    throw new RuntimeException(response.body());
                }
        );
    }

    public CompletableFuture<List<Player>> getPlayersSorted() {
        HttpClient httpClient = HttpClientSingleton.getInstance();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(Endpoints.BASE_URL + Endpoints.GET_PLAYERS_SORTED))
                .header("Content-Type", MediaType.APPLICATION_JSON)
                .GET()
                .build();
        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString()).thenApply(
                response -> {
                    if (response.statusCode() == 200) {
                        try {
                            List<String> playerJsonList = ObjectMapperSingleton.getInstance().readValue(response.body(), List.class);
                            List<Player> players = new ArrayList<>();
                            for (String playerJson : playerJsonList) {
                                Player player = Player.fromJson(playerJson, Player.class);
                                players.add(player);
                            }
                            return players;

                        } catch (Exception e) {
                            throw new RuntimeException("Failed to parse response", e);
                        }
                    }
                    throw new RuntimeException(response.body());
                }
        );
    }
    private CompletableFuture<Player> getPlayerResponse(HttpClient httpClient, HttpRequest request) {
        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString()).thenApply(
                response -> {
                    if (response.statusCode() == 200) {
                        try {
                            return Player.fromJson(response.body(), Player.class);
                        } catch (Exception e) {
                            throw new RuntimeException("Failed to parse response", e);
                        }
                    }
                    throw new RuntimeException(response.body());
                }
        );
    }

    private HttpRequest.BodyPublisher buildFormData(UpdatePlayerRequest updatePlayerRequest) {
        List<byte[]> byteArrays = new ArrayList<>();
        if (updatePlayerRequest.getName() != null) {
            byteArrays.add(("--boundary\r\nContent-Disposition: form-data; name=\"name\"\r\n\r\n" + updatePlayerRequest.getName() + "\r\n").getBytes());
        }
        if (updatePlayerRequest.getScore() != 0) {
            byteArrays.add(("--boundary\r\nContent-Disposition: form-data; name=\"score\"\r\n\r\n" + updatePlayerRequest.getScore() + "\r\n").getBytes());
        }
        byteArrays.add("\r\n--boundary--\r\n".getBytes());
        return HttpRequest.BodyPublishers.ofByteArrays(byteArrays);
    }





}
