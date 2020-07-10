package com.example.wellsafe.ui.checkin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.example.wellsafe.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CheckInHistory extends AppCompatActivity {

    RecyclerView recyclerView;
    HistoryAdapter adapter;
    RecyclerView.LayoutManager layoutManager;
    String [] location, dateTime;

    ArrayList<CheckInData> historyList  = new ArrayList<CheckInData>();

    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try
        {
            this.getSupportActionBar().hide();
        }
        catch (NullPointerException e){}
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkin_history);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerviewHistory);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new HistoryAdapter(CheckInHistory.this, historyList);
        recyclerView.setAdapter(adapter);

        reference = FirebaseDatabase.getInstance().getReference().child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Check-In History");
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Iterable<DataSnapshot> historyChildren = snapshot.getChildren();
                for(DataSnapshot dataSnapshot: historyChildren){
                    CheckInData h = dataSnapshot.getValue(CheckInData.class);
                    Log.e("History: ", h.location + " " + h.date);
                    historyList.add(h);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Iterable<DataSnapshot> historyChildren = snapshot.getChildren();
                for(DataSnapshot dataSnapshot: historyChildren){
                    CheckInData h = dataSnapshot.getValue(CheckInData.class);
                    Log.e("History: ", h.location + " " + h.date);
                    historyList.add(h);
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        /*reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> historyChildren = snapshot.getChildren();
                for(DataSnapshot dataSnapshot: historyChildren){
                    CheckInData h = dataSnapshot.getValue(CheckInData.class);
                    Log.e("History: ", h.location + " " + h.date);
                    historyList.add(h);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*/

    }
}