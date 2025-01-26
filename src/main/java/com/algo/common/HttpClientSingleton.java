package com.algo.common;



import java.net.http.HttpClient;



public class HttpClientSingleton {
    private static HttpClient  httpClient;

    public static synchronized HttpClient getInstance() {
        if (httpClient == null) {
            httpClient = HttpClient.newHttpClient();
        }
        return httpClient;
    }




}
