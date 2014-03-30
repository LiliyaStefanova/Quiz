package com.liliya.quiz.server;

import com.liliya.quiz.model.PlayerQuizInstance;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: liliya
 * Date: 30/03/14
 * Time: 14:43
 * To change this template use File | Settings | File Templates.
 */
public class HighScores {

    private static List<PlayerQuizInstance> scores = new ArrayList<PlayerQuizInstance>();


    public static PlayerQuizInstance getTopPlayerScore(String name) {
        int maxScore = 0;
        PlayerQuizInstance winner = new PlayerQuizInstance();
        for (PlayerQuizInstance game : scores) {
            if (game.getPlayer().getName().equals(name)) {
                if (game.getTotalScore() > maxScore) {
                    maxScore = game.getTotalScore();
                    winner = game;
                }
            }
        }
        return winner;
    }

    public static PlayerQuizInstance getTopQuizScore(int id) {
        PlayerQuizInstance winner = null;
        List<PlayerQuizInstance> singleQuizScores = new ArrayList<PlayerQuizInstance>();
        try{
        for (PlayerQuizInstance game : scores) {
            if (game.getQuiz().getQuizId() == id) {
                singleQuizScores.add(game);
            }
        }

         } catch (NullPointerException ex){
            System.out.println("No one has played this quiz!");
         }

        winner = Collections.max(singleQuizScores, new Comparator<PlayerQuizInstance>() {
            @Override
            public int compare(PlayerQuizInstance o1, PlayerQuizInstance o2) {
                if (o1.getTotalScore() > o2.getTotalScore()) {
                    return 1;
                } else if ((o1.getTotalScore() < o2.getTotalScore())) {
                    return -1;
                } else return 0;
            }
        });
        return winner;
    }

    public static void updateHighScores(PlayerQuizInstance newScore) {

        scores.add(newScore);
    }

    public static List<PlayerQuizInstance> getHighScores() {
        return scores;
    }


}
