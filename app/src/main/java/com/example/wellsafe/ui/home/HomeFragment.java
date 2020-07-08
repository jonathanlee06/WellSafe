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

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class HomeFragment extends Fragment {

    ArrayList<String> malaysiaList = new ArrayList<>();
    public static String inputData;
    View view;
    TextView totalCases;
    TextView totalRecoveries;
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

        /*    JSONObject jsonObject = new JSONObject(inputData);
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
