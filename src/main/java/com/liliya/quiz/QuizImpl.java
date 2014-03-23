package com.liliya.quiz;

import java.io.Serializable;
import java.util.*;

public class QuizImpl implements Quiz, Serializable {

    private String quizName;
    private int quizId;
    private Set<Question> quizQuestions;
    //set to false once quiz is closed by user
    private boolean quizActive;

    public QuizImpl(String name, Question [] questions){
        this.quizName=name;
        this.quizQuestions=convertQuestionToSet(questions);
        this.quizId=QuizIDGenerator.getNewID();
        quizActive=true;
    }

    @Override
    public String getQuizName() {
        return this.quizName;
    }

    @Override
    public void setQuizName(String name) {
        this.quizName=name;
    }

    @Override
    public int getQuizId() {
        return this.quizId;
    }

    @Override
    public Set<Question> getQuizQuestions() {

        return quizQuestions;
    }

    @Override
    public boolean getQuizState() {
        return this.quizActive;
    }

    @Override
    public void setQuizState(boolean state) {
        this.quizActive=state;
    }

  /*  @Override
    public void changeQuizQuestions(String searchString) {
        //To change body of implemented methods use File | Settings | File Templates.
    }*/


    private Set<Question> convertQuestionToSet(Question [] questions){
        Set<Question> quizQuestions=new HashSet<Question>();
        for(Question curr:questions){
            quizQuestions.add(curr);
        }
        return quizQuestions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        QuizImpl quiz = (QuizImpl) o;

        if (quizId != quiz.quizId) return false;
        if (quizName != null ? !quizName.equals(quiz.quizName) : quiz.quizName != null) return false;
        if (quizQuestions != null ? !quizQuestions.equals(quiz.quizQuestions) : quiz.quizQuestions != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = quizName != null ? quizName.hashCode() : 0;
        result = 31 * result + quizId;
        result = 31 * result + (quizQuestions != null ? quizQuestions.hashCode() : 0);
        return result;
    }
}
