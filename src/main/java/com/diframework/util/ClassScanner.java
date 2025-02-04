package com.diframework.util;

import java.io.File;
import java.net.URL;
import java.util.*;

public class ClassScanner {

    private final static Set<Class<?>> packages = new HashSet<>();
    private static String path = "";

    public static Set<Class<?>> findClasses() {
        try {
            return scanForClasses();
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }

        return packages;
    }

    private static Set<Class<?>> scanForClasses() throws ClassNotFoundException {
        Set<Class<?>> packages = new HashSet<>();
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        URL resource = classLoader.getResource("");

        if (resource != null) {
            path = resource.getPath();
            File directory = new File(resource.getFile());
            if (directory.exists()) {
                List<File> files = new ArrayList<>(Arrays.stream(directory.listFiles()).toList());

                for (int i = 0; i < files.size(); i++) {
                    File file = files.get(i);
                    if (file.isDirectory()) {
                        files.remove(file);
                        files.addAll(Arrays.asList(file.listFiles()));
                        i--;
                    } else if (file.getName().endsWith(".class")) {
                        String className = file.getAbsolutePath().replace(".class", "").replaceFirst(path, "").replaceAll("/", ".");
                        Class<?> clazz = Class.forName(className);
                        packages.add(clazz);
                    }
                }
            }
        }

        return packages;
    }
}
