package com.example.dpchat_app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class InformationActivity extends AppCompatActivity {

    private EditText etStatus;
    private EditText etNoiSinh;
    private EditText etDate;
    private Button buttonSave;
    private DatabaseReference databaseReference;
    private ProgressDialog progressDialog;
    private Toolbar toolbar;
    private CheckBox checkBox_nam;
    private CheckBox checkBox_nu;
    private EditText etUserName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);

        FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = current_user.getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(uid);

        //Anh xa
        etDate = findViewById(R.id.inf_date);
        etNoiSinh = findViewById(R.id.inf_noiSinh);
        etStatus = findViewById(R.id.inf_status);
        buttonSave = findViewById(R.id.inf_save);
        checkBox_nam = findViewById(R.id.inf_nam);
        checkBox_nu = findViewById(R.id.inf_nu);
        etUserName = findViewById(R.id.inf_username);

        //Lay ra noi dung ben kia da gui
       // etStatus.setText(getIntent().getStringExtra("status_key"));
       // etNoiSinh.setText(getIntent().getStringExtra("noisinh").substring(15));
       // etDate.setText(getIntent().getStringExtra("ngaysinh").substring(12));
       // etUserName.setText(getIntent().getStringExtra("name"));


        //setup toolBar
        toolbar = findViewById(R.id.inf_toolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Setting Information");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String nameUser = etUserName.getText().toString();

                if (nameUser.isEmpty()){
                    Toast.makeText(InformationActivity.this,"UserName không được bỏ trống",Toast.LENGTH_LONG).show();
                }else {

                    //Progress
                    progressDialog = new ProgressDialog(InformationActivity.this);
                    progressDialog.setTitle("Please wait...");
                    progressDialog.show();

                    String status = (etStatus.getText().toString().isEmpty()) ? "Xin chao cac ban" : etStatus.getText().toString();
                    String noisinh = etNoiSinh.getText().toString();
                    String data = etDate.getText().toString();
                    String gioitinh;

                    //xet gioi tinh
                    if (checkBox_nam.isChecked()) {
                        gioitinh = "Đực rựa";
                    } else {
                        gioitinh = "Cái";
                    }

                    databaseReference.child("name").setValue(nameUser);
                    databaseReference.child("gioiTinh").setValue(gioitinh);
                    databaseReference.child("status").setValue(status);
                    databaseReference.child("namSinh").setValue(data);
                    databaseReference.child("noiSinh").setValue(noisinh).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                progressDialog.dismiss();
                                Intent intent = new Intent(InformationActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                progressDialog.hide();
                                Toast.makeText(InformationActivity.this, "Khong the load du lieu", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });

        checkBox_nu.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    checkBox_nam.setChecked(false);
                } else {
                    checkBox_nam.setChecked(true);
                }
            }
        });

        checkBox_nam.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    checkBox_nu.setChecked(false);
                } else checkBox_nu.setChecked(true);
            }
        });
    }
}
