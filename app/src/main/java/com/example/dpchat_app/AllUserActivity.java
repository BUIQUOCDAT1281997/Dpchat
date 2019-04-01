package com.example.dpchat_app;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class AllUserActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView listUsers;
    private DatabaseReference userDatabaseReference;
    private FirebaseUser currentUser;
    private Query query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_user);

        // get id user


        // anh xa toolbar
        toolbar = findViewById(R.id.all_user_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("All Users");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Anh xa list nguoi dung
        listUsers = findViewById(R.id.all_user_list);
        listUsers.setHasFixedSize(true);
        listUsers.setLayoutManager(new LinearLayoutManager(this));

        //FireDatabase
        userDatabaseReference = FirebaseDatabase.getInstance().getReference().child("users");
        query = FirebaseDatabase.getInstance()
                .getReference()
                .child("users")
                .limitToLast(50);
    }

    @Override
    protected void onStart() {
        super.onStart();

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        final String current_userId = currentUser.getUid();

        FirebaseRecyclerOptions<Users> options = new FirebaseRecyclerOptions.Builder<Users>()
                .setQuery(query, Users.class)
                .build();

        FirebaseRecyclerAdapter adapter = new FirebaseRecyclerAdapter<Users, UsersViewHolder>(options) {

            @Override
            public UsersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.layout_item_user, parent, false);

                return new UsersViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull UsersViewHolder usersViewHolder, int i, @NonNull Users users) {
                usersViewHolder.setName(users.getName());
                usersViewHolder.setStatus(users.getStatus());
                usersViewHolder.setThumbImage(users.getImage());


                final String user_id = getRef(i).getKey();

                usersViewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (user_id.equals(current_userId)){
                            Intent intent = new Intent(AllUserActivity.this,SettingActivity.class);
                            startActivity(intent);

                        }else {
                            Intent intent = new Intent(AllUserActivity.this, ProfileActivity.class);
                            intent.putExtra("user_id",user_id);
                            startActivity(intent);
                        }

                    }
                });
            }
        };
        listUsers.setAdapter(adapter);
        adapter.startListening();
    }

    public class UsersViewHolder extends RecyclerView.ViewHolder {

        View mView;

        public UsersViewHolder(@NonNull View itemView) {
            super(itemView);

            mView = itemView;
        }

        public void setName(String name) {
            TextView tvUserName = mView.findViewById(R.id.item_user_name);
            tvUserName.setText(name);
        }

        public void setStatus(String status) {
            TextView tvStatus = mView.findViewById(R.id.item_user_status);
            tvStatus.setText(status);
        }

        public void setThumbImage(String linkThumbImage) {

            CircleImageView circleImageView = mView.findViewById(R.id.item_user_image);

            if (!linkThumbImage.equals("default")) {
                Picasso.get().load(linkThumbImage).placeholder(R.drawable.user).into(circleImageView);
            }
        }
    }
}
