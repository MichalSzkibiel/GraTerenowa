package com.graterenowa.graterenowa;

import org.json.JSONException;
import org.json.JSONObject;

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
        set_name = set_name.replace(" ", "_");
        set_name = set_name.replace(",", "$");
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
    public Score(JSONObject json){
        try {
            pseudonim=json.getString("pseudonim");
            score=json.getString("score");
            set_name=json.getString("name");
            set_name = set_name.replace("_", " ");
            set_name = set_name.replace("$", ",");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

