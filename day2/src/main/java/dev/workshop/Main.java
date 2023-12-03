package dev.workshop;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Main {
    public static final System.Logger logger = System.getLogger(Main.class.getName());

    public static void main(String[] args) throws IOException, InterruptedException {
        int result = Common.run(args, "2", System.getenv("AOC_COOKIE"), Main::solution);
        logger.log(System.Logger.Level.INFO, "Result: " + result);
    }

    private static int solution(String input) {
        return Arrays.stream(input.split("\n"))
                .parallel()
                .map(Main::mapGameToRounds)
                .mapToInt(Main::mapRoundsToResult)
                .sum();
    }

    private static String[] mapGameToRounds(String input) {
        return input.split(":")[1].trim().split(";");
    }

    private static int mapRoundsToResult(String[] rounds) {
        Map<String, Integer> minValueMap = new HashMap<>();

        for (String round : rounds) {
            for (String set : round.trim().split(",")) {
                String[] setSplit = set.trim().split(" ");
                int value = Integer.parseInt(setSplit[0].trim());
                String key = setSplit[1].trim();

                int curr = minValueMap.getOrDefault(key, 0);
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