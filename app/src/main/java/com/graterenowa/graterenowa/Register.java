package com.graterenowa.graterenowa;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


//okno do rejestracji przydatne przy 1 logowaniu
public class Register extends AppCompatActivity {

    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        /*potrzebne bedzie okno z informacja do pobierania danych oraz sprawdzajece czy dany uzytkownik:
        a. uzywa wczesniej dodanego nickname'a
        b. pobrawnie wpisal poprawianie hasla
        c. w przypadku bledu uzytkownik bedzie musial wpisac ponownie- ponowne otwarcie tego samego okna
        d. nie wykluczone sa zmiany w ponizszym kodzie ze wzgledu na powyzsze rzeczy
        */
        mDatabase = FirebaseDatabase.getInstance().getReference("odwiedzone");
        final EditText name =(EditText) findViewById(R.id.name);
        final EditText sur =(EditText) findViewById(R.id.surname);
        final EditText em =(EditText) findViewById(R.id.email);
        final EditText nick =(EditText) findViewById(R.id.nickname);
        final EditText pass =(EditText) findViewById(R.id.password);
        final EditText retpass =(EditText) findViewById(R.id.password_return);
        final Button rEM = (Button) findViewById(R.id.register);
        rEM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //name.getText().toString();
                finish();
            }
        });

    }
}
