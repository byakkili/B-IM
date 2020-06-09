package com.github.byakkili.bim.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Scanner;

/**
 * @author Guannian Li
 */
@SpringBootApplication
public class JsonApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(JsonApplication.class, args);

        new Scanner(System.in).next();
        context.close();
    }
}
