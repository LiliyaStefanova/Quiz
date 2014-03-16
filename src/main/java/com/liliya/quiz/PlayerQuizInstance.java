package com.liliya.quiz;

/**
 * Created with IntelliJ IDEA.
 * User: liliya
 * Date: 16/03/14
 * Time: 22:47
 * To change this template use File | Settings | File Templates.
 */
public class PlayerQuizInstance {

    private Player player;
    private Quiz quiz;
    private int totalScore;

    public PlayerQuizInstance(Player player, Quiz quiz){
        this.player=player;
        this.quiz=quiz;
    }

    public int getTotalScore(){
        return  this.totalScore;
    }

    public void setTotalScore(int score){
        this.totalScore=score;
    }

    public Player getPlayer(){
        return this.player;
    }

    public Quiz getQuiz(){
        return this.quiz;
    }

}
