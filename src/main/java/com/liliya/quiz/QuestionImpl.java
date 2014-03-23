package com.liliya.quiz;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: liliya
 * Date: 15/03/14
 * Time: 12:56
 * To change this template use File | Settings | File Templates.
 */
public class QuestionImpl implements Question, Serializable {

    private String question;
    //key will be a, b,c, d etc
    private Map<String, String> possibleAnswers;
    private String correctAnswer;
    private int correctAnswerPoints;

    public QuestionImpl(String question, String [] answers, String correctAnswer, int correctAnswerPoints){
        this.question=question;
        this.possibleAnswers=convertAnswersListToQuizFormat(answers);
        this.correctAnswer=correctAnswer;
        this.correctAnswerPoints=correctAnswerPoints;
    }

    @Override
    public String getQuestion() {
        return this.question;
    }

    @Override
    public void setQuestion(String question) {
            this.question=question;
    }

    @Override
    public Map<String, String> getPossibleAnswers() {
        return possibleAnswers;
    }

    @Override
    public void setPossibleAnswers(String[] possibleAnswers) {

        this.possibleAnswers=convertAnswersListToQuizFormat(possibleAnswers);
    }

    @Override
    public String getCorrectAnswer() {
        return this.correctAnswer;
    }

    @Override
    public void setCorrectAnswer(String answer) {

        this.correctAnswer=answer;

    }

    @Override
    public void setCorrectAnswerPoints(int points) {

        this.correctAnswerPoints=points;
    }

    @Override
    public int getCorrectAnswerPoints() {
        return correctAnswerPoints;
    }

    private Map<String, String> convertAnswersListToQuizFormat(String [] answers){
        String [] questionIdentifiers={"a", "b", "c", "d"};
        Map<String, String> possibleAnswers=new HashMap<String, String>();
        possibleAnswers.put(questionIdentifiers[0],answers[0]);
        possibleAnswers.put(questionIdentifiers[1],answers[1]);
        possibleAnswers.put(questionIdentifiers[2],answers[2]);
        possibleAnswers.put(questionIdentifiers[3],answers[3]);
    //will use fixed number of possible answers to begin with
        return possibleAnswers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        QuestionImpl question1 = (QuestionImpl) o;

        if (correctAnswer != null ? !correctAnswer.equals(question1.correctAnswer) : question1.correctAnswer != null)
            return false;
        if (possibleAnswers != null ? !possibleAnswers.equals(question1.possibleAnswers) : question1.possibleAnswers != null)
            return false;
        if (question != null ? !question.equals(question1.question) : question1.question != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = question != null ? question.hashCode() : 0;
        result = 31 * result + (possibleAnswers != null ? possibleAnswers.hashCode() : 0);
        result = 31 * result + (correctAnswer != null ? correctAnswer.hashCode() : 0);
        return result;
    }
}
