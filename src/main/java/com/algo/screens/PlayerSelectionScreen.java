package com.algo.screens;

import com.algo.clients.PlayerClient;
import com.algo.common.singletons.RedisClientSingleton;
import com.algo.models.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PlayerSelectionScreen {
    private static final Scanner scanner = new Scanner(System.in);
    private static final int PAGE_SIZE = 5;

    public static void displayLeaderboard() {
        int currentPage = 0;
        List<Player> players;

        do {
            players = fetchPlayers(currentPage);
            if (players == null || players.isEmpty()) return;

            System.out.println("\n=== Leaderboard (Page " + (currentPage + 1) + ") ===");
            displayPlayers(players);

            System.out.println("\n[n] Next Page | [p] Previous Page | [b] Back");
            System.out.print("Choose option: ");
            String input = scanner.nextLine().trim().toLowerCase();

            if (input.equals("n")) {
                currentPage++;
            } else if (input.equals("p") && currentPage > 0) {
                currentPage--;
            } else if (input.equals("b")) {
                return;
            } else {
                System.out.println("Invalid input!");
            }
        } while (true);
    }

    public static Player selectPlayerToChallenge() {
        int currentPage = 0;
        List<Player> players;
        Player currentPlayer = getCurrentPlayerFromRedis(); // Get current player

        do {
            players = fetchPlayers(currentPage);
            if (players == null || players.isEmpty()) return null;

            // Filter out current player
            List<Player> filteredPlayers = filterPlayers(players, currentPlayer);

            if (filteredPlayers.isEmpty()) {
                System.out.println("\n=== Page " + (currentPage + 1) + " ===");
                System.out.println("No other players on this page.");
            } else {
                System.out.println("\n=== Select Player to Challenge (Page " + (currentPage + 1) + ") ===");
                displayPlayers(filteredPlayers);
            }

            System.out.println("\n[n] Next Page | [p] Previous Page | [b] Back");
            System.out.print("Enter player number or command: ");
            String input = scanner.nextLine().trim().toLowerCase();

            if (input.equals("n")) {
                currentPage++;
            } else if (input.equals("p") && currentPage > 0) {
                currentPage--;
            } else if (input.equals("b")) {
                return null;
            } else {
                if (!filteredPlayers.isEmpty()) {
                    try {
                        int choice = Integer.parseInt(input);
                        if (choice >= 1 && choice <= filteredPlayers.size()) {
                            return filteredPlayers.get(choice - 1);
                        }
                        System.out.println("Invalid player number!");
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input!");
                    }
                } else {
                    System.out.println("Invalid input! No players to select.");
                }
            }
        } while (true);
    }

    // Helper method to get current player from Redis
    private static Player getCurrentPlayerFromRedis() {
        String playerJson = RedisClientSingleton.getInstance().getData("player");
        if (playerJson == null || playerJson.isEmpty()) {
            throw new RuntimeException("No player signed in. Please sign in first.");
        }
        return Player.fromJson(playerJson, Player.class);
    }

    // Helper method to filter out current player
    private static List<Player> filterPlayers(List<Player> players, Player currentPlayer) {
        List<Player> filtered = new ArrayList<>();
        for (Player p : players) {
            if (!p.getId().equals(currentPlayer.getId())) {
                filtered.add(p);
            }
        }
        return filtered;
    }

    private static List<Player> fetchPlayers(int page) {
        try {
            return new PlayerClient().getPlayersSorted(page, PAGE_SIZE);
        } catch (Exception e) {
            System.out.println("Error fetching players: " + e.getMessage());
            return null;
        }
    }

    private static void displayPlayers(List<Player> players) {
        System.out.println("Rank | Name\t\t| Score");
        System.out.println("--------------------------------");
        for (int i = 0; i < players.size(); i++) {
            Player p = players.get(i);
            System.out.printf("%2d. | %-15s | %d%n",
                    (i + 1), p.getName(), p.getScore());
        }
    }
}