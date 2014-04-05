package com.liliya.quiz.model;

import java.io.Serializable;
import java.util.Map;

public class QuestionImpl implements Question, Serializable {

    private String question;
    //key will be 1,2,3,4
    private Map<Integer, String> possibleAnswers;
    private int correctAnswer;
    private int correctAnswerPoints;

    public QuestionImpl(){
        //no args constructor for serialization
    }

    public QuestionImpl(String question, Map<Integer, String> possibleAnswers, int correctAnswer, int correctAnswerPoints) {
        this.question = question;
        this.possibleAnswers = possibleAnswers;
        this.correctAnswer = correctAnswer;
        this.correctAnswerPoints = correctAnswerPoints;
    }

    @Override
    public String getQuestion() {
        return this.question;
    }

    @Override
    public void setQuestion(String question) {
        this.question = question;
    }

    @Override
    public Map<Integer, String> getPossibleAnswers() {
        return possibleAnswers;
    }

    @Override
    public void setPossibleAnswers(Map<Integer, String> possibleAnswers) {

        this.possibleAnswers = possibleAnswers;
    }

    @Override
    public int getCorrectAnswer() {

        return correctAnswer;
    }

    @Override
    public void setCorrectAnswer(int answer) {

        this.correctAnswer = answer;

    }

    @Override
    public void setCorrectAnswerPoints(int points) {

        this.correctAnswerPoints = points;
    }

    @Override
    public int getCorrectAnswerPoints() {
        return correctAnswerPoints;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        QuestionImpl question1 = (QuestionImpl) o;

        if (correctAnswerPoints != question1.correctAnswerPoints) return false;
        if (correctAnswer != question1.correctAnswer) return false;
        if (possibleAnswers != null ? !possibleAnswers.equals(question1.possibleAnswers) : question1.possibleAnswers != null)
            return false;
        if (question != null ? !question.equals(question1.question) : question1.question != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = question != null ? question.hashCode() : 0;
        result = 31 * result + (possibleAnswers != null ? possibleAnswers.hashCode() : 0);
        result = 31 * result + correctAnswer;
        result = 31 * result + correctAnswerPoints;
        return result;
    }
}
