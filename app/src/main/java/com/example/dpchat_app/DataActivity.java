package com.example.dpchat_app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DataActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private EditText editText;
    private Button button;
    private DatabaseReference databaseReference;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);

        FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = current_user.getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(uid);

        // anh xa
        button = findViewById(R.id.data_button_save);
        editText = findViewById(R.id.data_edittext);
        toolbar = findViewById(R.id.data_toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Sinh nhật");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = new ProgressDialog(DataActivity.this);
                progressDialog.setMessage("Please wait...");
                progressDialog.show();

                // viet phan co ban
                databaseReference.child("namSinh").setValue(editText.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            progressDialog.dismiss();
                            Toast.makeText(DataActivity.this,"Thông tin của bạn đã được tải lên server",Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(DataActivity.this, SettingActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
            }
        });
    }
}
