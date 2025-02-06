package com.algo.screens;

import com.algo.clients.ChallengeClient;
import com.algo.common.singletons.RedisClientSingleton;
import com.algo.models.Player;
import java.util.Scanner;


import com.algo.models.Player;

import java.util.Scanner;

public class PlayerScreen {
    private static final Scanner scanner = new Scanner(System.in);

    public static void displayMenu() {
        Player currentPlayer = getCurrentPlayerFromRedis();
        if (currentPlayer == null) return;

        while (true) {
            System.out.println("\n=== Player Menu ===");
            System.out.println("Welcome, " + currentPlayer.getName() + "!");
            System.out.println("1. View Leaderboard");
            System.out.println("2. View My Challenges");
            System.out.println("3. Create New Challenge");
            System.out.println("4. Exit to Main Menu");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    PlayerSelectionScreen.displayLeaderboard();
                    break;
                case "2":
                    ViewChallengesScreen.displayChallenges();
                    break;
                case "3":
                    CreateChallengeScreen.createChallenge();
                    // Refresh player data after challenge creation
                    currentPlayer = getCurrentPlayerFromRedis();
                    break;
                case "4":
                    System.out.println("Returning to main menu...");
                    return;
                default:
                    System.out.println("Invalid option! Please try again.");
            }
        }


    }

    private static Player getCurrentPlayerFromRedis() {
        String playerJson = RedisClientSingleton.getInstance().getData("player");
        if (playerJson == null || playerJson.isEmpty()) {
            throw new RuntimeException("No player signed in. Please sign in first.");
        }
        return Player.fromJson(playerJson, Player.class);
    }

}

