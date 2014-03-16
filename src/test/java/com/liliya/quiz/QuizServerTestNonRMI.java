package com.liliya.quiz;

import org.junit.*;

import java.rmi.Remote;
import java.rmi.RemoteException;

import static org.junit.Assert.*;

/**
 * Testing the methods the remote server implements without remote connection
 */
public class QuizServerTestNonRMI {

    Question questionCapital;
    Question questionRiver;
    QuizService quizServer;
    Question [] questions={questionCapital, questionRiver};

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
        try{
            quizServer=new QuizServer();
        }
        catch(RemoteException ex){
            ex.printStackTrace();
        }
    }

    @After
    public void cleanUp(){
     //   quizServer=null;
    }

    @Test
    public void generateQuizNormalTest(){
        String quizName="General knowledge";
        try{
        //assertNotNull(quizServer.generateQuiz(quizName, questions));
        assertEquals(QuizIDGenerator.checkCurrentId()+1, quizServer.generateQuiz(quizName, questions));
        QuizIDGenerator.checkCurrentId();
        }
        catch(RemoteException ex){
            ex.printStackTrace();
        }

    }

    @Test
    public void getListCurrentQuizzes(){

    }


}
