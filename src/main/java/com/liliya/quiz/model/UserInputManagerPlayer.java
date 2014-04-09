package com.liliya.quiz.model;

import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Handles user input for  player functionality
 */

public class UserInputManagerPlayer {

    public int selectQuizToPlayFromList() {
        int choice = -1;
        do {
            try {
                System.out.print(">>");
                Scanner sc = new Scanner(System.in);
                choice = sc.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("You need to enter the quiz number");
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
                System.out.println("Answer must be number of the question.Try again!");
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
        } while (name.trim().equals(""));
        return name;
    }

}
