package com.algo.screens;

import com.algo.models.Player;

import java.util.Scanner;

import static com.algo.screens.SignIn.signInPlayer;
import static com.algo.screens.SignUp.signUpPlayer;

public class Welcome {
    public static void WelcomePlayer(){

        Scanner scanner = new Scanner(System.in);
        System.out.println("===================================");
        System.out.println("Welcome to the Labyrinth Game!");
        System.out.println("===================================");
        System.out.println("1. Sign In");
        System.out.println("2. Sign Up");
        System.out.println("===================================");
        System.out.println("Choose an option (1 or 2): ");


        String choice = scanner.nextLine();

        switch (choice) {
            case "1":
                signInPlayer();
                break;
            case "2":
                signUpPlayer();
                break;
            default:
                System.out.println("Invalid option. Please try again.");
                WelcomePlayer();
        }
    }







    public static void displayLogo(){

        //the ASCII art:
        String asciiArt = """
               \s
                               ▗▖    ▗▄▖ ▗▄▄▖▗▖  ▗▖▗▄▄▖ ▗▄▄▄▖▗▖  ▗▖▗▄▄▄▖▗▖ ▗▖
                               ▐▌   ▐▌ ▐▌▐▌ ▐▌▝▚▞▘ ▐▌ ▐▌  █  ▐▛▚▖▐▌  █  ▐▌ ▐▌
                               ▐▌   ▐▛▀▜▌▐▛▀▚▖ ▐▌  ▐▛▀▚▖  █  ▐▌ ▝▜▌  █  ▐▛▀▜▌
                               ▐▙▄▄▖▐▌ ▐▌▐▙▄▞▘ ▐▌  ▐▌ ▐▌▗▄█▄▖▐▌  ▐▌  █  ▐▌ ▐▌
                                                                            \s
                                                                            \s
                                                                            \s
               \s
       \s""";

        String blueColor = "\u001B[34m"; // ANSI escape code for blue
        String resetColor = "\u001B[0m"; // Reset color


        // Print the ASCII art

        System.out.println(blueColor+asciiArt+resetColor);




    }
}
