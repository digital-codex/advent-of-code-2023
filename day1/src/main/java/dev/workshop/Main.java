package dev.workshop;

import java.io.IOException;
import java.util.Arrays;

public class Main {
    public static final System.Logger logger = System.getLogger(Main.class.getName());

    public static void main(String[] args) throws IOException, InterruptedException {
        int result = Common.run(args, "1", System.getenv("AOC_COOKIE"), Main::solution);
        logger.log(System.Logger.Level.INFO, "Result: " + result);
    }

    private static int solution(String input) {
        return Arrays.stream(input.split("\n"))
                .parallel()
                .map(Main::curateInput)
                .mapToInt(Main::mapCuratedInputToResult)
                .sum();
    }

    private static int mapCuratedInputToResult(String input) {
        int[] potential = input.chars().filter(Character::isDigit).toArray();
        return (potential.length >= 1)
                ? Integer.parseInt("" + (char) potential[0] + (char) potential[potential.length - 1])
                : 0;
    }

    private static String curateInput(String input) {
        return Main.findFist(input) + Main.findLast(input);
    }

    private static String findFist(String input) {
        StringBuilder result = new StringBuilder();

        int i = 0;
        while (i < input.length() && !Main.checkForward(input, i, input.charAt(i), result)) i++;

        return result.toString();
    }

    private static String findLast(String input) {
        StringBuilder result = new StringBuilder();

        char[] chars = input.toCharArray();
        int h = 0;
        int t = chars.length - 1;
        while (h < t) {
            char s = chars[h];
            chars[h] = chars[t];
            chars[t] = s;
            h++; t--;
        }

        String reverse = String.valueOf(chars);

        int i = 0;
        while (i < reverse.length() && !Main.checkBackward(reverse, i, reverse.charAt(i), result)) i++;

        return result.reverse().toString();
    }

    @SuppressWarnings("java:S3776")
    private static boolean checkForward(String input, int i, char c, StringBuilder result) {
        boolean found = false;
        switch (c) {
            case 'e' -> found = processMatch(input, i + 1, 4, "ight", '8', 'e', result);
            case 'f' -> {
                if (i + 1 < input.length()) {
                    switch (input.charAt(i + 1)) {
                        case 'i' -> found = processMatch(input, i + 2, 2, "ve", '5', 'i', result);
                        case 'o' -> found = processMatch(input, i + 2, 2, "ur", '4', 'o', result);
                        default -> result.append(c);
                    }
                } else {
                    result.append('f');
                }
            }
            case 'n' -> found = processMatch(input, i + 1, 3, "ine", '9', 'n', result);
            case 'o' -> found = processMatch(input, i + 1, 2, "ne", '1', 'o', result);
            case 's' -> {
                if (i + 1 < input.length()) {
                    switch (input.charAt(i + 1)) {
                        case 'e' -> found = processMatch(input, i + 2, 3, "ven", '7', 'e', result);
                        case 'i' -> found = processMatch(input, i + 2, 1, "x", '6', 'i', result);
                        default -> result.append(c);
                    }
                } else {
                    result.append('s');
                }
            }
            case 't' -> {
                if (i + 1 < input.length()) {
                    switch (input.charAt(i + 1)) {
                        case 'h' -> found = processMatch(input, i + 2, 3, "ree", '3', 'h', result);
                        case 'w' -> found = processMatch(input, i + 2, 1, "o", '2', 'w', result);
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

    @SuppressWarnings("java:S3776")
    private static boolean checkBackward(String input, int i, char c, StringBuilder result) {
        boolean found = false;
        switch (c) {
            case 'e' -> {
                if (i + 1 < input.length()) {
                    switch (input.charAt(i + 1)) {
                        case 'e' -> found = processMatch(input, i + 2, 3, "rht", '3', 'e', result);
                        case 'n' -> {
                            if (i + 2 < input.length()) {
                                switch (input.charAt(i + 2)) {
                                    case 'i' -> found = processMatch(input, i + 3, 1, "n", '9', 'i', result);
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
                        case 'v' -> found = processMatch(input, i + 2, 2, "if", '5', 'v', result);
                        default -> result.append(c);
                    }
                } else {
                    result.append('e');
                }
            }
            case 'n' -> found = processMatch(input, i + 1, 4, "eves", '7', 'n', result);
            case 'o' -> found = processMatch(input, i + 1, 2, "wt", '2', 'o', result);
            case 'r' -> found = processMatch(input, i + 1, 3, "uof", '4', 'r', result);
            case 't' -> found = processMatch(input, i + 1, 4, "hgie", '8', 't', result);
            case 'x' -> found = processMatch(input, i + 1, 2, "is", '6', 'x', result);
            default -> result.append(c);
        }

        return found;
    }

    private static boolean processMatch(String input, int start, int length, String match, char isMatch, char isNonMatch, StringBuilder result) {
        if (match(input, start, length, match)) {
            result.append(isMatch);
            return true;
        } else {
            result.append(isNonMatch);
            return false;
        }
    }

    private static boolean match(String input, int start, int length, String match) {
        return start + length <= input.length() && input.substring(start, start + length).equals(match);
    }
}