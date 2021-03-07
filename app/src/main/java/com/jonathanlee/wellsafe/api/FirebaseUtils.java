package com.jonathanlee.wellsafe.api;

import com.jonathanlee.wellsafe.authentication.Users;
import com.jonathanlee.wellsafe.ui.checkin.CheckInSummary;
import com.jonathanlee.wellsafe.ui.profile.EditProfile;
import com.jonathanlee.wellsafe.ui.profile.ProfileFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;

public class FirebaseUtils {

    DatabaseReference reference;
    FirebaseUser user;

    public void getProfileData(){
        reference = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("User Information");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    Users userInfo = snapshot.getValue(Users.class);
                    ProfileFragment profileFragment = new ProfileFragment();

                    ProfileFragment.fullNameData = userInfo.fullName;
                    ProfileFragment.emailData = userInfo.email;
                    ProfileFragment.phoneNumberData = userInfo.phone;
                    EditProfile.fullNameProfile = userInfo.fullName;
                    EditProfile.emailProfile = userInfo.email;
                    EditProfile.phoneNumberProfile = userInfo.phone;
                    CheckInSummary.nameSummary = userInfo.fullName;
                    CheckInSummary.phoneSummary = userInfo.phone;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void getSummaryData(){
        reference = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("User Information");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    Users userInfo = snapshot.getValue(Users.class);

                    CheckInSummary.nameSummary = userInfo.fullName;
                    CheckInSummary.phoneSummary = userInfo.phone;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void deleteProfile(){
        String userID = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).getKey();
        reference = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference.removeValue();
    }


}
