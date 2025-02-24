package com.algo.common.singletons;

import redis.clients.jedis.Jedis;

public class RedisClientSingleton {

    private static RedisClientSingleton instance = null;
    private static final String REDIS_HOST = "localhost";
    private static final int REDIS_PORT = 6379;

    private RedisClientSingleton() {}

    public static RedisClientSingleton getInstance() {
        if (instance == null) {
            instance = new RedisClientSingleton();
        }
        return instance;
    }

    public void saveData(String key, String value) {
        try (Jedis jedis = new Jedis(REDIS_HOST, REDIS_PORT)) {
            jedis.set(key, value);
        }
    }

    public String getData(String key) {
        try (Jedis jedis = new Jedis(REDIS_HOST, REDIS_PORT)) {
            return jedis.get(key);
        }

    }

    public void deleteData(String key) {
        try (Jedis jedis = new Jedis(REDIS_HOST, REDIS_PORT)) {
            jedis.del(key);
        }
    }


}