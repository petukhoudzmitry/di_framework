package com.diframework;

import com.diframework.annotation.Autowired;
import com.diframework.annotation.Bean;
import com.diframework.annotation.Configuration;
import com.diframework.container.BeanContainer;
import com.diframework.util.ClassScanner;

import java.util.Arrays;

@Configuration
public class Main {

    @Autowired
    private static String value = "a";

    public static void main(String[] args) {
        System.out.println(value);
        BeanContainer container = new BeanContainer();
        System.out.println(value);
    }

    @Bean("test")
    public static String getBean() {
        return "Hello, world!";
    }
}