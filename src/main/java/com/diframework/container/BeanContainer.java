package com.diframework.container;

import com.diframework.annotation.Autowired;
import com.diframework.annotation.Bean;
import com.diframework.annotation.Configuration;
import com.diframework.util.ClassScanner;
import com.diframework.util.Pair;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class BeanContainer implements Container {

    private final Map<Class<?>, Object> beans = new HashMap<>();
    private final Map<Pair<String, Class<?>>, Object> namedBeans = new HashMap<>();


    public BeanContainer() {
        processConfigurations();
    }

    private void processConfigurations() {
        for (Class<?> clazz : ClassScanner.findClasses()) {
            if (clazz.isAnnotationPresent(Configuration.class)) {
                Method[] methods =  clazz.getMethods();
                for (Method method : methods) {
                    if (method.isAnnotationPresent(Bean.class)) {
                        try {
                            beans.put(method.getReturnType(), method.invoke(null));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }


    private void processAutowired() {
        for (Class<?> clazz : ClassScanner.findClasses()) {
//            Method[] methods = clazz.getMethods();
            Field[] fields = clazz.getFields();

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
        }
    }

    @Override
    public <T> T getBean(Class<T> clazz) {
        return (T) beans.get(clazz);
    }

    @Override
    public <T> T getBean(String name, Class<T> clazz) {
        return null;
    }
}
