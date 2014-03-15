package com.liliya.quiz;

/**
 * Created with IntelliJ IDEA.
 * User: liliya
 * Date: 15/03/14
 * Time: 12:51
 * To change this template use File | Settings | File Templates.
 */
public class PlayerImpl implements Player {

    private String name;

    public PlayerImpl(String name){
        this.name=name;
    }

    @Override
    public String getName() {

        return this.name;
    }

}