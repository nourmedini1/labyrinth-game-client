package com.algo.screens;

import com.algo.clients.PlayerClient;
import com.algo.models.LoginRequest;
import com.algo.models.Player;
import java.util.Scanner;

public class SignUp {

    public static void signUpPlayer(Player player) {
        System.out.println("=========================================");
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your username: ");
        String username = scanner.nextLine();
        LoginRequest loginRequest = new LoginRequest(username);
        PlayerClient playerClient = new PlayerClient();
        try {
            player = playerClient.signUp(loginRequest);
            System.out.println("Signed up player: " + player.getName());

        } catch (Exception e) {
            System.out.println("Error during sign-up: " + e.getMessage());

        }
    }

}
