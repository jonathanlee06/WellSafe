package com.example.wellsafe.ui.checkin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.wellsafe.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {

    private String[] checkinHistory;
    ArrayList<CheckInData> historyList = new ArrayList<>();
    Context c;

    HistoryAdapter(Context c, ArrayList<CheckInData> historyList){
        this.c = c;
        this.historyList = historyList;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public HistoryAdapter.HistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_card, null);
        HistoryViewHolder historyViewHolder = new HistoryViewHolder(view);


        return historyViewHolder;
    }

    public static class HistoryViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        TextView location;
        TextView dateTime;
        TextView temperature;
        public HistoryViewHolder(View v) {
            super(v);
            location = (TextView) v.findViewById(R.id.locationName);
            dateTime = (TextView) v.findViewById(R.id.dateTime);
            temperature = (TextView) v.findViewById(R.id.temp);
        }
    }

    public HistoryAdapter(String[] checkinHistory) {
        checkinHistory = checkinHistory;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(HistoryViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        //holder.textView.setText(checkinHistory[position]);

        CheckInData history = historyList.get(position);
        String date = history.getDate();
        String time = history.getTime();
        String dateTime = date + " " + time;
        holder.location.setText(history.getLocation());
        holder.dateTime.setText(dateTime);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return historyList.size();
    }

}