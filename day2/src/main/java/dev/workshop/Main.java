package dev.workshop;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
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
        String[][][] games = Main.buildGameArray(splitInput);
        int result = 0;

        for (String[][] game : games) {
            Map<String, Integer> minValueMap = new HashMap<>(Map.of("red", 0, "green", 0, "blue", 0));
            for (String[] round : game) {
                for (String set : round) {
                    String[] setSplit = set.split(" ");

                    int value = Integer.parseInt(setSplit[0].trim());
                    String key = setSplit[1].trim();

                    int curr = minValueMap.get(key);
                    if (value > curr) {
                        minValueMap.put(key, value);
                    }
                }
            }
            result += minValueMap.values().stream().mapToInt(Integer::intValue).reduce(1, (a, b) -> a * b);
        }

        return result;
    }

    private static String[][][] buildGameArray(String[] splitInput) {
        String[][][] games = new String[splitInput.length][][];

        for (int i = 0; i < splitInput.length; i++) {
            String[] gameSplit = splitInput[i].split(":");

            String[] roundSplit = gameSplit[1].trim().split(";");
            games[i] = new String[roundSplit.length][];

            for (int j = 0; j < roundSplit.length; j++) {
                String[] valueSplit = roundSplit[j].trim().split(",");
                games[i][j] = new String[valueSplit.length];

                for (int k = 0; k < valueSplit.length; k++) {
                    games[i][j][k] = valueSplit[k].trim();
                }
            }
        }

        return games;
    }
}