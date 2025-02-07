package com.diframework.container;

import com.diframework.annotation.Autowired;
import com.diframework.annotation.Bean;
import com.diframework.annotation.Configuration;
import com.diframework.util.ClassScanner;
import com.diframework.util.Pair;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.logging.Logger;

public class BeanContainer implements Container {

    private static final Set<Class<?>> classes = ClassScanner.findClasses();

    private final Map<Class<?>, Object> beans = new HashMap<>();
    private final Map<Pair<String, Class<?>>, Object> namedBeans = new HashMap<>();
    private final List<Method> beanMethods = new ArrayList<>();


    public BeanContainer() {
        processConfigurations();
        processAutowired();
    }


    private void processConfigurations() {
        for (Class<?> clazz : classes) {
            if (clazz.isAnnotationPresent(Configuration.class)) {
                Method[] methods =  clazz.getMethods();
                for (Method method : methods) {
                    if (method.isAnnotationPresent(Bean.class)) {
                        beanMethods.add(method);
                    }
                }
            }
        }

        processMethods();
    }


    private void processAutowired() {
        for (Class<?> clazz : ClassScanner.findClasses()) {
//            Method[] methods = clazz.getMethods();
            Field[] fields = clazz.getDeclaredFields();

//            for (Method method : methods) {
//                for (Parameter parameter : method.getParameters()) {
//                    if (parameter.isAnnotationPresent(Autowired.class)) {
//                        try {
//                            beans.put(parameter.getType(), method.invoke(null));
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//            }

            for (Field field : fields) {
                if (field.isAnnotationPresent(Autowired.class) &&
                        Modifier.isStatic(field.getModifiers()) &&
                        !Modifier.isFinal(field.getModifiers())) {
                    try {
                        field.setAccessible(true);
                        field.set(null, beans.get(field.getType()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }


    private void processMethods() {
        if (beanMethods.isEmpty()) {
            return;
        }

        beanMethods.sort(Comparator.comparingInt(Method::getParameterCount));
        if (beanMethods.getFirst().getParameterCount() != 0) {
            throw new RuntimeException("Couldn't resolve beans required for method " + beanMethods.getFirst().getName());
        }

        for (Method method : beanMethods) {

        }
    }


    @Override
    @SuppressWarnings("unchecked")
    public <T> T getBean(Class<T> clazz) {
        return (T) beans.get(clazz);
    }

    @Override
    public <T> T getBean(String name, Class<T> clazz) {
        return null;
    }
}
