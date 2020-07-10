package com.example.wellsafe.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.wellsafe.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.logging.Level;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

public class HomeFragment extends Fragment {

    ArrayList<String> malaysiaList = new ArrayList<>();
    public static String inputData;
    View view;
    public static TextView totalCases;
    public static TextView totalRecoveries;
    public static TextView text_home;
    public static TextView proximityRating;
    JSONObject malaysiaData;
    public static int confirmed;
    public static int recovered;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         /*try {
            get_json();

        } catch (JSONException e) {
            e.printStackTrace();
        }*/

        view = inflater.inflate(R.layout.fragment_home, container, false);
        totalCases = (TextView) view.findViewById(R.id.totalCases);
        totalRecoveries = (TextView) view.findViewById(R.id.totalRecoveries);
        text_home = (TextView) view.findViewById(R.id.text_home);
        proximityRating = (TextView) view.findViewById(R.id.proximityRating);
        totalCases.setText(String.valueOf(confirmed));
        totalRecoveries.setText(String.valueOf(recovered));

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        /* try {
            get_json();

        } catch (JSONException e) {
            e.printStackTrace();
        } */
    }

    private void get_json() throws JSONException {
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        String URL = "https://covid2019-api.herokuapp.com/v2/country/malaysia";

        JsonObjectRequest objectRequest = new JsonObjectRequest(
                Request.Method.GET,
                URL,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //Log.e("API Response", response.toString());
                        try {
                            malaysiaData = response.getJSONObject("data");
                            //totalCases = (TextView) getActivity().findViewById(R.id.totalCases);
                            String country = malaysiaData.getString("location");
                            confirmed = malaysiaData.getInt("confirmed");
                            recovered = malaysiaData.getInt("recovered");

                            //Log.e("location response", malaysiaData.getString("data"));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error", error.toString());
                    }
                }
        );

        requestQueue.add(objectRequest);
    }

    private static class Level {
        String text;
        int textColor;
        int image;
    }

    private Level getLevel(int userCount) {
        Level level = new Level();
        level.text = "-";

        if (userCount <= 200) {
            level.text = getString(R.string.level1);
            level.textColor = R.color.colorLevel1;
        } else if (userCount <= 400) {
            level.text = getString(R.string.level2);
            level.textColor = R.color.colorLevel2;
        } else if (userCount <= 800) {
            level.text = getString(R.string.level3);
            level.textColor = R.color.colorLevel3;
        } else {
            level.text = getString(R.string.level4);
            level.textColor = R.color.colorLevel4;
        }

        return level;
    }
}
