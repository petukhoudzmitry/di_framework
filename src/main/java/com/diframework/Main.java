package com.diframework;

import com.diframework.annotation.Autowired;
import com.diframework.annotation.Bean;
import com.diframework.annotation.Configuration;
import com.diframework.annotation.DIApplication;
import com.diframework.application.Application;

@Configuration
@DIApplication
public class Main {

    @Autowired("test")
    private static String value = "a";

    public static void main(String[] args) {
        System.out.println(value);
        Application.start(Main.class);
        System.out.println(value);
    }

    @Bean
    public static String getBean() {
        return "Hello, world!";
    }

    @Bean("test")
    public static String getBean2(String value) {
        return value + "!";
    }
}