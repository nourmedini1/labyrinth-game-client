package com.algo.screens;

import com.algo.clients.PlayerClient;
import com.algo.models.LoginRequest;
import com.algo.models.Player;
import java.util.Scanner;

public class SignUp {

    public Player signUpPlayer() {
        System.out.print("=========================================");
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter your username: ");
        String username = scanner.nextLine();
        LoginRequest loginRequest = new LoginRequest(username);
        PlayerClient playerClient = new PlayerClient();
        try {
            Player player = playerClient.signUp(loginRequest);
            System.out.println("Signed up player: " + player.getName());
            return player;
        } catch (Exception e) {
            System.out.println("Error during sign-up: " + e.getMessage());
            return null;
        }
    }

}
