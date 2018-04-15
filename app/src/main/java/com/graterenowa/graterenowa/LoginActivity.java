package com.graterenowa.graterenowa;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;
//Okno logowania, twoja działka Karol
//właśnie próbuje to zrozumieć
/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */

    // UI references.
    private EditText mEmailView;//nie chce mi sie wszedzie zmieniac na Nickname'a!
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private Button rejestracja;
    private Button mEmailSignInButton;
    private ProgressDialog PB;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        mEmailView = (EditText) findViewById(R.id.nickname);
        mAuth = FirebaseAuth.getInstance();
        mPasswordView = (EditText) findViewById(R.id.password);
        mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        PB = new ProgressDialog(this);

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        //wymagany przy logowaniu będzie pseudonim, a nie Email, chyba ze bedzie sie po raz pierwszy rejestrowal
        rejestracja = findViewById(R.id.register);
        mAuth=FirebaseAuth.getInstance();
        mEmailSignInButton.setOnClickListener(this);
        rejestracja.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent rej = new Intent(getApplicationContext(), Register.class);
                startActivity(rej);
            }
        });
    }

    private void LogIn(){
            String Nick = mEmailView.getText().toString().trim();
            String pass = mPasswordView.getText().toString().trim();
            if (TextUtils.isEmpty(Nick)) {

                Toast.makeText(LoginActivity.this, "Podaj swojego Nicka!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(pass)) {
                Toast.makeText(LoginActivity.this, "Podaj hasło!", Toast.LENGTH_SHORT).show();
                return;
            } else {
                Toast.makeText(LoginActivity.this,"Błędne dane, sprawdź poprawność jeszcze raz!",Toast.LENGTH_SHORT).show();
            }
            PB.setMessage("Trwa logowanie");
            PB.show();
            mAuth.signInWithEmailAndPassword(Nick,pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>(){
                @Override
                public void onComplete(@NonNull Task<AuthResult> task){
                    if (task.isSuccessful()) {
                        Toast.makeText(LoginActivity.this, "Zalogowano, Witamy w naszej grze!", Toast.LENGTH_SHORT).show();

                    }
                    else
                    {
                        Toast.makeText(LoginActivity.this, "Bledne dane logowania. Sprawdz, czy wpisales je poprawnie!", Toast.LENGTH_SHORT).show();
                    }
                }

            });
        }
            @Override
            public void onClick(View view) {

                if(view == mEmailSignInButton){
                    LogIn();
                    }
                if(view ==rejestracja){

                    }
                }
    }




