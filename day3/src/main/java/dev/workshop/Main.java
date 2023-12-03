package dev.workshop;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static final System.Logger logger = System.getLogger(Main.class.getName());

    public static void main(String[] args) throws IOException, InterruptedException {
        int result = Common.run(args, "3", System.getenv("AOC_COOKIE"), Main::solution);
        logger.log(System.Logger.Level.INFO, "Result: " + result);
    }

    public static int solution(String input) {
        return Main.validPartNumbers(input).stream()
                .parallel()
                .mapToInt(Integer::intValue)
                .sum();
    }

    public static List<Integer> validPartNumbers(String input) {
        String[] rows = input.split("\n");
        List<Integer> validPartNumbers = new ArrayList<>();

        for (int i = 0; i < rows.length; i++) {
            int j = 0;
            while (j < rows[i].length()) {
                if (Character.isDigit(rows[i].charAt(j))) {
                    StringBuilder integer = new StringBuilder();
                    boolean isValid = Main.isValid(i, j, rows);
                    integer.append(rows[i].charAt(j)); j++;
                    while(j < rows[i].length() && Character.isDigit(rows[i].charAt(j))) {
                        if (!isValid) {
                            isValid = Main.isValid(i, j, rows);
                        }
                        integer.append(rows[i].charAt(j)); j++;
                    }
                    if (isValid) {
                        validPartNumbers.add(Integer.parseInt(integer.toString()));
                    }
                }
                j++;
            }
        }

        return validPartNumbers;
    }

    private static boolean isValid(int rowIndex, int columnIndex, String[] rows) {
        return (rowIndex - 1 >= 0 && columnIndex - 1 >= 0 && (!Character.isDigit(rows[rowIndex - 1].charAt(columnIndex - 1)) && rows[rowIndex - 1].charAt(columnIndex - 1) != '.'))
                || (rowIndex - 1 >= 0 && (!Character.isDigit(rows[rowIndex - 1].charAt(columnIndex)) && rows[rowIndex - 1].charAt(columnIndex) != '.'))
                || (rowIndex - 1 >= 0 && columnIndex + 1 < rows[rowIndex].length() && (!Character.isDigit(rows[rowIndex - 1].charAt(columnIndex + 1)) && rows[rowIndex - 1].charAt(columnIndex + 1) != '.'))
                || (columnIndex - 1 >= 0 && (!Character.isDigit(rows[rowIndex].charAt(columnIndex - 1)) && rows[rowIndex].charAt(columnIndex - 1) != '.'))
                || (columnIndex + 1 < rows[rowIndex].length() && (!Character.isDigit(rows[rowIndex].charAt(columnIndex + 1)) && rows[rowIndex].charAt(columnIndex + 1) != '.'))
                || (rowIndex + 1 < rows.length && columnIndex - 1 >= 0 && (!Character.isDigit(rows[rowIndex + 1].charAt(columnIndex - 1)) && rows[rowIndex + 1].charAt(columnIndex - 1) != '.'))
                || (rowIndex + 1 < rows.length && (!Character.isDigit(rows[rowIndex + 1].charAt(columnIndex)) && rows[rowIndex + 1].charAt(columnIndex) != '.'))
                || (rowIndex + 1 < rows.length && columnIndex + 1 < rows[rowIndex].length() && (!Character.isDigit(rows[rowIndex + 1].charAt(columnIndex + 1)) && rows[rowIndex + 1].charAt(columnIndex + 1) != '.'));
    }
}