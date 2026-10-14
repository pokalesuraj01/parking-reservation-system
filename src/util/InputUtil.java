package util;

import java.util.Scanner;

/** Reads console input without mixing token-based and line-based Scanner methods. */
public class InputUtil {
    private final Scanner scanner;

    public InputUtil() {
        this(new Scanner(System.in));
    }

    public InputUtil(Scanner scanner) {
        if (scanner == null) {
            throw new IllegalArgumentException("Scanner is required.");
        }
        this.scanner = scanner;
    }

    public String readString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    public int readInt(String prompt) {
        while (true) {
            String value = readString(prompt);
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException exception) {
                System.out.println("Please enter a whole number.");
            }
        }
    }

    public int readMenuChoice(String prompt) {
        return readInt(prompt);
    }
}
