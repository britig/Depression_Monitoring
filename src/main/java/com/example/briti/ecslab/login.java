package com.example.briti.ecslab;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by Briti on 03-Feb-18.
 */

public class login extends Activity {
    private FirebaseAuth mAuth;
    String password;
    String email;
    private ProgressBar progressBar;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setting default screen to login.xml
        setContentView(R.layout.login);

        TextView registerScreen = (TextView) findViewById(R.id.link_to_register);
        mAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressBar);
        // Listening to register new account link
        registerScreen.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // Switching to Register screen
                Intent i = new Intent(getApplicationContext(), register.class);
                startActivity(i);
            }
        });
    }
    public void onLoginButtonClick(View view){
        EditText reg_password = (EditText)findViewById(R.id.reg_password);
        password = reg_password.getText().toString();
        EditText reg_email = (EditText)findViewById(R.id.reg_email);
        email = reg_email.getText().toString();
        //authenticate user
        progressBar.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(login.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        progressBar.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            // there was an error
                            Toast.makeText(login.this, "Login successful", Toast.LENGTH_LONG).show();
                            // Switching to LocationLogger screen
                            Intent i = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(i);
                        } else {
                            Toast.makeText(login.this, "Login unsuccessful", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}
