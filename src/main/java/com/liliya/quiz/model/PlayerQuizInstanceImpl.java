package com.liliya.quiz.model;

import java.io.Serializable;

public class PlayerQuizInstanceImpl implements PlayerQuizInstance, Serializable {

    private Player player;
    private Quiz quiz;
    private int totalScore;
    private boolean quizPlayed;


    public PlayerQuizInstanceImpl() {
        //no args constructor for serialization
    }

    public PlayerQuizInstanceImpl(Player player, Quiz quiz) {
        this.player = player;
        this.quiz = quiz;
        quizPlayed = true;
    }

    public int getTotalScore() {
        return this.totalScore;
    }

    public boolean isQuizPlayed() {
        return quizPlayed;
    }

    public void setQuizPlayed(boolean quizPlayed) {
        this.quizPlayed = quizPlayed;
    }

    public void setTotalScore(int score) {
        this.totalScore = score;
    }

    public Player getPlayer() {
        return this.player;
    }

    public Quiz getQuiz() {
        return this.quiz;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PlayerQuizInstance that = (PlayerQuizInstance) o;

        if (totalScore != that.getTotalScore()) return false;
        if (player != null ? !player.equals(that.getPlayer()) : that.getPlayer() != null) return false;
        if (quiz != null ? !quiz.equals(that.getQuiz()) : that.getQuiz() != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = player != null ? player.hashCode() : 0;
        result = 31 * result + (quiz != null ? quiz.hashCode() : 0);
        result = 31 * result + totalScore;
        return result;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
    }

}
