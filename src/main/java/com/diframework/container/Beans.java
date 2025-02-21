package com.diframework.container;

import com.diframework.util.Pair;

import java.util.HashMap;
import java.util.Map;

public class Beans {
    private static final Map<Class<?>, Object> beans = new HashMap<>();
    private static final Map<Pair<String, Class<?>>, Object> namedBeans = new HashMap<>();

    public static Map<Class<?>, Object> getBeans() {
        return beans;
    }

    public static Map<Pair<String, Class<?>>, Object> getNamedBeans() {
        return namedBeans;
    }
}
