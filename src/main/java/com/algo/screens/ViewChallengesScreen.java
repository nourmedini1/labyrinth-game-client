package com.algo.screens;



import com.algo.clients.ChallengeClient;
import com.algo.clients.LabyrinthClient;
import com.algo.common.singletons.RedisClientSingleton;
import com.algo.common.trie.Trie;
import com.algo.models.*;

import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class ViewChallengesScreen {
    private static final Scanner scanner = new Scanner(System.in);
    private static final int PAGE_SIZE = 5;

    public static void displayChallenges() {
        int currentPage = 0;
        List<Challenge> challenges;
        ChallengeClient challengeClient = new ChallengeClient();
        String input;
        do {
            challenges = fetchChallenges(currentPage);
            if (challenges == null || challenges.isEmpty()){
                System.out.println("No challenges available Now");
                return;
            }

            displayChallengeList(challenges, currentPage);

            System.out.println("\n[n] Next Page | [p] Previous Page | [b] Back");
            System.out.print("Enter challenge number to view details: ");
            input = scanner.nextLine().trim().toLowerCase();

            if (handleNavigation(input, currentPage)) {
                currentPage = updateCurrentPage(input, currentPage);
            } else if (input.matches("\\d+")) {
                int choice = Integer.parseInt(input);
                if (choice >= 1 && choice <= challenges.size()) {
                    handleSelectedChallenge(challenges.get(choice - 1));
                } else {
                    System.out.println("Invalid challenge number!");
                }
            } else if (!input.equals("b")) {
                System.out.println("Invalid input!");
            }

        } while (!input.equals("b"));
    }

    private static List<Challenge> fetchChallenges(int page) {
        try {
            RedisClientSingleton redisClient = RedisClientSingleton.getInstance();
            Player player = Player.fromJson(redisClient.getData("player"), Player.class);
            HashMap<String, Object> params = new HashMap<>();
            params.put("page", page);
            params.put("size", PAGE_SIZE);
            params.put("status","PENDING");
            params.put("challengedId", player.getId());
            return new ChallengeClient().getChallenges(params);
        } catch (Exception e) {
            System.out.println("Error fetching challenges: " + e.getMessage());
            return null;
        }
    }

    private static void displayChallengeList(List<Challenge> challenges, int currentPage) {
        System.out.println("\n=== Challenges (Page " + (currentPage + 1) + ") ===");
        System.out.printf("%-3s | %-15s | %-10s%n", "#", "Theme", "Difficulty");
        System.out.println("-----------------------------------");
        for (int i = 0; i < challenges.size(); i++) {
            Challenge c = challenges.get(i);
            System.out.printf("%-3d | %-15s | %-10d%n",
                    (i + 1), c.getTheme(), c.getDifficultyLevel());
        }
    }

    private static void handleSelectedChallenge(Challenge challenge) {
        System.out.println("\n=== Challenge Details ===");
        System.out.println("ID: " + challenge.getId());
        System.out.println("Theme: " + challenge.getTheme());
        System.out.println("Difficulty: " + challenge.getDifficultyLevel());
        //System.out.println("Status: " + challenge.getStatus());
        System.out.println("Created: " + challenge.getCreatedAt());

        System.out.println("\n1. Accept Challenge");
        System.out.println("2. Decline Challenge");
        System.out.println("3. Back to List");
        System.out.print("Choose option: ");

        String choice = scanner.nextLine();
        switch (choice) {
            case "1":
                acceptChallenge(challenge);
                break;
            case "2":
                declineChallenge(challenge);
                break;
            case "3":
                break;
            default:
                System.out.println("Invalid option!");
        }
    }

    private static void acceptChallenge(Challenge challenge) {
        try {
            // Update challenge status
            Challenge acceptedChallenge = new ChallengeClient().acceptChallenge(challenge.getId());

            // Start the game
            Labyrinth labyrinth = new LabyrinthClient().getLabyrinth(challenge.getLabyrinthId());
            List<String> words = labyrinth.getWords();
            Trie trie = new Trie();
            for(String word : words){
                trie.insert(word);
            }
            GameScreen gameScreen=new GameScreen();
            gameScreen.gameLoop(labyrinth ,trie, challenge.getDifficultyLevel());

            updateChallengeScore(challenge.getId());


        } catch (Exception e) {
            System.out.println("Error accepting challenge: " + e.getMessage());
        }
    }



    private static void declineChallenge(Challenge challenge) {
        try {
            new ChallengeClient().declineChallenge(challenge.getId());
            System.out.println("Challenge declined successfully.");
        } catch (Exception e) {
            System.out.println("Error declining challenge: " + e.getMessage());
        }
    }

    private static boolean handleNavigation(String input, int currentPage) {
        return input.equals("n") || (input.equals("p") && currentPage > 0);
    }

    private static int updateCurrentPage(String input, int currentPage) {
        return input.equals("n") ? currentPage + 1 : currentPage - 1;
    }

    private static  void updateChallengeScore(String challengeId){
        ChallengeClient challengeClient = new ChallengeClient();
        Player player= getCurrentPlayerFromRedis();

        // Create an UpdateChallengeRequest object
        UpdateChallengeRequest updateRequest = new UpdateChallengeRequest();
        updateRequest.setChallengedScore(player.getScore());


        // Call the updateChallenge method
        UpdateChallengeResponse response = challengeClient.updateChallenge(challengeId, updateRequest);

        // Print the response message
        System.out.println("Response Message: " + response.getMessage());
    }

    private static Player getCurrentPlayerFromRedis() {
        String playerJson = RedisClientSingleton.getInstance().getData("player");
        if (playerJson == null || playerJson.isEmpty()) {
            throw new RuntimeException("No player signed in. Please sign in first.");
        }
        return Player.fromJson(playerJson, Player.class);
    }

}
