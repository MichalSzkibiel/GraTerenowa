package com.graterenowa.graterenowa;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

//Okno z zasadami, twoja działka Karol
public class RegulationsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regulations);
        setRequestedOrientation(
                ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Button reg = (Button) findViewById(R.id.button1);
        reg.setOnClickListener(new View.OnClickListener() {
            //wracamy do glownego menu
            @Override
            public void onClick(View view) {
               finish();//zamyka to okno
            }
        });

    }
}
