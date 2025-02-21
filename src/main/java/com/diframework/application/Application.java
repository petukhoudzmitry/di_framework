package com.diframework.application;

import com.diframework.annotation.DIApplication;
import com.diframework.configuration.ClassScanner;
import com.diframework.processor.AnnotationProcessor;

public class Application {
    public static void start(Class<?> entryPoint) {
        if (entryPoint == null) {
            throw new RuntimeException("DIApplication entry point should not be null.");
        } else if (!entryPoint.isAnnotationPresent(DIApplication.class)) {
            throw new RuntimeException("DIApplication entry point should be marked with DIApplication annotation.");
        }

        ClassScanner.findClasses(entryPoint.getPackageName());
        AnnotationProcessor.init();
    }
}
