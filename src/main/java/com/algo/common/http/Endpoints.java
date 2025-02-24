package com.algo.common.http;

public abstract class Endpoints {

    public static final String BASE_URL = "http://localhost:8080";
    public static final String SIGN_IN = "/players/sign-in";
    public static final String SIGN_UP = "/players/sign-up";
    public static final String GET_PLAYER = "/players/%s";
    public static final String UPDATE_PLAYER = "/players/%s";
    public static final String DELETE_PLAYER = "/players/%s";
    public static final String GET_PLAYER_BY_NAME = "/players/by-name/%s";
    public static final String GET_PLAYERS_SORTED = "/players/sorted";
    public static final String CREATE_CHALLENGE = "/challenges/create";
    public static final String UPDATE_CHALLENGE = "/challenges";
    public static final String GET_CHALLENGES = "/challenges";
    public static final String ACCEPT_CHALLENGE = "/challenges/%s/accept";
    public static final String DECLINE_CHALLENGE = "/challenges/%s/decline";
    public static final String DELETE_CHALLENGE = "/challenges/%s";
    public static final String GET_LABYRINTH = "/labyrinth/%s";

    public static String injectStringIntoPath(String path, String injection) {
        return String.format(path, injection);
    }
}
