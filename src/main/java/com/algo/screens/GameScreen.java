package com.algo.screens;
import com.algo.models.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static com.algo.common.ClearConsole.clearConsole;
import static com.algo.common.Color.*;
import static com.algo.common.Logo.displayLogo;

public class GameScreen {


    public void gameLoop(Labyrinth labyrinth, Player player) {


        Coordinates playerPosition=labyrinth.getStart();



        List<Coordinates> playerPath = new ArrayList<>();
        playerPath.add(new Coordinates(playerPosition.getX(), playerPosition.getY()));
        Scanner scanner = new Scanner(System.in);


        while(true){


            clearConsole(); // Clear the console
            displayLogo();

            displayPlayerInfo(player.getName(), player.getScore());

            displayLabyrinth(labyrinth, playerPosition);

            Coordinates newPosition = getNextPosition(scanner, playerPosition);

            if (isValidMove(newPosition, labyrinth)) {
                playerPosition.setX(newPosition.getX());
                playerPosition.setY(newPosition.getY());
                playerPath.add(new Coordinates(playerPosition.getX(), playerPosition.getY()));
                player.setScore(player.getScore()+1);

                // Check win condition
                if (playerPosition.getX() == labyrinth.getEnd().getX() &&
                        playerPosition.getY() == labyrinth.getEnd().getY()) {
                    clearConsole();
                    displayLabyrinth(labyrinth, playerPosition);
                    System.out.println("Congratulations! You've reached the end!");
                    System.out.println("Your path: " + playerPath);
                    break;
                }
            } else {
                System.out.println("Invalid move! Try again.");
            }
        }


        scanner.close();


    }


    private void displayPlayerInfo(String playerName, int playerScore) {
        System.out.println("===================================");
        System.out.println("Player: " + playerName + " | Score: " + playerScore);
        System.out.println("===================================");
    }

    private Coordinates getNextPosition(Scanner scanner, Coordinates currentPosition) {
        // Get player input
        System.out.print("Move using (Z/Q/S/D): ");
        String input = scanner.nextLine();

        // Calculate the new position based on input
        Coordinates newPosition = new Coordinates(currentPosition.getX(), currentPosition.getY());
        switch (input.toLowerCase()) {
            case "z": // Move up
                newPosition.setY(newPosition.getY() - 1);
                break;
            case "s": // Move down
                newPosition.setY(newPosition.getY() + 1);
                break;
            case "q": // Move left
                newPosition.setX(newPosition.getX() - 1);
                break;
            case "d": // Move right
                newPosition.setX(newPosition.getX() + 1);
                break;
            default:
                System.out.println("Invalid input. Use Z/Q/S/D to move.");
                break;
        }
        return newPosition;
    }

    private boolean isValidMove(Coordinates position, Labyrinth labyrinth) {
        int x = position.getX();
        int y = position.getY();

        // Check if within bounds
        if (x < 0 || x >= labyrinth.getWidth() || y < 0 || y >= labyrinth.getHeight()) {
            return false;
        }

        // Check if the node is not a wall
        return !labyrinth.getNodes().get(y).get(x).isWall();
    }

    private void displayLabyrinth(Labyrinth labyrinth, Coordinates playerPosition) {
        List<List<Node>> nodes = labyrinth.getNodes();
        Coordinates start = labyrinth.getStart();
        Coordinates end = labyrinth.getEnd();

        for (int y = 0; y < labyrinth.getHeight(); y++) {
            for (int x = 0; x < labyrinth.getWidth(); x++) {
                Node node = nodes.get(y).get(x);

                if (playerPosition.getX() == x && playerPosition.getY() == y) {
                    // Player's current position
                    System.out.print(BG_BLUE + "P" + RESET + " ");
                } else if (start.getX() == x && start.getY() == y) {
                    // Start position
                    System.out.print(BG_GREEN + node.getValue() + RESET + " ");
                } else if (end.getX() == x && end.getY() == y) {
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
