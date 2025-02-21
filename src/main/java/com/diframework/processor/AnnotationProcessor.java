package com.diframework.processor;

import com.diframework.annotation.Autowired;
import com.diframework.annotation.Bean;
import com.diframework.annotation.Configuration;
import com.diframework.configuration.ClassScanner;
import com.diframework.container.Beans;
import com.diframework.util.Pair;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.logging.Logger;

public class AnnotationProcessor {

    private static final Map<Class<?>, Object> beans = Beans.getBeans();
    private static final Map<Pair<String, Class<?>>, Object> namedBeans = Beans.getNamedBeans();

    private static final List<Method> beanMethods = new ArrayList<>();
    private static final List<Field> autowiredFields = new ArrayList<>();

    public static void init() {
        processConfigurations();
        processMethods();
        processAutowired();
    }

    private static void processConfigurations() {
        Set<Class<?>> classes = ClassScanner.getClasses();

        for (Class<?> clazz : classes) {
            if (clazz.isAnnotationPresent(Configuration.class)) {
                Method[] methods =  clazz.getMethods();
                for (Method method : methods) {
                    if (method.isAnnotationPresent(Bean.class)) {
                        beanMethods.add(method);
                    }
                }

                Field[] fields = clazz.getDeclaredFields();
                for (Field field : fields) {
                    if (field.isAnnotationPresent(Autowired.class)) {
                        autowiredFields.add(field);
                    }
                }
            }
        }
    }

    private static void processMethods() {
        int beanCount = beanMethods.size();

        while (!beanMethods.isEmpty()) {
            for (int i = 0; i < beanMethods.size(); i++) {
                Method method = beanMethods.get(i);
                String name = method.getDeclaredAnnotation(Bean.class).value();
                Class<?> returnType = method.getReturnType();

                try {
                    Object bean;

                    if (method.getParameterCount() > 0) {
                        List<Object> passedParameters = new ArrayList<>();

                        for (Parameter parameter : method.getParameters()) {
                            Autowired annotation = parameter.getDeclaredAnnotation(Autowired.class);
                            if (annotation != null) {
                                String parameterName = annotation.value();
                                if (!parameterName.isEmpty()) {
                                    Pair<String, Class<?>> key = new Pair<>(parameterName, parameter.getType());
                                    if (namedBeans.containsKey(key)) {
                                        passedParameters.add(namedBeans.get(key));
                                    }
                                } else {
                                    if (beans.containsKey(parameter.getType())) {
                                        passedParameters.add(beans.get(parameter.getType()));
                                    }
                                }
                            } else {
                                if (beans.containsKey(parameter.getType())) {
                                    passedParameters.add(beans.get(parameter.getType()));
                                }
                            }
                        }

                        if (passedParameters.size() == method.getParameterCount()) {
                            bean = method.invoke(null, passedParameters.toArray());
                        } else {
                            continue;
                        }
                    } else {
                        bean = method.invoke(null);
                    }

                    if (name.isEmpty()) {
                        beans.put(returnType, bean);
                    } else {
                        namedBeans.put(new Pair<>(name, returnType), bean);
                    }
                    beanMethods.remove(i);
                    i--;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (beanCount - beanMethods.size() == 0) {
                throw new RuntimeException("Could not resolve all beans");
            } else {
                beanCount = beanMethods.size();
            }
        }
    }

    private static void processAutowired() {
        for (Field field : autowiredFields) {
            if (Modifier.isStatic(field.getModifiers()) && !Modifier.isFinal(field.getModifiers())) {
                try {
                    field.setAccessible(true);
                    String name = field.getDeclaredAnnotation(Autowired.class).value();
                    if (name.isEmpty()) {
                        field.set(null, beans.get(field.getType()));
                    } else {
                        field.set(null, namedBeans.get(new Pair<>(name, field.getType())));
                    }
                } catch (Exception e) {
                    Logger.getAnonymousLogger().severe(e.getMessage());
                }
            }
        }
    }
}
