package com.example.dpchat_app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class GioiTinhActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RadioButton rbNam;
    private RadioButton rbNu;
    private Button button_save;
    private DatabaseReference databaseReference;
    private String gioitinh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gioi_tinh);

        // anh xa
        toolbar = findViewById(R.id.gioitinh_layout_toolbar);
        rbNam = findViewById(R.id.gioitinh_nam);
        rbNu = findViewById(R.id.gioitinh_nu);
        button_save = findViewById(R.id.gioitinh_button_save);
        FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = current_user.getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(uid);

        // set toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Giới tính");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        button_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (rbNam.isChecked()) {
                    gioitinh = "Đực rựa";
                } else {
                    gioitinh = "Cái";
                }

                databaseReference.child("gioiTinh").setValue(gioitinh);

                Intent intent = new Intent(GioiTinhActivity.this,SettingActivity.class);
                startActivity(intent);
                finish();
            }
        });

        rbNu.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    rbNam.setChecked(false);
                } else {
                    rbNam.setChecked(true);
                }
            }
        });

        rbNam.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    rbNu.setChecked(false);
                } else rbNu.setChecked(true);
            }
        });



    }

}
