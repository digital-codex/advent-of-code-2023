package dev.workshop;

import dev.workshop.type.Try;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Main {
    public static final System.Logger logger = System.getLogger(Main.class.getName());

    public static void main(String[] args) throws IOException, InterruptedException {
        Main.logger.log(System.Logger.Level.INFO, "Result: " + Common.run(args, "1", System.getenv("AOC_COOKIE"), Main::solution));
    }

    // TODO: refactor
    private static boolean putChar(Appendable result, char c, boolean ret) throws IOException {
        result.append(c);
        return ret;
    }

    // TODO: refactor
    private static boolean match(String input, int start, int length, String match, char isMatch, char isNonMatch, Appendable result) throws IOException {
        if (start + length >= input.length()) {
            return Main.putChar(result, isNonMatch, false);
        }

        for (int i = 0; i < length; i++) {
            if (input.charAt(start + i) != match.charAt(i)) {
                return Main.putChar(result, isNonMatch, false);
            }
        }

        return Main.putChar(result, isMatch, true);
    }

    // TODO: refactor
    private static boolean matchReverse(String input, int end, int length, String match, char isMatch, char isNonMatch, Appendable result) throws IOException {
        if (end - (length - 1) < 0) {
            return Main.putChar(result, isNonMatch, false);
        }

        for (int i = 0; i < length; i++) {
            if (input.charAt(end - i) != match.charAt(i)) {
                return Main.putChar(result, isNonMatch, false);
            }
        }

        return Main.putChar(result, isMatch, true);
    }

    private static boolean intermediateMatch(String input, int idx, int depth, Map<String, ProcessMatch> cases, String currentCase, Appendable result) throws IOException {
        return (idx + depth < input.length()) ? cases.getOrDefault(currentCase + input.charAt(idx + depth), (String in, int i, Appendable res) -> Main.putChar(res, in.charAt(i), false)).process(input, idx, result) : Main.putChar(result, currentCase.charAt(currentCase.length() - 1), false);
    }

    @FunctionalInterface
    public interface ProcessMatch {
        boolean process(String input, int i, Appendable result) throws IOException;
    }

    private static final Map<String, ProcessMatch> FORWARD_CASES = new HashMap<>();
    static {
        Main.FORWARD_CASES.put("e", (String in, int i, Appendable res) -> Main.match(in, i + 1, 4, "ight", '8', 'e', res));
        Main.FORWARD_CASES.put("f", (String in, int i, Appendable res) -> Main.intermediateMatch(in, i, 1, Main.FORWARD_CASES, "f", res));
        Main.FORWARD_CASES.put("fi", (String in, int i, Appendable res) -> Main.match(in, i + 2, 2, "ve", '5', 'i', res));
        Main.FORWARD_CASES.put("fo", (String in, int i, Appendable res) -> Main.match(in, i + 2, 2, "ur", '4', 'o', res));
        Main.FORWARD_CASES.put("n", (String in, int i, Appendable res) -> Main.match(in, i + 1, 3, "ine", '9', 'n', res));
        Main.FORWARD_CASES.put("o", (String in, int i, Appendable res) -> Main.match(in, i + 1, 2, "ne", '1', 'o', res));
        Main.FORWARD_CASES.put("s", (String in, int i, Appendable res) -> Main.intermediateMatch(in, i, 1, Main.FORWARD_CASES, "s", res));
        Main.FORWARD_CASES.put("se", (String in, int i, Appendable res) -> Main.match(in, i + 2, 3, "ven", '7', 'e', res));
        Main.FORWARD_CASES.put("si", (String in, int i, Appendable res) -> Main.match(in, i + 2, 1, "x", '6', 'i', res));
        Main.FORWARD_CASES.put("t", (String in, int i, Appendable res) -> Main.intermediateMatch(in, i, 1, Main.FORWARD_CASES, "t", res));
        Main.FORWARD_CASES.put("th", (String in, int i, Appendable res) -> Main.match(in, i + 2, 3, "ree", '3', 'h', res));
        Main.FORWARD_CASES.put("tw", (String in, int i, Appendable res) -> Main.match(in, i + 2, 1, "o", '2', 'w', res));
    }

    private static final Map<String, ProcessMatch> BACKWARD_CASES = new HashMap<>();
    static {
        Main.BACKWARD_CASES.put("e", (String in, int i, Appendable res) -> Main.intermediateMatch(in, i, -1, Main.BACKWARD_CASES, "e", res));
        Main.BACKWARD_CASES.put("ee", (String in, int i, Appendable res) -> Main.matchReverse(in, i - 2, 3, "rht", '3', 'e', res));
        Main.BACKWARD_CASES.put("en", (String in, int i, Appendable res) -> Main.intermediateMatch(in, i, -2, Main.BACKWARD_CASES, "en", res));
        Main.BACKWARD_CASES.put("eni", (String in, int i, Appendable res) -> Main.matchReverse(in, i - 3, 1, "n", '9', 'i', res));
        Main.BACKWARD_CASES.put("eno", (String in, int i, Appendable res) -> Main.putChar(res, '1', true));
        Main.BACKWARD_CASES.put("ev", (String in, int i, Appendable res) -> Main.matchReverse(in, i - 2, 2, "if", '5', 'v', res));
        Main.BACKWARD_CASES.put("n", (String in, int i, Appendable res) -> Main.matchReverse(in, i - 1, 4, "eves", '7', 'n', res));
        Main.BACKWARD_CASES.put("o", (String in, int i, Appendable res) -> Main.matchReverse(in, i - 1, 2, "wt", '2', 'o', res));
        Main.BACKWARD_CASES.put("r", (String in, int i, Appendable res) -> Main.matchReverse(in, i - 1, 3, "uof", '4', 'r', res));
        Main.BACKWARD_CASES.put("t", (String in, int i, Appendable res) -> Main.matchReverse(in, i - 1, 4, "hgie", '8', 't', res));
        Main.BACKWARD_CASES.put("x", (String in, int i, Appendable res) -> Main.matchReverse(in, i - 1, 2, "is", '6', 'x', res));
    }

    private static final ProcessMatch DEFAULT_CASE = (String in, int i, Appendable res) -> Main.putChar(res, in.charAt(i), false);

    private static boolean checkMatch(String input, int idx, Map<String, ProcessMatch> cases, Appendable result) throws IOException {
        return cases.getOrDefault(String.valueOf(input.charAt(idx)), Main.DEFAULT_CASE).process(input, idx, result);
    }

    // TODO: refactor
    private static String findFist(String input) throws IOException {
        StringBuilder result = new StringBuilder();

        int i = 0;
        while (i < input.length() && !Main.checkMatch(input, i, Main.FORWARD_CASES, result)) i++;

        return result.toString();
    }

    // TODO: refactor
    private static String findLast(String input) throws IOException {
        StringBuilder result = new StringBuilder();

        int i = input.length() - 1;
        while (i >= 0 && !Main.checkMatch(input, i, Main.BACKWARD_CASES, result)) i--;

        return result.reverse().toString();
    }

    // TODO: refactor
    private static int mapCuratedInputToResult(String input) {
        int[] potential = input.chars().filter(Character::isDigit).toArray();
        return (potential.length >= 1)
                ? Integer.parseInt("" + (char) potential[0] + (char) potential[potential.length - 1])
                : 0;
    }

    private static String curateInput(String input) throws IOException {
        return Main.findFist(input) + Main.findLast(input);
    }

    private static int solution(String input) {
        return Arrays.stream(input.split("\n"))
                .parallel()
                .map(Try.lift(Main::curateInput))
                .map(Try::getSuccess)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .mapToInt(Main::mapCuratedInputToResult)
                .sum();
    }
}