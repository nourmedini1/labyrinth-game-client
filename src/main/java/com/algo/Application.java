package com.algo;

import com.algo.models.Player;

import static com.algo.screens.Welcome.WelcomePlayer;
import static com.algo.screens.Welcome.displayLogo;

public class Application {

    public static void main(String[] args) {
        displayLogo();
        Player player= new Player();
        WelcomePlayer(player);

    }
}
