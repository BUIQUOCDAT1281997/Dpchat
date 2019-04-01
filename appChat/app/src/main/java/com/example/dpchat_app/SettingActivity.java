package com.example.dpchat_app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;

public class SettingActivity extends AppCompatActivity {

    private DatabaseReference userDatabase;
    private FirebaseUser currentUser;

    private ListView listViewData;
    ArrayList<DataUsers> listDataUsers;
    DataUsersAdapter adapter;

    private CircleImageView circleImageView;
    private TextView tvUserName;
    private TextView tvStatus;

    private Toolbar toolbar;
    private ProgressDialog progressDialog;
    private ImageView changeImage;
    private static final int GALLERY_PICK = 1;

    //thu vien anh tren Firebase
    private StorageReference mStorageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        // Ánh xạ cho phần list view
        listViewData = findViewById(R.id.setting_listDataUser);

        //progress-load du lieu
        progressDialog = new ProgressDialog(SettingActivity.this);
        progressDialog.setMessage("Please wait...");
        //progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        circleImageView = findViewById(R.id.setting_image);
        tvUserName = findViewById(R.id.Setting_user_name);
        tvStatus = findViewById(R.id.setting_status);

        changeImage = findViewById(R.id.setting_change_image);


        //storageFirebase
        mStorageRef = FirebaseStorage.getInstance().getReference();

        //setup toolBar
        toolbar = findViewById(R.id.setting_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Setting Account");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String currentUid = currentUser.getUid();
        userDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(currentUid);
        userDatabase.keepSynced(true);

        userDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String name = Objects.requireNonNull(dataSnapshot.child("name").getValue()).toString();
                final String image = dataSnapshot.child("image").getValue().toString();
                String namSinh = dataSnapshot.child("namSinh").getValue().toString();
                String noiSinh = dataSnapshot.child("noiSinh").getValue().toString();
                String status = dataSnapshot.child("status").getValue().toString();
                String gioiTinh = dataSnapshot.child("gioiTinh").getValue().toString();

                // set dữ liệu người dùng cho listView
                listDataUsers = new ArrayList<>();
                listDataUsers.add(new DataUsers("Giới tính", gioiTinh));
                listDataUsers.add(new DataUsers("Nơi làm việc", noiSinh));
                listDataUsers.add(new DataUsers("Sinh nhật", namSinh));
                listDataUsers.add(new DataUsers("Tình trạng hôn nhân", "Đéo nói"));
                listDataUsers.add(new DataUsers("Khác", "Click here"));

                adapter = new DataUsersAdapter(SettingActivity.this, R.layout.list_item_layout_setting, listDataUsers);
                listViewData.setAdapter(adapter);



                tvStatus.setText(status);
                tvUserName.setText(name);

                if (!image.equals("default")) {

                    // them phan nextWorkPolicy de ve phan offline

                    Picasso.get().load(image).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.user).into(circleImageView, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError(Exception e) {

                            Picasso.get().load(image).placeholder(R.drawable.user).into(circleImageView);

                        }
                    });


                }

                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        circleImageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent galleryIntent = new Intent();
                galleryIntent.setType("image/*");
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(galleryIntent, "SELECT IMAGE"), GALLERY_PICK);
                return true;
            }
        });

       /*
        buttonIdea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String status_send = tvStatus.getText().toString();
                Intent intent = new Intent(SettingActivity.this, InformationActivity.class);
                //nen noi dung vao intent
                intent.putExtra("status_key", status_send);
                intent.putExtra("noisinh", tvNoiSinh.getText().toString());
                intent.putExtra("ngaysinh", tvNgaySinh.getText().toString());
                intent.putExtra("gioitinh", tvInf.getText().toString());
                intent.putExtra("name", tvUserName.getText());
                startActivity(intent);
            }
        });
        */

        changeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent();
                galleryIntent.setType("image/*");
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(galleryIntent, "SELECT IMAGE"), GALLERY_PICK);
            }
        });

        listViewData.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:{
                        Intent gioiTinhIntent = new Intent(SettingActivity.this, GioiTinhActivity.class);
                        startActivity(gioiTinhIntent);
                        break;
                    }
                    case 1: {
                        Intent noiLamViecIntent = new Intent(SettingActivity.this, AccommodationActivity.class);
                        startActivity(noiLamViecIntent);
                        break;
                    }
                    case 2: {
                        Intent dataIntent = new Intent(SettingActivity.this, DataActivity.class);
                        startActivity(dataIntent);
                        break;
                    }
                    case 3 :{
                        break;
                    }
                    default: break;

                }
            }
        });

        tvStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this,StatusActivity.class);
                startActivity(intent);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {


            if (requestCode == GALLERY_PICK && resultCode == RESULT_OK) {
                Uri imageUri = data.getData();

                CropImage.activity(imageUri)
                        .setAspectRatio(1, 1)  // có thể dùng thêm phương thức .setMinCropWindowSize để cố định khíc thước khi crop
                        .start(this);
            }

            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (resultCode == RESULT_OK) {

                    progressDialog = new ProgressDialog(this);
                    progressDialog.setTitle("Uploading Image...");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();

                    Uri resultUri = result.getUri(); //kết quả sau khi crop hình

                    final File thumb_filePath = new File(resultUri.getPath());

                    String currentUser_id = currentUser.getUid();


                    Bitmap thumb_bitmap = new Compressor(this)
                            .setMaxHeight(200)
                            .setMaxWidth(200)
                            .setQuality(75)
                            .compressToBitmap(thumb_filePath);

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    thumb_bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    final byte[] thumb_byte = baos.toByteArray();


                    final StorageReference filePath = mStorageRef.child("profile_images").child(currentUser_id + ".jpg");
                    final StorageReference thumb_filePathInStorage = mStorageRef.child("profile_images").child("thumb_image").child(currentUser_id + ".jpg");
                    //String a =filePath.getDownloadUrl().toString();

                    filePath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if (task.isSuccessful()) {
                                filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        final String downloadUrl = uri.toString();



                                        UploadTask uploadTask = thumb_filePathInStorage.putBytes(thumb_byte);
                                        uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                                               if (task.isSuccessful()){
                                                   thumb_filePathInStorage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                       @Override
                                                       public void onSuccess(Uri thumb_uri) {
                                                           String thumb_download = thumb_uri.toString();
                                                           userDatabase.child("thumb_image").setValue(thumb_download).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                               @Override
                                                               public void onComplete(@NonNull Task<Void> task) {
                                                                   if (task.isSuccessful()) {
                                                                       userDatabase.child("image").setValue(downloadUrl).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                           @Override
                                                                           public void onComplete(@NonNull Task<Void> task) {
                                                                               progressDialog.dismiss();
                                                                           }
                                                                       });
                                                                   } else {
                                                                       Toast.makeText(SettingActivity.this, "khong the load anh len", Toast.LENGTH_LONG).show();
                                                                       progressDialog.dismiss();
                                                                   }
                                                               }
                                                           });
                                                       }
                                                   });
                                               }else {
                                                   Toast.makeText(SettingActivity.this, "khong the load anh len", Toast.LENGTH_LONG).show();
                                                   progressDialog.dismiss();
                                               }


                                            }
                                        });

                                    }
                                });
                            } else {
                                Toast.makeText(SettingActivity.this, "khong the load anh len", Toast.LENGTH_LONG).show();
                            }
                        }
                    });

                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    Exception error = result.getError();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
