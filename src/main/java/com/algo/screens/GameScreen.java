package com.algo.screens;
import com.algo.clients.PlayerClient;
import com.algo.common.singletons.RedisClientSingleton;
import com.algo.models.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static com.algo.common.ui.ClearConsole.clearConsole;
import static com.algo.common.ui.Color.*;
import static com.algo.common.ui.Logo.displayLogo;
import static com.algo.screens.PlayerScreen.displayMenu;
import  com.algo.common.trie.Trie;

public class GameScreen {

    public void gameLoop(Labyrinth labyrinth, Trie trie, int difficultyLevel) {
        String fullPath = "";
        int currentScore = 0;
        Coordinates playerPosition = labyrinth.getStart();
        List<Coordinates> playerPath = new ArrayList<>();
        playerPath.add(new Coordinates(playerPosition.getX(), playerPosition.getY()));
        Scanner scanner = new Scanner(System.in);
        RedisClientSingleton redisClient = RedisClientSingleton.getInstance();
        Player player = Player.fromJson(redisClient.getData("player"), Player.class);

        // Step limit calculation
        List<Coordinates> shortestPath = labyrinth.getShortestPath();
        int stepLimit = (shortestPath != null && !shortestPath.isEmpty())
                ? (shortestPath.size() - 1) + 10
                : 20;

        int stepsTaken = 0;
        boolean gameWon = false;
        boolean exitedViaMenu = false;

        while(true) {
            clearConsole();
            displayLogo();
            displayPlayerInfo(
                    player.getName(),
                    currentScore,
                    stepLimit - stepsTaken
            );
            displayLabyrinth(labyrinth, playerPosition);
            System.out.println('\n');


            Coordinates newPosition = getNextPosition(scanner, playerPosition);

            // Handle menu exit
            if (newPosition == null) {
                exitedViaMenu = true;
                break;
            }

            if (isValidMove(newPosition, labyrinth)) {
                playerPosition = new Coordinates(newPosition.getX(), newPosition.getY());
                playerPath.add(playerPosition);
                stepsTaken++;
                fullPath += labyrinth.getNodes().get(playerPosition.getX()).get(playerPosition.getY()).getValue();
                currentScore += trie.containsSubword(fullPath);
                if (stepsTaken > stepLimit) {
                    clearConsole();
                    displayLabyrinth(labyrinth, playerPosition);
                    System.out.println(RED + "Out of steps! Game Over." + RESET);
                    System.out.println("Maximum allowed steps: " + stepLimit);
                    break;
                }

                if (playerPosition.equals(labyrinth.getEnd())) {
                    gameWon = true;
                    player.setScore(player.getScore() + currentScore*difficultyLevel);
                    break;
                }
            } else {
                System.out.println(RED + "Invalid move! Try again." + RESET);
            }
        }



        if (exitedViaMenu) {
            handleMenuExit(player);
        } else if (gameWon) {
            handleGameCompletion(labyrinth, player, playerPath, stepsTaken);
        } else {
            handleStepLimitLoss(player, stepsTaken);
        }
        //savePlayerProgress(player);
    }

    private void handleMenuExit(Player player) {
        int penalty = 5;
        player.setScore(player.getScore() - penalty);
        savePlayerProgress(player);
        System.out.println(RED + "\nExited to menu. " + penalty + " points deducted!" + RESET);

    }

    private void handleStepLimitLoss(Player player, int stepsTaken) {
//        player.setScore(player.getScore() - stepsTaken);
        player.setScore(player.getScore() - 1);
        savePlayerProgress(player);

    }

    private Coordinates getNextPosition(Scanner scanner, Coordinates currentPosition) {
        System.out.print("Move using (Z/Q/S/D) or B to go back: ");
        String input = scanner.nextLine().toLowerCase();

        if (input.equals("b")) {
            return null;
        }

        Coordinates newPosition = new Coordinates(currentPosition.getX(), currentPosition.getY());
        switch (input) {
            case "z": // Up
                newPosition.setX(newPosition.getX() - 1);
                break;
            case "s": // Down
                newPosition.setX(newPosition.getX() + 1);
                break;
            case "q": // Left
                newPosition.setY(newPosition.getY() - 1);
                break;
            case "d": // Right
                newPosition.setY(newPosition.getY() + 1);
                break;
            default:
                System.out.println("Invalid input. Use Z/Q/S/D to move or B to exit.");
                break;
        }
        return newPosition;
    }

