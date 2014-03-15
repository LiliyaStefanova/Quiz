package com.liliya.quiz;

import java.util.*;

public class QuizImpl implements Quiz {

    private String quizName;
    private int quizId;
    private Set<Question> quizQuestions;

    public QuizImpl(String name, Question [] questions){
        this.quizName=name;
        this.quizQuestions=convertQuestionToSet(questions);
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
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Set<Question> getQuizQuestions() {

        return quizQuestions;
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
}
