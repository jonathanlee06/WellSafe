package com.jonathanlee.wellsafe.ui.profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jonathanlee.wellsafe.R;
import com.jonathanlee.wellsafe.SignUpActivity;
import com.jonathanlee.wellsafe.authentication.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditProfile extends AppCompatActivity {

    DatabaseReference reference;
    FirebaseAuth auth;
    FirebaseUser user;
    public static String fullNameProfile;
    public static String emailProfile;
    public static String phoneNumberProfile;
    private EditText fullNameEdit;
    private EditText emailEdit;
    private EditText phoneNumberEdit;

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

        fullNameEdit = (EditText) findViewById((R.id.fullNameEdit));
        emailEdit = (EditText) findViewById((R.id.emailEdit));
        phoneNumberEdit = (EditText) findViewById((R.id.phoneNumberEdit));
        Button save = (Button) findViewById(R.id.saveEditButton);
        Button editClose = (Button) findViewById(R.id.editClose);
        TextView deleteAccount = (TextView) findViewById(R.id.deleteAccount);

        fullNameEdit.setText(fullNameProfile, TextView.BufferType.EDITABLE);
        emailEdit.setText(emailProfile, TextView.BufferType.EDITABLE);
        phoneNumberEdit.setText(phoneNumberProfile, TextView.BufferType.EDITABLE);

        deleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(EditProfile.this);
                builder.setCancelable(false);
                builder.setTitle("Warning!");
                builder.setMessage("You are about to delete this account. Please be noted that this action is irreversible and all data associated with this account will be deleted. Are you sure you want to continue?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //if user pressed "yes", then he is allowed to exit from application
                        String userID = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).getKey();
                        reference = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                        user = FirebaseAuth.getInstance().getCurrentUser();
                        reference.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(EditProfile.this, "Account Deleted", Toast.LENGTH_SHORT).show();
                                            loadSignUpView();
                                        }
                                    }
                                });
                            }
                        });
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //if user select "No", just cancel this dialog and continue with app
                        dialog.cancel();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String fullName = fullNameEdit.getText().toString().trim();
                final String email = emailEdit.getText().toString().trim();
                final String phone = phoneNumberEdit.getText().toString().trim();

                if(fullNameEdit.getText().toString().isEmpty() || phoneNumberEdit.getText().toString().isEmpty()){
                    AlertDialog.Builder builder = new AlertDialog.Builder(EditProfile.this);
                    builder.setMessage(R.string.signup_error_message)
                            .setTitle(R.string.signup_error_title)
                            .setPositiveButton(android.R.string.ok, null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                } else {
                    Users userData = new Users(fullName, email, phone);
                    Log.e("userData", fullName + " " + email + " " + phone);
                    FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("User Information")
                            .setValue(userData).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(EditProfile.this, "Edit Profile Successful", Toast.LENGTH_SHORT).show();
                                    onBackPressed();
                                    Fragment selectedFragment = new ProfileFragment();
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

        editClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void loadSignUpView() {
        Intent intent = new Intent(this, SignUpActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

}