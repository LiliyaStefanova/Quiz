package com.liliya.quiz.model;

import org.junit.*;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;


public class QuizTest {

    Question questionCapital;
    Question questionRiver;

    Quiz newQuiz;

    @Before
    public void buildUp(){

        String question1="What is the capital of Australia?";
        String question2="What is the longest rive in the world?";
        int correctAnswer1=0;
        int correctAnswer2=0;
        int pointsAwarded1=5;
        int pointsAwarded2=7;
        Map<Integer, Question> questionMap=new HashMap<Integer, Question>();
        Map<Integer, String> possibleAnswers1=new HashMap<Integer, String>();
        possibleAnswers1.put(0, "Canberra");
        possibleAnswers1.put(1, "Melbourne");
        possibleAnswers1.put(2, "Darwin");
        possibleAnswers1.put(3, "Singapore");
        Map<Integer, String> possibleAnswers2=new HashMap<Integer, String>();
        possibleAnswers2.put(0, "The Nile");
        possibleAnswers2.put(1, "The Amazon");
        possibleAnswers2.put(2, "Orinoko");
        possibleAnswers2.put(3,"Mississippi");
        questionCapital=new QuestionImpl(question1, possibleAnswers1, correctAnswer1, pointsAwarded1);
        questionRiver=new QuestionImpl(question2, possibleAnswers2, correctAnswer2, pointsAwarded2);
        questionMap.put(1, questionCapital);
        questionMap.put(2, questionRiver);

        newQuiz=new QuizImpl("Geography", questionMap, 1);

    }
    @After
    public void cleanUp(){
        newQuiz=null;
    }

    @Test
    public void getQuizNameTest(){

        assertEquals("Geography", newQuiz.getQuizName());
    }

    @Test
    public void setQuizNameTest(){
        Map<Integer, Question> questionMap=new HashMap<Integer, Question>();
        questionMap.put(1, questionCapital);
        questionMap.put(2, questionRiver);
        Quiz newQuizDifferentName= new QuizImpl("Geography", questionMap, 2);
        newQuizDifferentName.setQuizName("Rivers and Capitals");

        assertEquals("Rivers and Capitals", newQuizDifferentName.getQuizName());

    }

    @Test
    public void getQuizIdTest(){
        Map<Integer, Question> questionMap=new HashMap<Integer, Question>();
        Quiz quiz1=new QuizImpl("Capitals", questionMap, 3);
        Quiz quiz2=new QuizImpl("Capitals", questionMap, 4);

        assertEquals(3, quiz1.getQuizId());

        assertEquals(4, quiz2.getQuizId());
    }

    @Test
    public void getQuizQuestionsTest(){

         //more extensive testing required to ensure correct entries
        assertTrue(newQuiz.getQuizQuestions().size()==2);
    }


}
