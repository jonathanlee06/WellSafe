package com.example.wellsafe.ui.profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.wellsafe.R;
import com.example.wellsafe.authentication.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditProfile extends AppCompatActivity {

    DatabaseReference reference;
    private TextView fullNameEdit;
    private TextView emailEdit;
    private TextView phoneNumberEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try
        {
            this.getSupportActionBar().hide();
        }
        catch (NullPointerException e){}
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        fullNameEdit = (TextView) findViewById((R.id.fullNameEdit));
        emailEdit = (TextView) findViewById((R.id.emailEdit));
        phoneNumberEdit = (TextView) findViewById((R.id.phoneNumberEdit));

        reference = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("User Information");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    Users userInfo = snapshot.getValue(Users.class);
                    fullNameEdit.setText(userInfo.fullName);
                    emailEdit.setText(userInfo.email);
                    phoneNumberEdit.setText(userInfo.phone);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}