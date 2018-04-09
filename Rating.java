package com.graterenowa.graterenowa;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

//Okno rankingu, twoja działka Karol
public class Rating extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);
        Button ret = (Button) findViewById(R.id.button5);
        ret.setOnClickListener(new View.OnClickListener() {
            //wracamy do glownego menu
            @Override
            public void onClick(View view) {
                finish();//zamyka okno
            }
        });

    }
}
