package com.algo.screens;

import com.algo.clients.PlayerClient;
import com.algo.models.LoginRequest;
import com.algo.models.Player;
import java.util.Scanner;

public class  SignIn {

    public static void signInPlayer(Player player) {
        System.out.print("=========================================");
        System.out.println("Enter your username to sign in.");
        System.out.print("=========================================");
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter your username: ");
        String username = scanner.nextLine();
        LoginRequest loginRequest = new LoginRequest(username);
        PlayerClient playerClient = new PlayerClient();
        try {
            player = playerClient.signIn(loginRequest);
            System.out.println("Signed in as: " + player.getName());

        } catch (Exception e) {
            System.out.println("Error during sign-in: " + e.getMessage());

        }
    }

}