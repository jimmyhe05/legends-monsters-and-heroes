package legends.utilities;

import java.util.Scanner;

public class InputUtil {

	private static final Scanner SCANNER = new Scanner(System.in);

	public static int readInt() {
		while (true) {
			String line = SCANNER.nextLine();
			try {
				return Integer.parseInt(line.trim());
			} catch (NumberFormatException e) {
				System.out.print("Please enter a valid integer: ");
			}
		}
	}

	public static int readIntInRange(String prompt, int min, int max) {
		System.out.print(prompt);
		while (true) {
			int value = readInt();
			if (value >= min && value <= max) {
				return value;
			}
			System.out.print("Please enter a number between " + min + " and " + max + ": ");
		}
	}
}
