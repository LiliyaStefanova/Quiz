package com.liliya.quiz;


import org.junit.Test;
import static org.junit.Assert.*;
import  org.junit.*;

public class PlayerTest {

    @Test
    public void getPlayerNameTest(){

        Player newPlayer=new PlayerImpl("Jim");

        assertEquals("Jim", newPlayer.getName());
    }
}
