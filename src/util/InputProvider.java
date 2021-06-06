package util;

import BusinessLayer.Player;

import java.util.Scanner;

public class InputProvider {

    private static InputProvider inputProvider = null;

    //fields
    private final Scanner reader;

    private InputProvider(){
        reader = new Scanner(System.in);
    }

    public static InputProvider getInputProvider() {
        if (inputProvider == null){
            inputProvider = new InputProvider();
        }
        return inputProvider;
    }

    public void getAction(Player player){
        while (true) {
            String input = reader.nextLine();
            input = input.trim().replaceAll("\\s+", " ");
            String[] words = input.split("\\s");
            for (String word: words){
                switch(word)
                {
                    case "q":
                        player.doNothing();
                        return;
                    case "w":
                        player.moveUp();
                        return;
                    case "e":
                        player.castAbility();
                        return;
                    case "a":
                        player.moveLeft();
                        return;
                    case "s":
                        player.moveDown();
                        return;
                    case "d":
                        player.moveRight();
                        return;
                }
            }
        }
    }
}
