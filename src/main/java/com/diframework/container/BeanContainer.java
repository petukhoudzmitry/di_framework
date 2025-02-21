package com.diframework.container;

import com.diframework.util.Pair;

import java.util.Map;

public class BeanContainer implements Container {

    private final Map<Class<?>, Object> beans = Beans.getBeans();
    private final Map<Pair<String, Class<?>>, Object> namedBeans = Beans.getNamedBeans();


    @Override
    @SuppressWarnings("unchecked")
    public <T> T getBean(Class<T> clazz) {
        return (T) beans.get(clazz);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getBean(String name, Class<T> clazz) {
        return (T) namedBeans.get(new Pair<>(name, clazz));
    }
}
