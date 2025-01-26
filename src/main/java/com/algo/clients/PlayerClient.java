package com.algo.clients;

import com.algo.common.Endpoints;
import com.algo.common.HttpClientSingleton;
import com.algo.models.LoginRequest;
import com.algo.models.Player;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.MediaType;

import java.awt.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


@ApplicationScoped
public class PlayerClient {

    public Player signIn(LoginRequest loginRequest) {
        HttpClient httpClient = HttpClientSingleton.getInstance();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(Endpoints.BASE_URL + Endpoints.SIGN_IN))
                .header("Content-Type", MediaType.APPLICATION_JSON)
                .POST(HttpRequest.BodyPublishers.ofString(loginRequest.toJson()))
                .build();
        HttpResponse<String> response = httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString()).join();
        try {
            return Player.fromJson(response.body(), Player.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to sign in player", e);
        }

    }


}
