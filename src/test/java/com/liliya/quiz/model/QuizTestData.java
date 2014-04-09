package com.liliya.quiz.model;

import com.liliya.quiz.model.*;
import com.liliya.quiz.model.PlayerQuizInstance;
import com.liliya.quiz.model.Question;

import java.util.HashMap;
import java.util.Map;

public class QuizTestData {

    public Map<Integer, String> possibleAnswers1;
    public Map<Integer, String> possibleAnswers2;
    public Map<Integer, Question> questions;
    public Question question1;
    public Question question2;
    public Player newPlayer;
    public Quiz newQuiz;
    public PlayerQuizInstance newInstance;
    public Map<Question, Integer> playerGuesses;

    public QuizTestData() {

        possibleAnswers1 = new HashMap<Integer, String>();
        possibleAnswers1.put(1, "1945");
        possibleAnswers1.put(2, "1940");
        possibleAnswers1.put(3, "1939");
        possibleAnswers1.put(4, "1962");

        possibleAnswers2=new HashMap<Integer, String>();
        possibleAnswers2.put(1, "Central Processing Unit");
        possibleAnswers2.put(2, "Calculating Power Unit");
        possibleAnswers2.put(3, "Conglomerate Powerful Unity");
        possibleAnswers2.put(4, "Nothing");

        questions = new HashMap<Integer, Question>();
        question1 = new QuestionImpl("When did WWII start?", possibleAnswers1, 3, 6);
        question2 = new QuestionImpl("What does CPU stand for?", possibleAnswers2, 1, 7);
        questions.put(1, question1);
        questions.put(2, question2);

        newPlayer = new PlayerImpl("John");
        newQuiz = new QuizImpl("My Quiz", questions, 3);

        newInstance = new PlayerQuizInstance(newPlayer, newQuiz);

        playerGuesses=new HashMap<Question, Integer>();
        playerGuesses.put(question1, 3);
        playerGuesses.put(question2, 1);
    }
}

