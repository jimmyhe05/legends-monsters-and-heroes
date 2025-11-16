package legends.game;

import legends.utilities.InputUtil;

public class GameController {

    private final Game game;

    public GameController() {
        this.game = new Game();
    }

    public void start() {
        printWelcome();

        boolean running = true;
        while (running) {
            printMainMenu();
            int choice = InputUtil.readIntInRange("Enter choice: ", 1, 3);

            switch (choice) {
                case 1:
                    startNewGame();
                    break;
                case 2:
                    System.out.println("Feature not implemented yet.");
                    break;
                case 3:
                    System.out.println("Goodbye!");
                    running = false;
                    break;
            }
        }
    }

    private void startNewGame() {
        game.setup();
        game.run();
    }

    private void printWelcome() {
        System.out.println("====================================");
        System.out.println("   Legends: Monsters and Heroes");
        System.out.println("====================================");
    }

    private void printMainMenu() {
        System.out.println();
        System.out.println("1) Start new game");
        System.out.println("2) Other options");
        System.out.println("3) Quit");
    }
}