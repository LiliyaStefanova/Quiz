package com.liliya.quiz.model;

import com.liliya.constants.ExceptionMsg;
import com.liliya.menu.MenuActions;
import com.liliya.menu.TextMenu;
import com.liliya.menu.TextMenuItem;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

/**
 * Handles user input for  player functionality
 */

public class UserInputManagerPlayer {

    public MenuActions showMenu(String menuName, List<TextMenuItem> textMenuItems) {
        return TextMenu.display(menuName, textMenuItems);
    }

    public int selectQuizToPlayFromList() {
        int choice = -1;
        do {
            try {
                System.out.print(">>");
                Scanner sc = new Scanner(System.in);
                choice = sc.nextInt();
            } catch (InputMismatchException e) {
                System.out.println(ExceptionMsg.QUIZ_ID_NUM);
            }
        } while (choice == -1);

        return choice;
    }

    public int provideSelectedAnswer() {
        int playerGuess = 0;
        do {
            try {
                System.out.print("Your answer(type the answer number): ");
                Scanner sc = new Scanner(System.in);
                playerGuess = sc.nextInt();
            } catch (InputMismatchException ex) {
                System.out.println(ExceptionMsg.ANS_NUM_TRY);
            }
        } while (!answerRangeValidationCheck(playerGuess) || playerGuess == 0);

        return playerGuess;
    }

    private boolean answerRangeValidationCheck(int answer) {
        boolean correct = true;
        if (answer < 1 || answer > 4) {
            correct = false;
        }
        return correct;
    }

    public String providePlayerName() {
        String name = "";
        do {
            System.out.print("Enter your name: ");
            Scanner sc = new Scanner(System.in);
            name = sc.nextLine();
            System.out.println();
        } while (name.trim().isEmpty());
        return name;
    }

    public String confirmExit() {
        String userInput = "";
        do {
            System.out.print("Are you sure you want to quit(y/n)?:");
            try {
                Scanner sc1 = new Scanner(System.in);
                userInput = sc1.nextLine();
            } catch (InputMismatchException ex) {
                System.out.println(ExceptionMsg.YN);
            }
        } while (userInput.trim().isEmpty());

        return userInput;
    }

}
