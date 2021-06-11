package BusinessLayer.util;

import BusinessLayer.Tiles.Player;

public class PredeterminedInputProvider implements InputProvider {
    private static InputProvider inputProvider = null;
    private static UserAction action;

    private PredeterminedInputProvider(UserAction action) {
        PredeterminedInputProvider.action = action;
    }

    public static InputProvider getInputProvider(UserAction action) {
        if (inputProvider == null) {
            inputProvider = new PredeterminedInputProvider(action);
        }
        return inputProvider;
    }

    public void getAction(Player player) {
            switch (action) {
                case DO_NOTHING -> {
                    player.doNothing();
                }
                case MOVE_UP -> {
                    player.moveUp();
                }
                case CAST_ABILITY -> {
                    player.castAbility();
                }
                case MOVE_LEFT -> {
                    player.moveLeft();
                }
                case MOVE_DOWN -> {
                    player.moveDown();
                }
                case MOVE_RIGHT -> {
                    player.moveRight();
                }
            }
        }
}