package com.ziko.isaac.easysalepost.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.ziko.isaac.easysalepost.AsyncTask.Async;
import com.ziko.isaac.easysalepost.Interfaces.AsyncResponse;
import com.ziko.isaac.easysalepost.Model.EasySale;
import com.ziko.isaac.easysalepost.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class MainActivity extends AppCompatActivity implements AsyncResponse {
    private Button retrieve_json_btn, parse_json_btn, transferToSQLite;
    private String jsonString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //init Views
        Dialog myDialog = new Dialog(this);

        retrieve_json_btn = findViewById(R.id.retrieve_json_btn);
        parse_json_btn = findViewById(R.id.saveToFile);
        transferToSQLite = findViewById(R.id.transferToSQLite);

        RequestQueue mQueue = Volley.newRequestQueue(getApplicationContext());

        //onClickListeners.
        retrieve_json_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                retrieveJsonDataAsync();
            }
        });
        parse_json_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveLocalJsonFile();
            }
        });
        transferToSQLite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                transferFromLocalFileToSQLiteDB();
            }
        });
    }

    private void retrieveJsonDataAsync() {
        Async async = new Async(this);
        async.delegate = this;
        async.execute();
    }

    private void saveLocalJsonFile() {
        // Define the File Path and its Name
        File file = new File(this.getFilesDir(), "easysale.json");
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(file);
        } catch (IOException e) {
            Toast.makeText(this, "There was a Problem creating the File", Toast.LENGTH_LONG).show();
        }
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        try {
            bufferedWriter.write(jsonString);
            bufferedWriter.close();

        } catch (IOException e) {
            Toast.makeText(this, "Problem Writing Data to JSON File", Toast.LENGTH_LONG).show();
        }
        Toast.makeText(this, "Successfully Saved Data To JSON File", Toast.LENGTH_SHORT).show(); }

    private void transferFromLocalFileToSQLiteDB() {

        try {
            String jsonFormat = readFromFile();
            JSONObject jsonObject  = new JSONObject(jsonFormat);
            JSONObject data = jsonObject.getJSONObject("data");
            JSONArray item_list2 = data.getJSONArray("item_list");
            for (int i = 0; i < item_list2.length(); i++) {

                String item_name = item_list2.getJSONObject(i).getString("item_name");
                String item_extended_description = item_list2.getJSONObject(i).getString("item_extended_description");
                String picture_link = item_list2.getJSONObject(i).getString("picture_link");
                int sale_nis = item_list2.getJSONObject(i).getInt("sale_nis");
                int quantity = item_list2.getJSONObject(i).getInt("quantity");


            }


        } catch (JSONException e) {
            Toast.makeText(this, "Problem finding this 'Key' value", Toast.LENGTH_SHORT).show();        }
    }

    private String readFromFile() {
        File file = new File(this.getFilesDir(), "easysale.json");
        FileReader fileReader = null;
        try {
            fileReader = new FileReader(file);
        } catch (FileNotFoundException e) {
            Toast.makeText(this, "File Not Found :(", Toast.LENGTH_SHORT).show();
        }
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        StringBuilder stringBuilder = new StringBuilder();
        String line = null;
        try {
            line = bufferedReader.readLine();
            while (line != null) {
                stringBuilder.append(line).append("\n");
                line = bufferedReader.readLine();
            }
            bufferedReader.close();
        } catch (IOException e) {
            Toast.makeText(this, "Was a problem reading from the File", Toast.LENGTH_SHORT).show();
        }

        //Json Format String
        return stringBuilder.toString();
    }

    @Override
    public void asyncResult(String output) {
        Toast.makeText(this, "New JSON Data Received", Toast.LENGTH_SHORT).show();
        this.jsonString = output;
    }

//    public void showPopup(View view) {
//        TextView txtClose;
//        Button btnFollow;
//        myDialog.setContentView(R.layout.custom_popup);
//        txtClose = myDialog.findViewById(R.id.close);
//        btnFollow = myDialog.findViewById(R.id.follow_me);
//
//        txtClose.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                myDialog.dismiss();
//            }
//        });
//        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        myDialog.show();
//    }
}
