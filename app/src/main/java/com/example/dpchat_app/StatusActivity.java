package com.example.dpchat_app;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class StatusActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private EditText edDate;
    private TextView tvCount;
    private Button buttonChange;
    private int valueLength;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);

        FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = current_user.getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(uid);

        //anh xa
        buttonChange = findViewById(R.id.status_button_save);
        edDate = findViewById(R.id.status_edit_date);
        tvCount = findViewById(R.id.status_count_tv);

        toolbar = findViewById(R.id.status_layout_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Status");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        edDate.setFilters(new InputFilter[]{new InputFilter.LengthFilter(60)});
        edDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                valueLength = edDate.getText().length();
                tvCount.setText(valueLength + "/60");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        buttonChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edDate.getText().length()==0){
                    Toast.makeText(StatusActivity.this,"Status is empty",Toast.LENGTH_LONG).show();
                }else {
                    databaseReference.child("status").setValue(edDate.getText().toString());
                    Intent intent = new Intent(StatusActivity.this,SettingActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });


    }
}
