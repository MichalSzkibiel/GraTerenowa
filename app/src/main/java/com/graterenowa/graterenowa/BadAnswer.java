package com.graterenowa.graterenowa;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

//Zła odpowiedź, moja działka
/*Wyświetla się, gdy gracz wskaże złą odpowiedź
 */
public class BadAnswer extends AppCompatActivity {
    private TextView mExplain;
    private TextView mQuest;
    private Button OK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bad_answer);
        //Pobranie identyfikatora zgłoszonego obiektu z intentu
        Intent intent = getIntent();
        int idx = intent.getIntExtra("index", 0);
        //Pobranie informacji o pytaniu i ilości zgłoszeń
        String quest = GameSetupActivity.current.elements.get(idx).quest;
        int comm = GameSetupActivity.current.elements.get(idx).commissions;
        //Jeżeli to pierwsze zgłoszenie, to gracz nie traci punktów. Ustawienie właściwego tekstu
        if (comm == 1) {
            mExplain = (TextView) findViewById(R.id.bad_explain1);
            mExplain.setText(R.string.bad_explain2);
        }
        //Ustawienie pytania we właściwy TextView
        mQuest = (TextView) findViewById(R.id.bad_quest_view);
        mQuest.setText(quest);
        //Ustalenie Callbacka na przycisk potwierdzający - koniec działania
        OK = (Button) findViewById(R.id.bad_OK);
        OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
