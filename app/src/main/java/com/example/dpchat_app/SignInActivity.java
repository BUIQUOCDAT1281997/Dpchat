package com.example.dpchat_app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignInActivity extends AppCompatActivity {

    private EditText etEmail;
    private EditText etPassword;
    private Button buttonLogin;
    private FirebaseAuth mAuth;
    private ProgressDialog signInProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        etEmail = findViewById(R.id.singin_email);
        etPassword = findViewById(R.id.signin_password);
        buttonLogin = findViewById(R.id.signin_buttom_login);
        mAuth = FirebaseAuth.getInstance();
        signInProgress = new ProgressDialog(this);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();

                signInProgress.setMessage("Please wait...");
                signInProgress.setCanceledOnTouchOutside(false);
                signInProgress.show();
                loginUser(email,password);
            }
        });

    }

    private void loginUser(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            signInProgress.dismiss();
                            sendToMainLayout();
                        } else {
                            // If sign in fails, display a message to the user.
                            signInProgress.hide();
                            Toast.makeText(SignInActivity.this, "Authentication failed.",
                                    Toast.LENGTH_LONG).show();
                        }

                        // ...
                    }
                });
    }

    private void sendToMainLayout() {
        Intent startIntent = new Intent(SignInActivity.this,MainActivity.class);
        startActivity(startIntent);
        finish();
    }
}
