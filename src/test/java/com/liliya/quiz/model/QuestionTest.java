package com.liliya.quiz.model;

import com.liliya.quiz.model.Question;
import com.liliya.quiz.model.QuestionImpl;
import org.junit.*;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class QuestionTest {

    Question newQuestion;

    @Before
    public void buildUp(){
        String question="What is the capital of Australia?";
        Map<Integer, String> possibleAnswers1=new HashMap<Integer, String>();
        possibleAnswers1.put(0, "Canberra");
        possibleAnswers1.put(1, "Melbourne");
        possibleAnswers1.put(2, "Darwin");
        possibleAnswers1.put(3, "Singapore");
        int correctAnswer=0;
        int pointsAwarded=5;
        newQuestion=new QuestionImpl(question, possibleAnswers1, correctAnswer, pointsAwarded);
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
        Map<Integer, String> possibleAnswers1=new HashMap<Integer, String>();
        possibleAnswers1.put(0, "Canberra");
        possibleAnswers1.put(1, "Melbourne");
        possibleAnswers1.put(2, "Darwin");
        possibleAnswers1.put(3, "Singapore");
        int correctAnswer=0;
        int pointsAwarded=5;
        Question newQuestion=new QuestionImpl(question, possibleAnswers1, correctAnswer, pointsAwarded);
        newQuestion.setQuestion("What is the north-most city?");
        assertEquals("What is the north-most city?", newQuestion.getQuestion());
    }

    @Test
    public void getPossibleAnswersTest(){
        Map<Integer, String> possibleAnswersExpected=new HashMap<Integer, String>();
        possibleAnswersExpected.put(0,"Canberra");
        possibleAnswersExpected.put(1,"Melbourne");
        possibleAnswersExpected.put(2,"Darwin");
        possibleAnswersExpected.put(3,"Singapore");

        assertEquals(possibleAnswersExpected, newQuestion.getPossibleAnswers());
    }

    @Test
    public void getCorrectAnswer(){

        //assertEquals(0, newQuestion.getCorrectAnswer());
    }

    @Test
    public void setCorrectAnswer(){
        String question="What is the capital of Australia?";
        Map<Integer, String> possibleAnswers1=new HashMap<Integer, String>();
        possibleAnswers1.put(0, "Canberra");
        possibleAnswers1.put(1, "Melbourne");
        possibleAnswers1.put(2, "Darwin");
        possibleAnswers1.put(3, "Singapore");
        int correctAnswer=0;
        int pointsAwarded=5;
        Question newQuestion=new QuestionImpl(question, possibleAnswers1, correctAnswer, pointsAwarded);
        newQuestion.setCorrectAnswer(1);

       // assertEquals(1, newQuestion.getCorrectAnswer());
    }


    @Test
    public void getCorrectAnswerPoint(){

        assertEquals(5, newQuestion.getCorrectAnswerPoints());
    }

    @Test
    public void setCorrectAnswerPoints(){
        String question="What is the capital of Australia?";
        Map<Integer, String> possibleAnswers1=new HashMap<Integer, String>();
        possibleAnswers1.put(0, "Canberra");
        possibleAnswers1.put(1, "Melbourne");
        possibleAnswers1.put(2, "Darwin");
        possibleAnswers1.put(3, "Singapore");
        int correctAnswer=0;
        int pointsAwarded=5;
        Question newQuestion=new QuestionImpl(question, possibleAnswers1, correctAnswer, pointsAwarded);
        newQuestion.setCorrectAnswerPoints(10);

        assertEquals(10, newQuestion.getCorrectAnswerPoints());
    }



}
