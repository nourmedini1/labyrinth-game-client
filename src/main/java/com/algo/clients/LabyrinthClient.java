package com.algo.clients;

import com.algo.common.http.Endpoints;
import com.algo.common.singletons.HttpClientSingleton;
import com.algo.models.Labyrinth;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.MediaType;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@ApplicationScoped
public class LabyrinthClient {

    public Labyrinth getLabyrinth(String id) {
        HttpClient httpClient = HttpClientSingleton.getInstance();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(Endpoints.BASE_URL + Endpoints.injectStringIntoPath(Endpoints.GET_LABYRINTH, id)))
                .header("Content-Type", MediaType.APPLICATION_JSON)
                .GET()
                .build();
        HttpResponse<String> response = httpClient.sendAsync(request,  HttpResponse.BodyHandlers.ofString()).join();
        if (response.statusCode() == 200) {
            try {
                return Labyrinth.fromJson(response.body(), Labyrinth.class);
            } catch (Exception e) {
                throw new RuntimeException("Failed to parse response", e);
            }
        }
        throw new RuntimeException(response.body());
    }
}
