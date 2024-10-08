package BusinessLayer.util;

import BusinessLayer.Tiles.Player;

import java.util.Scanner;

public class TrueInputProvider implements InputProvider{
    private static InputProvider inputProvider = null;
    private static final Scanner reader = new Scanner(System.in);

    public static InputProvider getInputProvider() {
        if (inputProvider == null) {
            inputProvider = new TrueInputProvider();
        }
        return inputProvider;
    }

    public void getAction(Player player) {
        while (true) {
            switch (reader.next().trim()) {
                case "q" -> {
                    player.doNothing();
                    return;
                }
                case "w" -> {
                    player.moveUp();
                    return;
                }
                case "e" -> {
                    player.castAbility();
                    return;
                }
                case "a" -> {
                    player.moveLeft();
                    return;
                }
                case "s" -> {
                    player.moveDown();
                    return;
                }
                case "d" -> {
                    player.moveRight();
                    return;
                }
            }
        }
    }
}