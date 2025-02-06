package com.algo;


import com.algo.common.singletons.RedisClientSingleton;
import com.algo.models.*;
import com.algo.screens.ViewChallengesScreen;

import static com.algo.screens.PlayerSelectionScreen.selectPlayerToChallenge;
import static com.algo.screens.Welcome.WelcomePlayer;


public class Application {



    public static void main(String[] args) {
        WelcomePlayer();
        System.out.println("Thank you for playing the Labyrinth Game!");
        RedisClientSingleton redisClient = RedisClientSingleton.getInstance();
        Player player = Player.fromJson(redisClient.getData("player"), Player.class);
        player=selectPlayerToChallenge();
        System.out.println(player.getName());
        System.out.println(player.getScore());
        System.out.println(player.getId());

//        ViewChallengesScreen.displayChallenges();


    }
}
