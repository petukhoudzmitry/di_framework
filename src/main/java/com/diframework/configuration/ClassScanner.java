package com.diframework.configuration;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ClassScanner {

    private final static Set<Class<?>> classes = new HashSet<>();

    public static void findClasses(String basePackage) {
        try {
            scanForClasses(basePackage);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void scanForClasses(String basePackage) throws ClassNotFoundException {
        if (basePackage == null) throw new RuntimeException("Base package cannot be null.");
        String packagePath = basePackage.replace(".", "/");

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        URL resource = classLoader.getResource(packagePath);

        if (resource != null) {
            if ("file".equals(resource.getProtocol())) {
                File directory = new File(resource.getFile());

                if (directory.exists() || directory.canExecute()) {
                    List<File> files = new ArrayList<>(Arrays.stream(directory.listFiles()).toList());

                    for (File file : files) {
                        if (file.getName().endsWith(".class")) {
                            String className = String.format("%s.%s", basePackage, file.getName().replace(".class", ""));
                            Class<?> clazz = Class.forName(className);
                            classes.add(clazz);
                        }
                    }
                }
            } else if ("jar".equals(resource.getProtocol())) {
                try {
                    JarURLConnection jarURLConnection = (JarURLConnection) resource.openConnection();
                    JarFile jarFile = jarURLConnection.getJarFile();
                    Enumeration<JarEntry> entries = jarFile.entries();
                    while (entries.hasMoreElements()) {
                        JarEntry entry = entries.nextElement();
                        if (entry.getName().endsWith(".class")) {
                            String className = entry.getName().replace("/", ".").replace(".class", "");
                            Class<?> clazz = Class.forName(className);
                            classes.add(clazz);
                        }
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

        }
    }

    public static Set<Class<?>> getClasses() {
        return classes;
    }
}
