package com.example.wellsafe.api;

import android.os.AsyncTask;
import android.util.Log;

import com.example.wellsafe.ui.home.HomeFragment;
import com.example.wellsafe.ui.stats.StatsFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class FetchData extends AsyncTask<Void, Void, Void> {
    String data;
    String location;
    int confirmed;

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            URL url = new URL("https://covid2019-api.herokuapp.com/v2/country/malaysia");
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            while(line != null){
                line = bufferedReader.readLine();
                data = data + line;
            }

            JSONObject caseData = new JSONObject(data);
            JSONObject malaysiaData = caseData.getJSONObject("data");
            confirmed = malaysiaData.getInt("confirmed");
            location = malaysiaData.getString("location");

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        Log.e("API from api", String.valueOf(this.confirmed));
            //HomeFragment.inputData = this.data;
    }
}
