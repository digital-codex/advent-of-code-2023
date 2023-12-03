package dev.workshop;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.out.println("Usage: day2 <path-to-input-file>");
            System.exit(-1);
        }

        String input = readInput(args[0]);
        int result = Main.solution(input.split("\n"));

        System.out.printf("Result: %d%n", result);
    }

    private static String readInput(String path) throws IOException {
        return Files.readString(Paths.get(path), Charset.defaultCharset());
    }

    private static int solution(String[] splitInput) {
        return Arrays.stream(splitInput)
                .parallel()
                .map(Main::mapGameToRoundsArray)
                .mapToInt(Main::mapGameToInt)
                .sum();
    }

    private static String[] mapGameToRoundsArray(String input) {
        return input.split(":")[1].trim().split(";");
    }

    private static int mapGameToInt(String[] rounds) {
        Map<String, Integer> minValueMap = new HashMap<>(
                Map.of("red", 0, "green", 0, "blue", 0)
        );

        for (String round : rounds) {
            for (String set : round.trim().split(",")) {
                String[] setSplit = set.trim().split(" ");
                int value = Integer.parseInt(setSplit[0].trim());
                String key = setSplit[1].trim();

                int curr = minValueMap.get(key);
                if (value > curr) {
                    minValueMap.put(key, value);
                }
            }
        }

        return minValueMap.values().stream()
                .mapToInt(Integer::intValue)
                .reduce(1, (a, b) -> a * b);
    }
}