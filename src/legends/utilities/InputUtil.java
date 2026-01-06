package legends.utilities;

import java.util.Scanner;

/**
 * Simple input helper for validated integer reads from System.in.
 */
public class InputUtil {

	private static final Scanner SCANNER = new Scanner(System.in);

	/**
	 * Read an integer from standard input, reprompting until a valid integer is entered.
	 * 
	 * @return the integer read from input
	 */
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

	/**
	 * Read an integer within a specified range from standard input, reprompting until valid.
	 * 
	 * @param prompt the prompt to display to the user
	 * @param min the minimum acceptable value (inclusive)
	 * @param max the maximum acceptable value (inclusive)
	 * @return the integer read from input within the specified range
	 */
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
