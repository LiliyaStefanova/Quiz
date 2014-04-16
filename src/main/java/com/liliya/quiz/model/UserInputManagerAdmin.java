package com.liliya.quiz.model;

import com.liliya.constants.ExceptionMsg;
import com.liliya.constants.UserDialog;
import com.liliya.constants.UserDialog;
import com.liliya.menu.MenuActions;
import com.liliya.menu.TextMenu;
import com.liliya.menu.TextMenuItem;

import java.util.*;

/**
 * Methods which handle user input in the Set up client
 */
public class UserInputManagerAdmin {

    public MenuActions showMenu(String menuName, List<TextMenuItem> textMenuItems) {
        return TextMenu.display(menuName, textMenuItems);
    }

    public String provideQuizName() {
        String quizName = "";
        do {
            try {
                System.out.print(UserDialog.ENTER_QUIZ_NAME);
                Scanner sc1 = new Scanner(System.in);
                quizName = sc1.nextLine();
            } catch (IllegalArgumentException ex) {
                System.out.println(ExceptionMsg.PROVIDE_QUIZ_NAME);
            }
        } while (quizName.trim().isEmpty());
        return quizName;
    }

    public int selectQuizToCloseFromList() {
        int choice = -1;
        do {
            try {
                System.out.print(">>");
                Scanner sc = new Scanner(System.in);
                choice = sc.nextInt();
            } catch (InputMismatchException e) {
                throw new RuntimeException(ExceptionMsg.ENTER_QUIZ_NUMBER, e);
            }
        } while (choice == -1);

        return choice;
    }

    public int setNumberOfQuestions() {
        try {
            System.out.print(UserDialog.NUM_QUESTIONS);
            Scanner sc1 = new Scanner(System.in);
            return sc1.nextInt();
        } catch (InputMismatchException ex) {
            throw new RuntimeException(ExceptionMsg.NUM_QUES_REQ, ex);
        }
    }

    public String provideQuestion() {
        String question = "";
        do {
            try {
                System.out.print(UserDialog.ENTER_QUESTION);
                Scanner sc2 = new Scanner(System.in);
                question = sc2.nextLine();
            } catch (IllegalArgumentException ex) {
                System.out.println(ExceptionMsg.ENTER_QUES);
            }
        } while (question.trim().isEmpty());
        return question;
    }

    public Map<Integer, String> providePossibleAnswers() {
        Map<Integer, String> possibleAnswers = new HashMap<Integer, String>();
        String answer = "";
        System.out.println(UserDialog.ENTER_POSS_ANS);
        for (int i = 1; i <= 4; i++) {
            do {
                System.out.print("Possible answer " + i + ":");
                try {

                    Scanner sc3 = new Scanner(System.in);
                    answer = sc3.nextLine();
                } catch (IllegalArgumentException ex) {
                    System.out.println(ExceptionMsg.POSS_ANS_ENTRY);
                }
                possibleAnswers.put(i, answer);

            } while (answer.trim().isEmpty());
        }
        return possibleAnswers;
    }

    public int provideCorrectAnswer() {
        int correctAnswer = 0;
        do {
            try {
                System.out.print(UserDialog.ENTER_CORR_ANS);
                Scanner sc4 = new Scanner(System.in);
                correctAnswer = sc4.nextInt();
            } catch (InputMismatchException ex) {
                System.out.println(ExceptionMsg.NUM_CORR_ANS);
            }
        } while (correctAnswer < 1 || correctAnswer > 4);
        return correctAnswer;
    }

    //a question can never have 0 points for a correct answer
    public int provideCorrectAnswerPoints() {
        int correctAnswerPoints = 0;
        do {
            try {
                System.out.print(UserDialog.ENTER_CORR_ANS_POINTS);
                Scanner sc5 = new Scanner(System.in);
                correctAnswerPoints = sc5.nextInt();
            } catch (InputMismatchException ex) {
                System.out.println(ExceptionMsg.ANS_POINTS);
            }
        } while (correctAnswerPoints == 0);
        return correctAnswerPoints;
    }

    public String inputFileName() {
        String filename = "";
        System.out.print(UserDialog.ENTER_PATH);
        do {
            try {
                Scanner sc1 = new Scanner(System.in);
                filename = sc1.nextLine();
            } catch (InputMismatchException ex) {
                throw new RuntimeException(ExceptionMsg.INVALID_PATH);
            }
        } while (filename.trim().isEmpty());
        return filename;
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
}
