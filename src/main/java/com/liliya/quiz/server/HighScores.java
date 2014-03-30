package com.liliya.quiz.server;

import com.liliya.quiz.model.PlayerQuizInstance;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: liliya
 * Date: 30/03/14
 * Time: 14:43
 * To change this template use File | Settings | File Templates.
 */
public class HighScores {

    private static List<PlayerQuizInstance> scores=new ArrayList<PlayerQuizInstance>();


    public static   PlayerQuizInstance getTopPlayerScore(String name){
        int maxScore=0;
        PlayerQuizInstance winner=new PlayerQuizInstance();
        for(PlayerQuizInstance game: scores){
            if(game.getPlayer().getName().equals(name)){
                if(game.getTotalScore()>maxScore){
                    maxScore=game.getTotalScore();
                    winner=game;
                }
            }
        }
        return winner;
    }

    public static PlayerQuizInstance getTopQuizScore(int id){
        int maxScore=0;
        PlayerQuizInstance winner=new PlayerQuizInstance();
       // try{
            for(PlayerQuizInstance game: scores){
                if(game.getQuiz().getQuizId()==id){
                    if(game.getTotalScore()>maxScore){
                        maxScore=game.getTotalScore();
                        System.out.println("Max score is: "+maxScore);
                    }
                }
            }
            for(PlayerQuizInstance game: scores){
                if(game.getTotalScore()==maxScore){
                    winner=game;
                }
            }
       // } catch (NullPointerException ex){
           // System.out.println("No one has played this quiz!");
       // }
        return winner;
    }

    public static void updateHighScores(PlayerQuizInstance newScore){

        scores.add(newScore);
    }

    public static List<PlayerQuizInstance> getHighScores() {
        return scores;
    }

}
