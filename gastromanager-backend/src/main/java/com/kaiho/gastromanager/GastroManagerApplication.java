package com.kaiho.gastromanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Arrays;

@SpringBootApplication
public class GastroManagerApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(GastroManagerApplication.class, args);
        // Print del profile activo
        System.out.println("Active profiles: " + Arrays.toString(run.getEnvironment().getActiveProfiles()));

        // Print de la propiedad de datasource username
        String dbUser = run.getEnvironment().getProperty("spring.datasource.username");
        System.out.println("Datasource username: " + dbUser);
    }

}
