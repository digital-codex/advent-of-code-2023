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
        }

        String input = Main.readInput(args[0]);
        int result = Main.solution(input.split("\n"));

        System.out.printf("Result: %d", result);
    }

    private static String readInput(String path) throws IOException {
        return Files.readString(Paths.get(path), Charset.defaultCharset());
    }

    private static int solution(String[] splitInput) {
        int[] calibrationsValues = new int[splitInput.length];

        for (int i = 0; i < splitInput.length; i++) {
            String input = splitInput[i];
            StringBuilder value = new StringBuilder();
            for (int j = 0; j < input.length(); j++) {
                char c = input.charAt(j);
                if (Character.isDigit(c)) {
                    value.append(c);
                    break;
                }
            }

            for (int j = input.length() - 1; j >= 0; j--) {
                char c = input.charAt(j);
                if (Character.isDigit(c)) {
                    value.append(c);
                    break;
                }
            }

            calibrationsValues[i] = Integer.parseInt(value.toString());
        }

        return Arrays.stream(calibrationsValues).sum();
    }
}