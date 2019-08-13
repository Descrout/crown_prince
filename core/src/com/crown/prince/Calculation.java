package com.crown.prince;

public class Calculation {
    public static boolean isTouching(int touching, int side){
        return (touching & side) == side;
    }
}
