package ru.yandex.practicum;

import com.google.gson.Gson;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Scanner;

@SpringBootApplication
public class Main {
    public static void main(final String[] args) {
        final Gson gson = new Gson();
        final Scanner scanner = new Scanner(System.in);
        SpringApplication.run(Main.class, args);

    }
}