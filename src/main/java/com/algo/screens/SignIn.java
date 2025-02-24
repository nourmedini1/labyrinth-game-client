package com.algo.screens;

import com.algo.clients.PlayerClient;
import com.algo.common.singletons.RedisClientSingleton;
import com.algo.models.LoginRequest;
import com.algo.models.Player;
import java.util.Scanner;

public class  SignIn {

    public static void signInPlayer() {
        System.out.println("=========================================");
        System.out.println("Enter your username to sign in.");
        System.out.println("=========================================");
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your username: ");
        String username = scanner.nextLine();
        LoginRequest loginRequest = new LoginRequest(username);
        PlayerClient playerClient = new PlayerClient();
        try {
            Player player = playerClient.signIn(loginRequest);
            RedisClientSingleton redisClient = RedisClientSingleton.getInstance();
            redisClient.saveData("player", player.toJson());
            System.out.println("Signed in as: " + player.getName());

        } catch (Exception e) {
            System.out.println("Error during sign-in: " + e.getMessage());

        }


    }

}