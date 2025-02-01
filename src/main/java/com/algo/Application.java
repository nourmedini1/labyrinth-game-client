package com.algo;

import java.util.concurrent.CompletableFuture;

import com.algo.clients.ChallengeClient;
import com.algo.clients.PlayerClient;
import com.algo.models.LoginRequest;
import com.algo.models.Player;

public class Application {

    public static void main(String[] args) {
        PlayerClient playerClient = new PlayerClient();
        ChallengeClient challengeClient = new ChallengeClient();

        // Example usage of PlayerClient
        LoginRequest loginRequest = new LoginRequest("karim");
        CompletableFuture<Player> signInFuture = playerClient.signIn(loginRequest);
        signInFuture.thenAccept(player -> {
            System.out.println("Signed in player: " + player.getName());
        }).exceptionally(ex -> {
            System.err.println("Failed to sign in: " + ex.getMessage());
            return null;
        });
        
    }
}
