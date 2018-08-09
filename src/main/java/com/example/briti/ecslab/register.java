package com.example.briti.ecslab;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by Briti on 03-Feb-18.
 */

public class register extends Activity {
    String gender;
    String name;
    String age;
    String password;
    String email;
    String weight;
    String height;
    private static final String TAG = "MainActivity";
    private FirebaseUser user;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set View to register.xml
        setContentView(R.layout.registration);
        TextView loginScreen = findViewById(R.id.link_to_login);
        mAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressBar);
        // Listening to Login Screen link
        loginScreen.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                // Closing registration screen
                // Switching to Login Screen/closing register screen
                finish();
            }
        });
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.female:
                if (checked)
                    gender = "Female";
                    break;
            case R.id.male:
                if (checked)
                    gender = "Male";
                    break;
        }
    }

    public void onRegisterButtonClicked(View view){
        EditText reg_fullname = (EditText)findViewById(R.id.reg_fullname);
        name = reg_fullname.getText().toString();
        EditText reg_age = (EditText)findViewById(R.id.age);
        age = reg_age.getText().toString();
        EditText reg_password = (EditText)findViewById(R.id.reg_password);
        password = reg_password.getText().toString();
        EditText reg_email = (EditText)findViewById(R.id.reg_email);
        email = reg_email.getText().toString();
        EditText reg_weight = (EditText)findViewById(R.id.reg_weight);
        weight = reg_weight.getText().toString();
        EditText reg_height = (EditText)findViewById(R.id.reg_height);
        height=reg_height.getText().toString();
        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            user = mAuth.getCurrentUser();
                            DatabaseOperation db = new DatabaseOperation();
                            db.initializeUser(name,age,gender,weight,height);
                            // Switching to LocationLogger screen
                            Intent i = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(i);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(register.this, "Authentication failed. "+task.getException(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
