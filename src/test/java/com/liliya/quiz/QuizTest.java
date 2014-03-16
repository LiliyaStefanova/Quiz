package com.liliya.quiz;

import org.junit.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;


public class QuizTest {

    Question questionCapital;
    Question questionRiver;
    Quiz newQuiz;

    @Before
    public void buildUp(){

        String question1="What is the capital of Australia?";
        String question2="What is the longest rive in the world?";
        String [] possibleAnswers1={"Canberra", "Melbourne", "Darwin", "Singapore" };
        String [] possibleAnswers2={"The Nile", "The Amazon", "Orinoko", "Mississippi" };
        String correctAnswer1="Canberra";
        String correctAnswer2="The Nile";
        int pointsAwarded1=5;
        int pointsAwarded2=7;
        questionCapital=new QuestionImpl(question1, possibleAnswers1, correctAnswer1, pointsAwarded1);
        questionRiver=new QuestionImpl(question2, possibleAnswers2, correctAnswer2, pointsAwarded2);
        Question [] questions={questionCapital, questionRiver};

        newQuiz=new QuizImpl("Geography", questions);

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
        Question [] questions={questionCapital, questionRiver};
        Quiz newQuizDifferentName= new QuizImpl("Geography", questions);
        newQuizDifferentName.setQuizName("Rivers and Capitals");

        assertEquals("Rivers and Capitals", newQuizDifferentName.getQuizName());

    }

    @Test
    public void getQuizIdTest(){
        Question [] questions={questionCapital, questionRiver};
        Quiz quiz1=new QuizImpl("Capitals", questions);
        Quiz quiz2=new QuizImpl("Capitals", questions);

        assertEquals(2, quiz1.getQuizId());

        assertEquals(3, quiz2.getQuizId());
    }

    @Test
    public void getQuizQuestionsTest(){

         //more extensive testing required to ensure correct entries
        assertTrue(newQuiz.getQuizQuestions().size()==2);
    }

    private void whatIsIDGeneratorNow(){
        System.out.print("ID generator is at:"+QuizIDGenerator.checkCurrentId());
    }

}
