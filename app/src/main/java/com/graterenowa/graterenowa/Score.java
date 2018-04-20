package com.graterenowa.graterenowa;

/**
 * Created by Karol Jakub on 14.04.2018.
 */

public class Score {

    String pseudonim;
    String score;
    String set_name;

    public Score(){
    }
    public Score(String pseudonim, String score, String set_name){
        this.pseudonim=pseudonim;
        this.score=score;
        this.set_name=set_name;
    }
    public void setPseudonim(String pseudonim){
        this.pseudonim=pseudonim;
    }
    public void setScore(String score){
        this.score=score;
    }
    public String getPseudonim(){
        return pseudonim;
    }
    public String getScore(){
        return score;
    }
    public void setName(String zest){
        set_name=zest;
    }
    public String getName(){
        return set_name;
    }
}

