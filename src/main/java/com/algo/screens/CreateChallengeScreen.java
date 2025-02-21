package com.algo.screens;

import com.algo.clients.ChallengeClient;
import com.algo.clients.LabyrinthClient;
import com.algo.common.singletons.RedisClientSingleton;
import com.algo.common.trie.Trie;
import com.algo.models.*;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class CreateChallengeScreen {
    private static final List<String> THEMES = Arrays.asList(
            "ART", "GEOGRAPHY", "HISTORY", "LITERATURE",
            "MOVIES", "MUSIC", "POLITICS", "SCIENCE",
            "SPORTS", "TECHNOLOGY"
    );

    private static final Scanner scanner = new Scanner(System.in);

    public static void createChallenge() {
        try {
            // Get current player from Redis
            Player challenger = getCurrentPlayerFromRedis();

            // Select player to challenge
            Player challenged = PlayerSelectionScreen.selectPlayerToChallenge();
            if (challenged == null) {
                System.out.println("Challenge creation canceled.");
                return;
            }

            // Get challenge parameters
            int difficulty = getDifficultyLevel();
            String theme = selectTheme();

            // Create and send challenge
            CreateChallengeRequest request = buildRequest(challenger, challenged, difficulty, theme);
            Challenge createdChallenge = new ChallengeClient().createChallenge(request);

            System.out.println("\nChallenge created! ID: " + createdChallenge.getId());

            // Start the game
            startGame(createdChallenge);

        } catch (Exception e) {
            System.out.println("Challenge creation failed: " + e.getMessage());
        } finally {
            // Return to player screen
            PlayerScreen.displayMenu();
        }
    }

    private static Player getCurrentPlayerFromRedis() {
        String playerJson = RedisClientSingleton.getInstance().getData("player");
        if (playerJson == null || playerJson.isEmpty()) {
            throw new RuntimeException("No player signed in. Please sign in first.");
        }
        return Player.fromJson(playerJson, Player.class);
    }

    private static int getDifficultyLevel() {
        while (true) {
            System.out.print("\nEnter difficulty level (1-3): ");
            String input = scanner.nextLine();
            try {
                int level = Integer.parseInt(input);
                if (level >= 1 && level <= 3) {
                    return level;
                }
                System.out.println("Please enter a number between 1 and 3.");
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }

    private static String selectTheme() {
        System.out.println("\nAvailable themes:");
        for (int i = 0; i < THEMES.size(); i++) {
            System.out.printf("%2d. %s%n", i + 1, THEMES.get(i));
        }

        while (true) {
            System.out.print("Select theme (1-" + THEMES.size() + "): ");
            String input = scanner.nextLine();
            try {
                int choice = Integer.parseInt(input);
                if (choice >= 1 && choice <= THEMES.size()) {
                    return THEMES.get(choice - 1);
                }
                System.out.println("Please enter a number between 1 and " + THEMES.size());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }

    private static CreateChallengeRequest buildRequest(Player challenger, Player challenged,
                                                       int difficulty, String theme) {
        CreateChallengeRequest request = new CreateChallengeRequest();
        request.setChallengerId(challenger.getId());
        request.setChallengedId(challenged.getId());
        request.setDifficultyLevel(difficulty);
        request.setTheme(theme);
        return request;
    }

    private static void startGame(Challenge challenge) {
        try {
            System.out.println("\nStarting challenge...");
            Labyrinth labyrinth = new LabyrinthClient().getLabyrinth(challenge.getLabyrinthId());
            List<String> words = labyrinth.getWords();
            Trie trie = new Trie();
            for(String word : words){
                trie.insert(word);
            }

            GameScreen gameScreen= new GameScreen();
            gameScreen.gameLoop(labyrinth, trie);
            updateChallengeScore(challenge.getId());

        } catch (Exception e) {
            System.out.println("Error starting game: " + e.getMessage());
        }
    }

    private static  void updateChallengeScore(String challengeId){
        ChallengeClient challengeClient = new ChallengeClient();
        Player player= getCurrentPlayerFromRedis();

        // Create an UpdateChallengeRequest object
        UpdateChallengeRequest updateRequest = new UpdateChallengeRequest();
        updateRequest.setChallengerScore(player.getScore());


        // Call the updateChallenge method
        UpdateChallengeResponse response = challengeClient.updateChallenge(challengeId, updateRequest);

        // Print the response message
        System.out.println("Response Message: " + response.getMessage());
    }
}