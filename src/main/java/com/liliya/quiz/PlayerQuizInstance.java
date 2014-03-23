package com.liliya.quiz;

import java.io.Serializable;

public class PlayerQuizInstance implements Serializable {

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PlayerQuizInstance that = (PlayerQuizInstance) o;

        if (totalScore != that.totalScore) return false;
        if (player != null ? !player.equals(that.player) : that.player != null) return false;
        if (quiz != null ? !quiz.equals(that.quiz) : that.quiz != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = player != null ? player.hashCode() : 0;
        result = 31 * result + (quiz != null ? quiz.hashCode() : 0);
        result = 31 * result + totalScore;
        return result;
    }
}
