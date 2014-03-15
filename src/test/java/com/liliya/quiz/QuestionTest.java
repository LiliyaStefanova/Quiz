package com.liliya.quiz;

import org.junit.*;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class QuestionTest {

    Question newQuestion;

    @Before
    public void buildUp(){
        String question="What is the capital of Australia?";
        String [] possibleAnswers={"Canberra", "Melbourne", "Darwin", "Singapore" };
        String correctAnswer="Canberra";
        int pointsAwarded=5;
        newQuestion=new QuestionImpl(question, possibleAnswers, correctAnswer, pointsAwarded);
    }

    @After
    public void cleanUp(){
        newQuestion=null;
    }
    @Test
    public void getQuestionTest(){
        assertEquals("What is the capital of Australia?", newQuestion.getQuestion());
    }

    @Test
    public void setQuestionTest(){
        String question="What is the capital of Australia?";
        String [] possibleAnswers={"Canberra", "Melbourne", "Darwin", "Singapore" };
        String correctAnswer="Canberra";
        int pointsAwarded=5;
        Question newQuestion=new QuestionImpl(question, possibleAnswers, correctAnswer, pointsAwarded);
        newQuestion.setQuestion("What is the north-most city?");
        assertEquals("What is the north-most city?", newQuestion.getQuestion());
    }

    @Test
    public void getPossibleAnswersTest(){
        Map possibleAnswersExpected=new HashMap<String, String>();
        possibleAnswersExpected.put("a","Canberra");
        possibleAnswersExpected.put("b","Melbourne");
        possibleAnswersExpected.put("c","Darwin");
        possibleAnswersExpected.put("d","Singapore");

        assertEquals(possibleAnswersExpected, newQuestion.getPossibleAnswers());
    }

    @Test
    public void getCorrectAnswer(){

        assertEquals("Canberra", newQuestion.getCorrectAnswer());
    }

    @Test
    public void setCorrectAnswer(){
        String question="What is the capital of Australia?";
        String [] possibleAnswers={"Canberra", "Melbourne", "Darwin", "Singapore" };
        String correctAnswer="Canberra";
        int pointsAwarded=5;
        Question newQuestion=new QuestionImpl(question, possibleAnswers, correctAnswer, pointsAwarded);
        newQuestion.setCorrectAnswer("Melbourne");

        assertEquals("Melbourne", newQuestion.getCorrectAnswer());
    }


    @Test
    public void getCorrectAnswerPoint(){

        assertEquals(5, newQuestion.getCorrectAnswerPoints());
    }

    @Test
    public void setCorrectAnswerPoints(){
        String question="What is the capital of Australia?";
        String [] possibleAnswers={"Canberra", "Melbourne", "Darwin", "Singapore" };
        String correctAnswer="Canberra";
        int pointsAwarded=5;
        Question newQuestion=new QuestionImpl(question, possibleAnswers, correctAnswer, pointsAwarded);
        newQuestion.setCorrectAnswerPoints(10);

        assertEquals(10, newQuestion.getCorrectAnswerPoints());
    }



}
