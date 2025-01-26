package com.algo.common;

import java.util.HashMap;

public abstract class HttpHelper {

    public static String createFormUrlEncodedBody(HashMap<String, Object> params) {
        StringBuilder body = new StringBuilder();
        for (String key : params.keySet()) {
            body.append(key).append("=").append(params.get(key)).append("&");
        }
        return body.toString();
    }

    public static String createQueryString(HashMap<String, Object> params) {
        StringBuilder queryString = new StringBuilder();
        queryString.append("?");
        for (String key : params.keySet()) {
            if (params.get(key) == null || params.get(key).toString().isEmpty()) {
                continue;
            }
            queryString.append(key).append("=").append(params.get(key)).append("&");
        }
        return queryString.substring(0, queryString.length() - 1);
    }
}
