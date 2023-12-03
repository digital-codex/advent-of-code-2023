package dev.workshop;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Main {
    public static final System.Logger logger = System.getLogger(Main.class.getName());

    public static void main(String[] args) throws IOException, InterruptedException {
        String input = (args.length != 1)
                ? Common.readInputFromFile(Paths.get(args[0]))
                : Common.readInputFromSource("2", System.getenv("AOC_COOKIE"));

        int result = Main.solution(input.split("\n"));
        logger.log(System.Logger.Level.ALL, "Result: %d%n", result);
    }

    private static int solution(String[] splitInput) {
        return Arrays.stream(splitInput)
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