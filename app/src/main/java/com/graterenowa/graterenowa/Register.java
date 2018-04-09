package com.graterenowa.graterenowa;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

//okno do rejestracji przydatne przy 1 logowaniu
public class Register extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        /*potrzebne bedzie okno z informacja do pobierania danych oraz sprawdzajece czy dany uzytkownik:
        a. uzywa wczesniej dodanego nickname'a
        b. pobrawnie wpisal poprawianie hasla
        c. w przypadku bledu uzytkownik bedzie musial wpisac ponownie- ponowne otwarcie tego samego okna
        d. nie wykluczone sa zmiany w ponizszym kodzie ze wzgledu na powyzsze rzeczy
        */
        Button rEM = (Button) findViewById(R.id.register);
        rEM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent em = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(em);
            }
        });

    }
}
