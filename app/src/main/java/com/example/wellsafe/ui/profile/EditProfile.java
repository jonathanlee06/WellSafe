package com.example.wellsafe.ui.profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wellsafe.HomeActivity;
import com.example.wellsafe.R;
import com.example.wellsafe.SignUpActivity;
import com.example.wellsafe.api.FirebaseUtils;
import com.example.wellsafe.authentication.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditProfile extends AppCompatActivity {

    DatabaseReference reference;
    FirebaseAuth auth;
    FirebaseUser user;
    public static String fullName;
    public static String email;
    public static String phoneNumber;
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
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        AuthCredential credential = EmailAuthProvider.getCredential("user@example.com", "password1234");

        fullNameEdit = (TextView) findViewById((R.id.fullNameEdit));
        emailEdit = (TextView) findViewById((R.id.emailEdit));
        phoneNumberEdit = (TextView) findViewById((R.id.phoneNumberEdit));
        Button save = (Button) findViewById(R.id.saveEditButton);

        fullNameEdit.setText(fullName);
        emailEdit.setText(email);
        phoneNumberEdit.setText(phoneNumber);

        final String userFullname = fullNameEdit.getText().toString();
        final String userEmail = emailEdit.getText().toString();
        final String userPhoneNumber = phoneNumberEdit.getText().toString();

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fullNameEdit.getText().toString().isEmpty() || emailEdit.getText().toString().isEmpty() || phoneNumberEdit.getText().toString().isEmpty()){
                    AlertDialog.Builder builder = new AlertDialog.Builder(EditProfile.this);
                    builder.setMessage(R.string.signup_error_message)
                            .setTitle(R.string.signup_error_title)
                            .setPositiveButton(android.R.string.ok, null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                } else {
                    user.updateEmail(userEmail).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Users userData = new Users(userFullname, userEmail, userPhoneNumber);
                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(user.getUid())
                                    .child("User Information")
                                    .setValue(userData).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(EditProfile.this, "Edit Profile Successful", Toast.LENGTH_SHORT).show();
                                        FirebaseUtils fb = new FirebaseUtils();
                                        //fb.getProfileData();
                                        onBackPressed();
                                    }
                                });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(EditProfile.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });


    }
}