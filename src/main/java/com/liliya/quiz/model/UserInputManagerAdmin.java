package com.liliya.quiz.model;

import com.liliya.constants.ExceptionMsg;
import com.liliya.constants.UserDialog;
import com.liliya.constants.UserDialog;
import com.liliya.exceptions.ChangedMyMindException;
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
                Scanner sc = new Scanner(System.in);
                quizName = sc.nextLine();
                checkInterruptionRequired(quizName);
            } catch (IllegalArgumentException ex) {
                System.out.println(ExceptionMsg.PROVIDE_QUIZ_NAME);
            }
        } while (quizName.trim().isEmpty());
        return quizName;
    }

    public int selectQuizToCloseFromList() {
        String choiceS = "";
        int choice = -1;
        do {
            try {
                System.out.print(">>");
                Scanner sc = new Scanner(System.in);
                choiceS = sc.nextLine();
                checkInterruptionRequired(choice + "");
                choice = Integer.parseInt(choiceS);

            } catch (InputMismatchException e) {
                throw new RuntimeException(ExceptionMsg.ENTER_QUIZ_NUMBER, e);
            }
        } while (choice == -1);

        return choice;
    }

    public int setNumberOfQuestions() {
        try {
            System.out.print(UserDialog.NUM_QUESTIONS);
            String choiceS = "";
            Scanner sc = new Scanner(System.in);
            choiceS = sc.nextLine();
            checkInterruptionRequired(choiceS);
            return Integer.parseInt(choiceS);
        } catch (InputMismatchException ex) {
            throw new RuntimeException(ExceptionMsg.NUM_QUES_REQ, ex);
        }
    }

    public String provideQuestion() {
        String question = "";
        do {
            try {
                System.out.print(UserDialog.ENTER_QUESTION);
                Scanner sc = new Scanner(System.in);
                question = sc.nextLine();
                checkInterruptionRequired(question);
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

                    Scanner sc = new Scanner(System.in);
                    answer = sc.nextLine();
                    checkInterruptionRequired(answer);
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
        String choiceS = "";
        do {
            try {
                System.out.print(UserDialog.ENTER_CORR_ANS);
                Scanner sc = new Scanner(System.in);
                choiceS = sc.nextLine();
                checkInterruptionRequired(choiceS);
                correctAnswer = Integer.parseInt(choiceS);

            } catch (InputMismatchException ex) {
                System.out.println(ExceptionMsg.NUM_CORR_ANS);
            }
        } while (correctAnswer < 1 || correctAnswer > 4);
        return correctAnswer;
    }

    //a question can never have 0 points for a correct answer
    public int provideCorrectAnswerPoints() {
        int correctAnswerPoints = 0;
        String choiceS = "";
        do {
            try {
                System.out.print(UserDialog.ENTER_CORR_ANS_POINTS);
                Scanner sc = new Scanner(System.in);
                choiceS = sc.nextLine();
                checkInterruptionRequired(choiceS);
                correctAnswerPoints = Integer.parseInt(choiceS);
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
                Scanner sc = new Scanner(System.in);
                filename = sc.nextLine();
                checkInterruptionRequired(filename);
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

    public void checkInterruptionRequired(String input) {
        if (input.equals("X")) {
            throw new ChangedMyMindException();
        }
    }
}
