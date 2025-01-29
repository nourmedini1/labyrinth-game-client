package com.algo.common;

public class Logo {


    public static void displayLogo(){

        //the ASCII art:
        String asciiArt = """
                
                               ▗▖    ▗▄▖ ▗▄▄▖▗▖  ▗▖▗▄▄▖ ▗▄▄▄▖▗▖  ▗▖▗▄▄▄▖▗▖ ▗▖
                               ▐▌   ▐▌ ▐▌▐▌ ▐▌▝▚▞▘ ▐▌ ▐▌  █  ▐▛▚▖▐▌  █  ▐▌ ▐▌
                               ▐▌   ▐▛▀▜▌▐▛▀▚▖ ▐▌  ▐▛▀▚▖  █  ▐▌ ▝▜▌  █  ▐▛▀▜▌
                               ▐▙▄▄▖▐▌ ▐▌▐▙▄▞▘ ▐▌  ▐▌ ▐▌▗▄█▄▖▐▌  ▐▌  █  ▐▌ ▐▌
                                                                            \s
                                                                            \s
                                                                            \s
                
        """;

        String blueColor = "\u001B[34m"; // ANSI escape code for blue
        String resetColor = "\u001B[0m"; // Reset color


        // Print the ASCII art

        System.out.println(blueColor+asciiArt+resetColor);




    }

}
