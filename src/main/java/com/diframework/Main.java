package com.diframework;

import com.diframework.util.ClassScanner;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        System.out.println(Arrays.toString(ClassScanner.findClasses().toArray(Class<?>[]::new)));
    }
}