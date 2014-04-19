package com.liliya.quiz.model;

import com.liliya.constants.ExceptionMsg;
import com.liliya.constants.UserDialog;
import com.liliya.exceptions.ChangedMyMindException;
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
        String choiceS = "";
        int choice = -1;
        do {
            try {
                System.out.print(">>");
                Scanner sc = new Scanner(System.in);
                choiceS = sc.nextLine();
                checkInterruptionRequired(choiceS);
                choice = Integer.parseInt(choiceS);

            } catch (InputMismatchException e) {
                throw new RuntimeException(ExceptionMsg.ENTER_QUIZ_NUMBER, e);
            }
        } while (choice == -1);

        return choice;
    }

    public int provideSelectedAnswer() {
        int playerGuess = 0;
        String choiceS = "";
        do {
            try {
                System.out.print(UserDialog.ANSWER_PROMPT);
                Scanner sc = new Scanner(System.in);
                choiceS = sc.nextLine();
                checkInterruptionRequired(choiceS);
                playerGuess = Integer.parseInt(choiceS);
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
            System.out.print(UserDialog.ENTER_NAME);
            Scanner sc = new Scanner(System.in);
            name = sc.nextLine();
            checkInterruptionRequired(name);
            System.out.println();
        } while (name.trim().isEmpty());
        return name;
    }

    public String confirmExit() {
        String userInput = "";
        do {
            System.out.print(UserDialog.CONFIRM_QUIT);
            try {
                Scanner sc1 = new Scanner(System.in);
                userInput = sc1.nextLine();
            } catch (InputMismatchException ex) {
                System.out.println(ExceptionMsg.YN);
            }
        } while (userInput.trim().isEmpty());

        return userInput;
    }

    public void checkInterruptionRequired(String input) {
        if (input.equals("X")) {
            throw new ChangedMyMindException();
        }
    }

}