    private void handleGameCompletion(Labyrinth labyrinth, Player player,
                                      List<Coordinates> playerPath, int stepsTaken) {
        clearConsole();
        displayLabyrinth(labyrinth, labyrinth.getEnd());

        // Calculate bonus
        boolean bonusEarned = false;
        List<Coordinates> shortestPath = labyrinth.getShortestPath();
        if (shortestPath != null && !shortestPath.isEmpty()) {
            int shortestLength = shortestPath.size() - 1;

            if (stepsTaken == shortestLength) {
                player.setScore(player.getScore() + 10);
                bonusEarned = true;
            }
        }

        System.out.println(GREEN + "\nCongratulations! You've reached the end!" + RESET);
//        System.out.println("Your path (" + stepsTaken + " steps):");
//        displayPath(playerPath);

        if (bonusEarned) {
            System.out.println(YELLOW + "\nBonus! You took the shortest path! +10 points" + RESET);
        }
        System.out.println("score= "+player.getScore());



        savePlayerProgress(player);
    }

    private void displayPlayerInfo(String playerName, int playerScore, int remainingSteps) {
        System.out.println("===================================");
        System.out.printf("Player: %s | Score: %d | Steps Left: %d%n",
                playerName, playerScore, remainingSteps);
        System.out.println("===================================");
    }

//    private void displayPath(List<Coordinates> path) {
//        for (int i = 0; i < path.size(); i++) {
//            Coordinates coord = path.get(i);
//            System.out.printf("[%d] (%d,%d)%s",
//                    i, coord.getX(), coord.getY(),
//                    (i < path.size()-1) ? " → " : "\n");
//        }
//    }

    private void savePlayerProgress(Player player) {
        try {
            // Save to Redis
            RedisClientSingleton.getInstance().saveData("player", player.toJson());

            // Update player in backend
            PlayerClient playerClient = new PlayerClient();
            UpdatePlayerRequest updateRequest = new UpdatePlayerRequest();
            updateRequest.setName(player.getName());
            updateRequest.setScore(player.getScore());

//            System.out.println("Sending update request for player ID: " + player.getId());
//            System.out.println("Name: " + player.getName());
//            System.out.println("Score: " + player.getScore());

            Player updatedPlayer = playerClient.updatePlayer(player.getId(), updateRequest);
//
//            System.out.println("Update successful. New score: " + updatedPlayer.getScore());
//
//            System.out.println(CYAN + "\nProgress saved successfully!" + RESET);
        } catch (Exception e) {
            System.out.println(RED + "\nError saving progress: " + e.getMessage() + RESET);
            e.printStackTrace();
        }
    }


    private void displayPlayerInfo(String playerName, int playerScore) {
        System.out.println("===================================");
        System.out.println("Player: " + playerName + " | Score: " + playerScore);
        System.out.println("===================================");
    }


    private boolean isValidMove(Coordinates position, Labyrinth labyrinth) {
        int x = position.getX();
        int y = position.getY();

        // Check if within bounds
        if (y < 0 || y >= labyrinth.getWidth() || x< 0 || x >= labyrinth.getHeight()) {
            return false;
        }

        // Check if the node is not a wall
        return !labyrinth.getNodes().get(x).get(y).isWall();
    }

    private void displayLabyrinth(Labyrinth labyrinth, Coordinates playerPosition) {
        List<List<Node>> nodes = labyrinth.getNodes();
        Coordinates start = labyrinth.getStart();
        Coordinates end = labyrinth.getEnd();


        for (int y = 0; y < labyrinth.getHeight(); y++) {
            for (int x = 0; x < labyrinth.getWidth(); x++) {
                Node node = nodes.get(y).get(x);

                if (playerPosition.getY() == x && playerPosition.getX() == y) {
                    // Player's current position
                    System.out.print(BG_BLUE + node.getValue() + RESET + " ");
                } else if (start.getY() == x && start.getX() == y) {
                    // Start position
                    System.out.print(BG_GREEN + node.getValue() + RESET + " ");
                } else if (end.getY() == x && end.getX() == y) {
                    // End position
                    System.out.print(BG_YELLOW + node.getValue() + RESET + " ");
                } else {
                    // Regular node
                    System.out.print(node.getValue() + " ");
                }
            }
            System.out.println();
        }
    }




}
