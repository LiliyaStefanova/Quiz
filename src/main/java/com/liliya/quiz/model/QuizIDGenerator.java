package com.liliya.quiz.model;

/**
 * Helper class with a static variable and methods to generate sequential IDs for quizzes
 * methods synchronised to avoid two set up clients trying to get the same resource at the same time
 */
@Deprecated
public class QuizIDGenerator {

    private static int IDCounter;

    public static synchronized int getNewID(){
        IDCounter++;
        return IDCounter;
    }

    public static synchronized int checkCurrentId(){
        return IDCounter;
    }
}
