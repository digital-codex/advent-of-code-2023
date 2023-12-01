package dev.workshop;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.out.println("Usage: day1 <path-to-input-file>");
            System.exit(-1);
        }

        String input = Main.readInput(args[0]);
        int result = Main.solution(input.split("\n"));

        System.out.printf("Result: %d%n", result);
    }

    private static String readInput(String path) throws IOException {
        return Files.readString(Paths.get(path), Charset.defaultCharset());
    }

    private static int solution(String[] splitInput) {
        return Arrays.stream(splitInput).parallel().map(Main::mapLetterRepresentationToDigitRepresentation).mapToInt(Main::mapCalibrationValueToInt).sum();
    }

    private static int mapCalibrationValueToInt(String input) {
        int[] potential = input.chars().filter(Character::isDigit).toArray();
        return (potential.length >= 1) ? Integer.parseInt("" + (char) potential[0] + (char) potential[potential.length - 1]) : 0;
    }

    private static String mapLetterRepresentationToDigitRepresentation(String input) {
        return Main.mapFistLetterRepresentation(input) + Main.mapLastLetterRepresentation(input);
    }

    private static String mapFistLetterRepresentation(String input) {
        StringBuilder result = new StringBuilder();

        int i = 0;
        while (i < input.length() && !Main.checkForwardLetterRepresentation(input, i, input.charAt(i), result)) {
            i++;
        }

        return result.toString();
    }

    private static String mapLastLetterRepresentation(String input) {
        StringBuilder result = new StringBuilder();
        String substring = new StringBuilder(input).reverse().toString();

        int i = 0;
        while (i < substring.length() && !Main.checkBackwardLetterRepresentation(substring, i, substring.charAt(i), result)) {
            i++;
        }

        return result.reverse().toString();
    }

    // don't see a way to reduce the cognitive complexity from 19 to 15
    private static boolean checkForwardLetterRepresentation(String input, int i, char c, StringBuilder result) {
        boolean found = false;
        switch (c) {
            case 'e' -> found = matchLetterRepresentation(input, i + 1, 4, "ight", '8', 'e', result);
            case 'f' -> {
                if (i + 1 < input.length()) {
                    switch (input.charAt(i + 1)) {
                        case 'i' -> found = matchLetterRepresentation(input, i + 2, 2, "ve", '5', 'i', result);
                        case 'o' -> found = matchLetterRepresentation(input, i + 2, 2, "ur", '4', 'o', result);
                        default -> result.append(c);
                    }
                } else {
                    result.append('f');
                }
            }
            case 'n' -> found = matchLetterRepresentation(input, i + 1, 3, "ine", '9', 'n', result);
            case 'o' -> found = matchLetterRepresentation(input, i + 1, 2, "ne", '1', 'o', result);
            case 's' -> {
                if (i + 1 < input.length()) {
                    switch (input.charAt(i + 1)) {
                        case 'e' -> found = matchLetterRepresentation(input, i + 2, 3, "ven", '7', 'e', result);
                        case 'i' -> found = matchLetterRepresentation(input, i + 2, 1, "x", '6', 'i', result);
                        default -> result.append(c);
                    }
                } else {
                    result.append('s');
                }
            }
            case 't' -> {
                if (i + 1 < input.length()) {
                    switch (input.charAt(i + 1)) {
                        case 'h' -> found = matchLetterRepresentation(input, i + 2, 3, "ree", '3', 'h', result);
                        case 'w' -> found = matchLetterRepresentation(input, i + 2, 1, "o", '2', 'w', result);
                        default -> result.append(c);
                    }
                } else {
                    result.append('t');
                }
            }
            default -> result.append(c);
        }

        return found;
    }

    // don't see a way to reduce the cognitive complexity from 17 to 15
    private static boolean checkBackwardLetterRepresentation(String input, int i, char c, StringBuilder result) {
        boolean found = false;
        switch (c) {
            case 'e' -> {
                if (i + 1 < input.length()) {
                    switch (input.charAt(i + 1)) {
                        case 'e' -> found = matchLetterRepresentation(input, i + 2, 3, "rht", '3', 'e', result);
                        case 'n' -> {
                            if (i + 2 < input.length()) {
                                switch (input.charAt(i + 2)) {
                                    case 'i' -> found = matchLetterRepresentation(input, i + 3, 1, "n", '9', 'i', result);
                                    case 'o' -> {
                                        result.append('1');
                                        found = true;
                                    }
                                    default -> result.append(c);
                                }
                            } else {
                                result.append('n');
                            }
                        }
                        case 'v' -> found = matchLetterRepresentation(input, i + 2, 2, "if", '5', 'v', result);
                        default -> result.append(c);
                    }
                } else {
                    result.append('e');
                }
            }
            case 'n' -> found = matchLetterRepresentation(input, i + 1, 4, "eves", '7', 'n', result);
            case 'o' -> found = matchLetterRepresentation(input, i + 1, 2, "wt", '2', 'o', result);
            case 'r' -> found = matchLetterRepresentation(input, i + 1, 3, "uof", '4', 'r', result);
            case 't' -> found = matchLetterRepresentation(input, i + 1, 4, "hgie", '8', 't', result);
            case 'x' -> found = matchLetterRepresentation(input, i + 1, 2, "is", '6', 'x', result);
            default -> result.append(c);
        }

        return found;
    }

    private static boolean matchLetterRepresentation(String input, int start, int length, String match, char isMatch, char isNonMatch, StringBuilder result) {
        if (matchLetterRepresentation(input, start, length, match)) {
            result.append(isMatch);
            return true;
        } else {
            result.append(isNonMatch);
            return false;
        }
    }

    private static boolean matchLetterRepresentation(String input, int start, int length, String match) {
        return start + length <= input.length() && input.substring(start, start + length).equals(match);
    }
}