package com.graterenowa.graterenowa;

/**
 * Created by Karol Jakub on 14.04.2018.
 */

public class Score {

    String psudonim;
    String score;

    public Score(){
    }
    public Score(String psudonim, String score){
        this.psudonim=psudonim;
        this.score=score;
    }
    public void setPseudonim(String pseudonim){
        this.psudonim=pseudonim;
    }
    public void setScore(String score){
        this.score=score;
    }
    public String getPseudonim(){
        return psudonim;
    }
    public String getScore(){
        return score;
    }
}

