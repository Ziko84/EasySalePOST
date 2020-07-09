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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class MainActivity extends AppCompatActivity implements AsyncResponse {
    Button retrieve_json_btn, parse_json_btn;
    private String jsonString;
    private Dialog myDialog;
    private RequestQueue mQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //init Views
        myDialog = new Dialog(this);
        retrieve_json_btn = findViewById(R.id.retrieve_json_btn);
        parse_json_btn = findViewById(R.id.parse_json_btn);
        mQueue = Volley.newRequestQueue(getApplicationContext());

        //onClickListeners.
        retrieve_json_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { retrieveJsonDataAsync();
            }
        });

        parse_json_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createLocalJsonFile();
            }
        });
    }

    private void createLocalJsonFile() {
        // Define the File Path and its Name
        File file = new File(this.getFilesDir(),"easysale.json");
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
            Toast.makeText(this, "Problem Writing Data to JSON File", Toast.LENGTH_LONG).show();        }

    }


    private void retrieveJsonDataAsync() {
        Async async = new Async(this);
        async.delegate = this;
        async.execute();
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
