package com.example.wellsafe.api;

import com.example.wellsafe.authentication.Users;
import com.example.wellsafe.ui.profile.EditProfile;
import com.example.wellsafe.ui.profile.ProfileFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;

public class FirebaseUtils {

    DatabaseReference reference;

    public void getProfileData(){
        reference = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("User Information");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    Users userInfo = snapshot.getValue(Users.class);

                    ProfileFragment.fullNameData = userInfo.fullName;
                    ProfileFragment.emailData = userInfo.email;
                    ProfileFragment.phoneNumberData = userInfo.phone;
                    EditProfile.fullNameProfile = userInfo.fullName;
                    EditProfile.emailProfile = userInfo.email;
                    EditProfile.phoneNumberProfile = userInfo.phone;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void getHistoryData(){

    }
}
