package com.example.dpchat_app;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.util.Date;

public class ProfileActivity extends AppCompatActivity {

    private Button buttonSendRequest , buttonDecline;
    private TextView tvUserName , tvStatus, tvTotalfriend;
    private ImageView imageViewUser;
    private DatabaseReference databaseReference;
    private ProgressDialog progressDialog;

    private String curent_state;

    private DatabaseReference dataFriendReq;
    private FirebaseUser currentUser;
    private DatabaseReference databaseFriend;
    private DatabaseReference mNotificationDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        final String user_id = getIntent().getStringExtra("user_id");

        databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(user_id);
        dataFriendReq = FirebaseDatabase.getInstance().getReference().child("Friend_req");
        databaseFriend = FirebaseDatabase.getInstance().getReference().child("Friend_data");
        mNotificationDatabase = FirebaseDatabase.getInstance().getReference().child("notifications");
        currentUser = FirebaseAuth.getInstance().getCurrentUser();



        buttonSendRequest = findViewById(R.id.profile_button);
        buttonDecline =findViewById(R.id.profile_button_huy);
        tvStatus = findViewById(R.id.profile_status);
        tvTotalfriend = findViewById(R.id.profile_total_friend);
        tvUserName = findViewById(R.id.profile_user_name);
        imageViewUser = findViewById(R.id.profile_image_user);

        curent_state = "not_friends"; //sua lai bang tieng viet

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String userName = dataSnapshot.child("name").getValue().toString();
                String statusUser = dataSnapshot.child("status").getValue().toString();
                String imageUser = dataSnapshot.child("image").getValue().toString();

                tvUserName.setText(userName);
                tvStatus.setText(statusUser);

                Picasso.get().load(imageUser).placeholder(R.drawable.ic_photo_camera).into(imageViewUser);

                dataFriendReq.child(currentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if (dataSnapshot.hasChild(user_id)){
                            String req_type = dataSnapshot.child(user_id).child("request_type").getValue().toString();

                            if (req_type.equals("received")){
                                curent_state = "req_received";
                                buttonSendRequest.setText("Accept Friend Request");

                                buttonDecline.setVisibility(View.VISIBLE);
                                buttonDecline.setEnabled(true);

                            }else if (req_type.equals("sent")){
                                curent_state = "req_sent";
                                buttonSendRequest.setText("Cancel Friend Request");
                                buttonDecline.setVisibility(View.INVISIBLE);
                                buttonDecline.setEnabled(false);
                            }

                            // progressDialog.dismiss();

                        }else {

                            databaseFriend.child(currentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                                    if (dataSnapshot.hasChild(user_id)){
                                        curent_state = "friends";
                                        buttonSendRequest.setText("Unfriend");

                                        buttonDecline.setVisibility(View.INVISIBLE);
                                        buttonDecline.setEnabled(false);

                                    }
                                    // progressDialog.dismiss();
                                    buttonDecline.setVisibility(View.INVISIBLE);
                                    buttonDecline.setEnabled(false);

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                    // progressDialog.dismiss();

                                }
                            });

                        }

                        progressDialog.dismiss(); // chonj cho de cai nay cho hop li trong bai 20
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        buttonSendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = new ProgressDialog(ProfileActivity.this);
                progressDialog.setMessage("Please wait...");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();

                buttonSendRequest.setEnabled(false);

                if (curent_state.equals("not_friends")){
                    dataFriendReq.child(currentUser.getUid()).child(user_id).child("request_type").setValue("sent").addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()){
                                dataFriendReq.child(user_id).child(currentUser.getUid()).child("request_type").setValue("received").addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {

                                        curent_state = "req_sent";
                                        buttonSendRequest.setText("Cancel Friend Request");
                                        Toast.makeText(ProfileActivity.this,"Request Sent Successfully",Toast.LENGTH_SHORT).show();

                                    }
                                });
                            }else {
                                Toast.makeText(ProfileActivity.this,"Failed Sending Request",Toast.LENGTH_SHORT).show();
                            }

                            buttonSendRequest.setEnabled(true);
                            progressDialog.dismiss();
                        }
                    });
                }
                if (curent_state.equals("req_sent")){
                    dataFriendReq.child(currentUser.getUid()).child(user_id).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            dataFriendReq.child(user_id).child(currentUser.getUid()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                    buttonSendRequest.setEnabled(true);
                                    curent_state = "not_friends";
                                    buttonSendRequest.setText("Send Friend Request");
                                    progressDialog.dismiss();

                                }
                            });
                        }
                    });
                }

                if (curent_state.equals("req_received")){
                    final String curentDate = DateFormat.getDateTimeInstance().format(new Date());
                    databaseFriend.child(currentUser.getUid()).child(user_id).setValue(curentDate).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            databaseFriend.child(user_id).child(currentUser.getUid()).setValue(curentDate).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                    dataFriendReq.child(currentUser.getUid()).child(user_id).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            dataFriendReq.child(user_id).child(currentUser.getUid()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {

                                                    curent_state = "friends";
                                                    buttonSendRequest.setText("Unfriend");

                                                    Toast.makeText(ProfileActivity.this,"Các bạn hiện giờ đã là bạn bè.",Toast.LENGTH_SHORT).show();
                                                    progressDialog.dismiss();

                                                    buttonDecline.setVisibility(View.INVISIBLE);
                                                    buttonDecline.setEnabled(false);

                                                }
                                            });
                                        }
                                    });

                                }
                            });
                        }
                    });
                    buttonSendRequest.setEnabled(true);
                }

                if (curent_state.equals("friends")){

                    databaseFriend.child(currentUser.getUid()).child(user_id).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            databaseFriend.child(user_id).child(currentUser.getUid()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                    buttonSendRequest.setEnabled(true);
                                    curent_state = "not_friends";
                                    buttonSendRequest.setText("Send Friend Request");
                                    progressDialog.dismiss();

                                }
                            });
                        }
                    });

                }

            }
        });


    }
}















