package com.github.pareronia.aoc;

import java.io.Reader;

import com.google.gson.Gson;

public class Json {

    public static <T> T fromJson(final String json, final Class<T> klass) {
        return new Gson().fromJson(json, klass);
    }
    
    public static <T> T fromJson(final Reader json, final Class<T> klass) {
        return new Gson().fromJson(json, klass);
    }
    
    public static String toJson(final Object obj) {
        return new Gson().toJson(obj);
    }
}
