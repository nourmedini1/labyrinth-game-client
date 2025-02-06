package com.algo;


import com.algo.clients.LabyrinthClient;
import com.algo.common.singletons.RedisClientSingleton;
import com.algo.models.*;
import com.algo.screens.CreateChallengeScreen;
import com.algo.screens.GameScreen;
import com.algo.screens.ViewChallengesScreen;

import static com.algo.screens.CreateChallengeScreen.createChallenge;
import static com.algo.screens.PlayerScreen.displayMenu;
import static com.algo.screens.PlayerSelectionScreen.selectPlayerToChallenge;
import static com.algo.screens.Welcome.WelcomePlayer;


public class Application {



    public static void main(String[] args) {
//        LabyrinthClient labyrinthClient= new LabyrinthClient();
//        GameScreen gameScreen= new GameScreen();
//
//        Labyrinth labyrinth= labyrinthClient.getLabyrinth("67a50bda4d0eb760c577e053");
//        System.out.println("start: "+labyrinth.getStart().getX()+" "+labyrinth.getStart().getY()+" value: "+labyrinth.getNodes().get(18).get(0).getValue());
//        System.out.println("end: "+labyrinth.getEnd().getX()+" "+labyrinth.getEnd().getY());
//
//          gameScreen.gameLoop(labyrinth);


       WelcomePlayer();
//        System.out.println("Thank you for playing the Labyrinth Game!");
//        RedisClientSingleton redisClient = RedisClientSingleton.getInstance();
//        Player player = Player.fromJson(redisClient.getData("player"), Player.class);
//
       displayMenu();
//        createChallenge();


//        player=selectPlayerToChallenge();
//        System.out.println(player.getName());
//        System.out.println(player.getScore());
//        System.out.println(player.getId());

//        ViewChallengesScreen.displayChallenges();


    }
}
