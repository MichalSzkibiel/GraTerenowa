package com.graterenowa.graterenowa;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

//Dobra odpowiedź, moja działka
/*
    Pokazuje się gdy gracz wskaże dobrą odpowiedź
 */
public class CorrectAnswer extends AppCompatActivity {
    private TextView mName;
    private TextView mQuest;
    private Button OK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_correct_answer);
        //Pobranie indentyfikatora obiektu z intentu
        Intent intent = getIntent();
        int idx = intent.getIntExtra("index", 0);
        //Pobranie nazwy oniektu i pytania
        String name = GameSetupActivity.current.elements.get(idx).name;
        String quest = GameSetupActivity.current.elements.get(idx).quest;
        //Ustawienie nazwy i pytania w odpowiednich TextView
        mName = (TextView) findViewById(R.id.correct_name_view);
        mName.setText(name);
        mQuest = (TextView) findViewById(R.id.correct_quest_view);
        mQuest.setText(quest);
        //Ustalenie callbacka na przycisk potwierdzający - koniec activity
        OK = (Button) findViewById(R.id.correct_OK);
        OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
