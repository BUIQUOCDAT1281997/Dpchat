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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SignUpActivity extends AppCompatActivity {

    private EditText etEmail;
    private EditText etPassword;
    private Button buttonCreate;
    private FirebaseAuth mAuth;
    private ProgressDialog signUpProgress;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sigup);

        etEmail = findViewById(R.id.signUp_email);
        etPassword = findViewById(R.id.signUp_password);
        buttonCreate = findViewById(R.id.signUp_buttonCreate);
        mAuth = FirebaseAuth.getInstance();
        signUpProgress = new ProgressDialog(this);

        buttonCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //String userName = etUserName.getText().toString();
                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();

                signUpProgress.setMessage("Please wait while we create your account");
                signUpProgress.setCanceledOnTouchOutside(false);
                signUpProgress.show();

                registerFireBase(email, password);
            }
        });

    }

    private void registerFireBase(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
                            String uid = current_user.getUid();

                            databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(uid);

                            HashMap<String,String> userMap = new HashMap<>();
                            userMap.put("name","default");
                            userMap.put("status","Xin chào! Rất vui khi được làm quen với các bạn :)");
                            userMap.put("namSinh","default");
                            userMap.put("image","default");
                            userMap.put("noiSinh","default");
                            userMap.put("gioiTinh","default");
                            userMap.put("thumb_image","default");

                            databaseReference.setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        signUpProgress.dismiss();
                                        Toast.makeText(SignUpActivity.this, "Authentication success.",
                                                Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(SignUpActivity.this, InformationActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            });
                        } else {
                            // If sign in fails, display a message to the user.
                            signUpProgress.hide();
                            Toast.makeText(SignUpActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }
}
