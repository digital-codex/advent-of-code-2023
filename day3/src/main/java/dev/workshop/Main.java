package dev.workshop;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
    public static final System.Logger logger = System.getLogger(Main.class.getName());

    private record Pair(int i, int j) {}

    public static void main(String[] args) throws IOException, InterruptedException {
        int result = Common.run(args, "3", System.getenv("AOC_COOKIE"), Main::solution);
        logger.log(System.Logger.Level.INFO, "Result: " + result);
    }

    public static int solution(String input) {
        return Main.findGearRatios(Main.mapInputToMatrix(input)).stream()
                .parallel()
                .mapToInt(Integer::intValue)
                .sum();
    }

    public static char[][] mapInputToMatrix(String input) {
        return Arrays.stream(input.split("\n"))
                .map(String::toCharArray)
                .toArray(char[][]::new);
    }

    public static List<Integer> findGearRatios(char[][] matrix) {
        List<Integer> result = new ArrayList<>();
        Map<Pair, List<Integer>> ratios = new HashMap<>();

        for (int i = 0; i < matrix.length; i++) {
            int j = 0;
            while (j < matrix[i].length) {
                j += processIsValid(i, j, matrix, ratios);
            }
        }
        ratios.values().stream().filter(value -> value.size() == 2).forEach(value -> result.add(value.get(0) * value.get(1)));
        return result;
    }

    private static int processIsValid(int i, int j, char[][] matrix, Map<Pair, List<Integer>> ratios) {
        StringBuilder integer = null;
        if (Character.isDigit(matrix[i][j])) {
            boolean isValid = Main.isValid(i, j, matrix);
            Pair gear = null;
            if (isValid) {
                gear = findGear(i, j, matrix);
            }
            integer = new StringBuilder();
            integer.append(matrix[i][j]); j++;
            while(j < matrix[i].length && Character.isDigit(matrix[i][j])) {
                if (!isValid) {
                    isValid = Main.isValid(i, j, matrix);
                } else if (gear == null) {
                    gear = findGear(i, j, matrix);
                }
                integer.append(matrix[i][j]); j++;
            }
            if (isValid && gear == null) {
                gear = findGear(i, j, matrix);
            }
            if (gear != null) {
                List<Integer> parts = ratios.getOrDefault(gear, new ArrayList<>());
                parts.add(Integer.parseInt(integer.toString()));
                ratios.put(gear, parts);
            }
        }

        return (integer == null || integer.isEmpty()) ? 1 : integer.length();
    }

    private static boolean isValid(int rowIndex, int columnIndex, char[][] matrix) {
        return (rowIndex - 1 >= 0 && columnIndex - 1 >= 0 && (!Character.isDigit(matrix[rowIndex - 1][columnIndex - 1]) && matrix[rowIndex - 1][columnIndex - 1] == '*'))
                || (rowIndex - 1 >= 0 && (!Character.isDigit(matrix[rowIndex - 1][columnIndex]) && matrix[rowIndex - 1][columnIndex] == '*'))
                || (rowIndex - 1 >= 0 && columnIndex + 1 < matrix[rowIndex].length && (!Character.isDigit(matrix[rowIndex - 1][columnIndex + 1]) && matrix[rowIndex - 1][columnIndex + 1] == '*'))
                || (columnIndex - 1 >= 0 && (!Character.isDigit(matrix[rowIndex][columnIndex - 1]) && matrix[rowIndex][columnIndex - 1] == '*'))
                || (columnIndex + 1 < matrix[rowIndex].length && (!Character.isDigit(matrix[rowIndex][columnIndex + 1]) && matrix[rowIndex][columnIndex + 1] == '*'))
                || (rowIndex + 1 < matrix.length && columnIndex - 1 >= 0 && (!Character.isDigit(matrix[rowIndex + 1][columnIndex - 1]) && matrix[rowIndex + 1][columnIndex - 1] == '*'))
                || (rowIndex + 1 < matrix.length && (!Character.isDigit(matrix[rowIndex + 1][columnIndex]) && matrix[rowIndex + 1][columnIndex] == '*'))
                || (rowIndex + 1 < matrix.length && columnIndex + 1 < matrix[rowIndex].length && (!Character.isDigit(matrix[rowIndex + 1][columnIndex + 1]) && matrix[rowIndex + 1][columnIndex + 1] == '*'));
    }

    private static Pair findGear(int rowIndex, int columnIndex, char[][] matrix) {
        if (rowIndex - 1 >= 0 && columnIndex - 1 >= 0 && (!Character.isDigit(matrix[rowIndex - 1][columnIndex - 1]) && matrix[rowIndex - 1][columnIndex - 1] == '*')) {
            return new Pair(rowIndex - 1, columnIndex - 1);
        }
        if (rowIndex - 1 >= 0 && (!Character.isDigit(matrix[rowIndex - 1][columnIndex]) && matrix[rowIndex - 1][columnIndex] == '*')) {
            return new Pair(rowIndex - 1, columnIndex);
        }
        if (rowIndex - 1 >= 0 && columnIndex + 1 < matrix[rowIndex].length && (!Character.isDigit(matrix[rowIndex - 1][columnIndex + 1]) && matrix[rowIndex - 1][columnIndex + 1] == '*')) {
            return new Pair(rowIndex - 1, columnIndex + 1);
        }
        if (columnIndex - 1 >= 0 && (!Character.isDigit(matrix[rowIndex][columnIndex - 1]) && matrix[rowIndex][columnIndex - 1] == '*')) {
            return new Pair(rowIndex, columnIndex - 1);
        }
        if (!Character.isDigit(matrix[rowIndex][columnIndex]) && matrix[rowIndex][columnIndex] == '*') {
            return new Pair(rowIndex, columnIndex);
        }
        if (columnIndex + 1 < matrix[rowIndex].length && (!Character.isDigit(matrix[rowIndex][columnIndex + 1]) && matrix[rowIndex][columnIndex + 1] == '*')) {
            return new Pair(rowIndex, columnIndex + 1);
        }
        if (rowIndex + 1 < matrix.length && columnIndex - 1 >= 0 && (!Character.isDigit(matrix[rowIndex + 1][columnIndex - 1]) && matrix[rowIndex + 1][columnIndex - 1] == '*')) {
            return new Pair(rowIndex + 1, columnIndex - 1);
        }
        if (rowIndex + 1 < matrix.length && (!Character.isDigit(matrix[rowIndex + 1][columnIndex]) && matrix[rowIndex + 1][columnIndex] == '*')) {
            return new Pair(rowIndex + 1, columnIndex);
        }
        if (rowIndex + 1 < matrix.length && columnIndex + 1 < matrix[rowIndex].length && (!Character.isDigit(matrix[rowIndex + 1][columnIndex + 1]) && matrix[rowIndex + 1][columnIndex + 1] == '*')) {
            return new Pair(rowIndex + 1, columnIndex + 1);
        }

        return null;
    }
}