package com.liliya.quiz.model;


import com.liliya.menu.MenuActions;
import com.liliya.menu.TextMenu;
import com.liliya.menu.TextMenuItem;
import com.liliya.quiz.client.QuizPlayerClient;
import com.liliya.quiz.client.QuizPlayerClientImpl;
import com.liliya.quiz.client.QuizSetUpClient;
import com.liliya.quiz.client.QuizSetUpClientImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QuizLauncher {

    private static final TextMenuItem quizAdmin = new TextMenuItem("Quiz Administration", MenuActions.QUIZ_ADMINISTRATION);
    private static final TextMenuItem playQuiz = new TextMenuItem("Play quiz", MenuActions.PLAY_QUIZ);
    private static final TextMenuItem quit = new TextMenuItem("Quit", MenuActions.QUIT);

    private static List<TextMenuItem> mainMenu = new ArrayList<TextMenuItem>(Arrays.asList(quizAdmin, playQuiz, quit));

    public static void main(String[] args) {

        QuizLauncher.launch();

    }

    public static void launch() {

        MenuActions action = TextMenu.display("Main Menu", mainMenu);

        switch (action) {
            case QUIZ_ADMINISTRATION:
                QuizSetUpClient suc = new QuizSetUpClientImpl();
                suc.menu();
                break;
            case PLAY_QUIZ:
                QuizPlayerClient newPlayerClient = new QuizPlayerClientImpl();
                newPlayerClient.launchMainMenuPlayer();
                break;
            case QUIT:
                try {
                    // server.flush();
                } catch (NullPointerException ex) {
                    System.out.println("Server does not exist");
                }
                System.out.println("Goodbye!");
                System.exit(0);
                break;
            default:
                System.out.println("Please enter a valid option");
        }


    }


}
