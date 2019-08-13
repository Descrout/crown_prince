package com.crown.prince;


public class FSM {
    public Runnable currentState;

    public FSM(Runnable start){
        currentState = start;
    }

    public void update(){
        currentState.run();
    }
}