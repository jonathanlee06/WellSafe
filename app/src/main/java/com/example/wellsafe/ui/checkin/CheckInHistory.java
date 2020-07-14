package com.example.wellsafe.ui.checkin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.wellsafe.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CheckInHistory extends AppCompatActivity {

    RecyclerView recyclerView;
    HistoryAdapter adapter;
    RecyclerView.LayoutManager layoutManager;
    TextView noHistory;
    Button historyClose;

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
        recyclerView = (RecyclerView) findViewById(R.id.recyclerviewHistory);;
        noHistory = (TextView) findViewById(R.id.noHistory);
        historyClose = (Button) findViewById(R.id.historyClose);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, 0));

        reference = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Check-In History");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                        Log.e("Key Value: ", dataSnapshot.getKey());
                        Log.e("History: ", dataSnapshot.getValue().toString());
                        CheckInData h = dataSnapshot.getValue(CheckInData.class);

                        historyList.add(h);
                    }
                    adapter = new HistoryAdapter(CheckInHistory.this, historyList);
                    recyclerView.setAdapter(adapter);
                } else {
                    noHistory.setVisibility(TextView.VISIBLE);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        historyClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }
}