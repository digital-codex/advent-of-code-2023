package dev.workshop;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.function.Predicate;

public class Main {
    public static final System.Logger logger = System.getLogger(Main.class.getName());

    public static void main(String[] args) throws IOException, InterruptedException {
        String input = (args.length == 1)
                ? Common.readInputFromFile(Paths.get(args[0]))
                : Common.readInputFromSource("4", System.getenv("AOC_COOKIE"));

        int result = Main.solution(input.split("\n"));
        logger.log(System.Logger.Level.INFO, "Result: " + result);
    }

    private static int solution(String[] splitInput) {
        int ret = 0;
        for (String input : splitInput) {
            int result = 0;
            String[] intermediate = input.split("\\|");
            int[] nums = Arrays.stream(intermediate[0].trim().split(":")[1].trim().split(" ")).filter(Predicate.not(String::isEmpty)).map(String::trim).mapToInt(Integer::parseInt).toArray();
            int[] values = Arrays.stream(intermediate[1].trim().split(" ")).filter(Predicate.not(String::isEmpty)).map(String::trim).mapToInt(Integer::parseInt).toArray();

            for (int value : values) {
                if (Main.contains(nums, value)) {
                    result = (result == 0) ? 1 : result * 2;
                }
            }
            ret += result;
        }
        return ret;
    }

    private static boolean contains(int[] arr, int val) {
        for (int i : arr) {
            if (i == val) return true;
        }
        return false;
    }
}