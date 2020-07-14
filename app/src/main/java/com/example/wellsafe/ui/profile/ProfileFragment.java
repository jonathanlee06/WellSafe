package com.example.wellsafe.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.wellsafe.LogInActivity;
import com.example.wellsafe.R;
import com.example.wellsafe.api.FirebaseUtils;
import com.example.wellsafe.authentication.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class ProfileFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    View view;
    DatabaseReference reference;
    public static String fullNameData;
    public static String emailData;
    public static String phoneNumberData;
    private TextView fullName;
    private TextView email;
    private TextView phoneNumber;
    SwipeRefreshLayout swipeLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FirebaseUtils fb = new FirebaseUtils();
        fb.getProfileData();

        view = inflater.inflate(R.layout.fragment_profile, container, false);
        Button logout = (Button) view.findViewById(R.id.logoutButton);
        Button editProfile = (Button) view.findViewById(R.id.editProfileButton);
        fullName = (TextView) view.findViewById((R.id.fullName));
        email = (TextView) view.findViewById((R.id.email));
        phoneNumber = (TextView) view.findViewById((R.id.phoneNumber));
        fullName.setText(fullNameData);
        email.setText(emailData);
        phoneNumber.setText(phoneNumberData);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                loadLogInView();
            }
        });
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EditProfile.class);
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.profileRefresh);
        swipeLayout.setOnRefreshListener((SwipeRefreshLayout.OnRefreshListener) this);
    }

    @Override
    public void onRefresh() {
        swipeLayout.setRefreshing(false);
        FragmentTransaction ftr = getFragmentManager().beginTransaction();
        ftr.detach(ProfileFragment.this).attach(ProfileFragment.this).commit();
    }


    private void loadLogInView() {
        Intent intent = new Intent(getActivity(), LogInActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

}
