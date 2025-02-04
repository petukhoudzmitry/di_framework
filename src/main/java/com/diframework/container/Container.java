package com.diframework.container;


public interface Container {
    <T> T getBean(Class<T> clazz);

    <T> T getBean(String name, Class<T> clazz);
}
