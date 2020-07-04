package com.example.wellsafe.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.wellsafe.R;
import com.example.wellsafe.api.FetchData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class HomeFragment extends Fragment {

    ArrayList<String> malaysiaList = new ArrayList<>();
    public static String inputData;
    View view;
    TextView totalCases;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FetchData process  = new FetchData();
        process.execute();
        view = inflater.inflate(R.layout.fragment_home, container, false);
        totalCases = (TextView) view.findViewById(R.id.totalCases);
        try {
            get_json();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return view;
    }

    public void get_json() throws JSONException {
            JSONObject jsonObject = new JSONObject(inputData);
            JSONObject malaysiaData = jsonObject.getJSONObject("data");

            //totalCases.setText(malaysiaData.getString("confirmed"));
        String confirmed = malaysiaData.getString("confirmed");
        Log.d("API test", confirmed + " or not");

            //malaysiaList.add(jsonObject.getString("data"));
            /* malaysiaList.add(jsonObject.getString("deaths"));
            malaysiaList.add(jsonObject.getString("recovered"));
            malaysiaList.add(jsonObject.getString("active")); */
    }
}
