package com.example.fantasyinvestor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class }, scanBasePackages = {"controllers","entities", "services", "configs", "exceptions", "helpers", "pojos", "requests", "responses"})
public class FantasyInvestorApplication {

    public static void main(String[] args) {
        SpringApplication.run(FantasyInvestorApplication.class, args);
    }

}
