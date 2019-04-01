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

public class AccommodationActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private EditText editText;
    private int valueLength;
    private TextView tvChange;
    private Button buttonSave;
    private DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accommodation);

        FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = current_user.getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(uid);

        valueLength = 0;

        buttonSave = findViewById(R.id.noilamviec_button_save);

        toolbar = findViewById(R.id.set_noilamviec_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Setting Information");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tvChange = findViewById(R.id.noilamviec_demtu);

        editText = findViewById(R.id.noilamviec_editText);
        editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(60)});
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                valueLength = editText.getText().length();
                tvChange.setText(valueLength + "/60");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editText.getText().length()==0){
                    Toast.makeText(AccommodationActivity.this,"UserName trong",Toast.LENGTH_LONG).show();
                }else {
                    databaseReference.child("noiSinh").setValue(editText.getText().toString());
                    Intent intent = new Intent(AccommodationActivity.this,SettingActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

    }
}
