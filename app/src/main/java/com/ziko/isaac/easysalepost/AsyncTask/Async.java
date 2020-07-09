package com.ziko.isaac.easysalepost.AsyncTask;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.ziko.isaac.easysalepost.Interfaces.AsyncResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Async extends AsyncTask<Void, Integer, String> {

    private static final String SERVER_URL = "https://api.rivhit.co.il/online/RivhitOnlineAPI.svc/Item.List";
    private static final String TOKEN = "A2DACF08-55F1-4D03-90B7-8839E66AE57A";
    public AsyncResponse delegate = null;
    private Context ctx;

    public Async(Context context) {
        this.ctx = context;
        this.delegate = delegate;
    }


    @Override
    protected String doInBackground(Void... voids) {
        int count = 1;
        StringBuilder bstrb = null;
        try {
            publishProgress(count++);
            URL url = new URL(SERVER_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            //conn.setConnectTimeout(240000);
            //conn.setReadTimeout(240000);


            JSONObject json = new JSONObject();
            try {
                json.put("api_token", TOKEN);


            } catch (JSONException e) {
                return "Fail1";
            }

            String input = json.toString();

            OutputStream os = conn.getOutputStream();
            os.write(input.getBytes());
            os.flush();

            if (conn.getResponseCode() != 200) {
                return "Fail2";
            }
            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

            String output;
            bstrb = new StringBuilder();

            int maxProgress = 0;
            while ((output = br.readLine()) != null) {
                publishProgress(count % (maxProgress - 1));
                bstrb.append(output);
            }
            publishProgress(maxProgress);
            conn.disconnect();

        } catch (MalformedURLException e) {
            Log.d("TAG", "Fail MalformedURLException" + e.getMessage());
            return "Fail3";
        } catch (UnsupportedEncodingException e) {
            Log.d("TAG", "Fail UnsupportedEncodingException" + e.getMessage());
            return "Fail4";
        } catch (IllegalStateException e) {
            Log.d("TAG", "Fail IllegalStateException" + e.getMessage());
            return "Fail5";
        } catch (IOException e) {
            Log.d("TAG", "Fail IOException" + e.getMessage());
            return "Fail6";
        }
        return bstrb.toString();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        delegate.asyncResult(s);
    }





}

