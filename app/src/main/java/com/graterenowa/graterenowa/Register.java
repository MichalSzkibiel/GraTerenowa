package com.graterenowa.graterenowa;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


//okno do rejestracji przydatne przy 1 logowaniu, nie chce sie uruchomic, ale nie wiem czemu?
public class Register extends AppCompatActivity implements View.OnClickListener {

    private EditText imie;
    private EditText nazw;
    private EditText mEmail;
    private EditText Nickname;
    private EditText mPassword;
    private EditText mPasswordRet;
    private Button zarej;
    private ProgressDialog PB;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mDatabase = FirebaseDatabase.getInstance().getReference("odwiedzone");
        imie = findViewById(R.id.name);
        nazw = findViewById(R.id.surname);
        mEmail =findViewById(R.id.email);
        Nickname= findViewById(R.id.nickname);//uzytkownik dodaje nickname'a uzywanego w grze
        mPassword =findViewById(R.id.password);
        mPasswordRet =findViewById(R.id.password_return);
        zarej=  findViewById(R.id.register);
        mAuth=FirebaseAuth.getInstance();
        zarej.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent zag = new Intent(getApplicationContext(), GameSetupActivity.class);
                startActivity(zag);
            }
        });


    }
    public void onClick(View view) {
        String Imie = imie.getText().toString().trim();
        String Nazw = nazw.getText().toString().trim();
        String Emial = mEmail.getText().toString().trim();
        String Pseudo = Nickname.getText().toString().trim();
        String Pass = mPassword.getText().toString().trim();
        String PassRet = mPassword.getText().toString().trim();
        if (TextUtils.isEmpty(Imie)) {
            Toast.makeText(Register.this, "Podaj swoje imie!", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(Nazw)) {
            Toast.makeText(Register.this, "Podaj swoje nazwisko!", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(Emial)) {
            Toast.makeText(Register.this, "Podaj swoj email!", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(Pseudo)) {
            Toast.makeText(Register.this, "Podaj swoj pseudonim, jaki bedziesz uzywac w grze!", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(Pass)) {
            Toast.makeText(Register.this, "Podaj haslo!", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(PassRet)) {
            Toast.makeText(Register.this, "powtorz haslo!", Toast.LENGTH_LONG).show();
            return;
        }
        PB.setMessage("Trwa rejestracja");
        PB.show();
        mAuth.createUserWithEmailAndPassword(Emial,Pass)//wiecej atrybutow nie przyjmuje. Nie wiem co z nimi?
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(Register.this, "Rejestracja zakonczona sucesem. Witamy w naszej grze!", Toast.LENGTH_SHORT).show();
                    // sprawdzenie poprawnosci hasla i loginu
                }
                else
                {
                    Toast.makeText(Register.this, "Bledna rejestracja. Sprobuj ponownie!", Toast.LENGTH_SHORT).show();
                    //w przypadku bledu uzytkownik musi podac dane jeszcze raz!
                }
            }
        });
    }
}
