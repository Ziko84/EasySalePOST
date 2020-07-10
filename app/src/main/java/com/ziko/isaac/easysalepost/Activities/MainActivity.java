package com.ziko.isaac.easysalepost.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.ziko.isaac.easysalepost.Adapters.EasySaleAdapter;
import com.ziko.isaac.easysalepost.AsyncTask.Async;
import com.ziko.isaac.easysalepost.Interfaces.AsyncResponse;
import com.ziko.isaac.easysalepost.R;
import com.ziko.isaac.easysalepost.SQLite.EasySaleContract;
import com.ziko.isaac.easysalepost.SQLite.EasySaleDBHelper;

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
    private SQLiteDatabase mDatabase;
    private String jsonString;
    private EasySaleAdapter adapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //init Views and DB
        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(this));
        EasySaleDBHelper easySaleDBHelper = new EasySaleDBHelper(this);
        mDatabase = easySaleDBHelper.getWritableDatabase();
        recyclerView = findViewById(R.id.rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        Dialog myDialog = new Dialog(this);
        RequestQueue mQueue = Volley.newRequestQueue(getApplicationContext());
        Button retrieve_json_btn = findViewById(R.id.retrieve_json_btn);
        Button parse_json_btn = findViewById(R.id.saveToFile);
        Button transferToSQLite = findViewById(R.id.transferToSQLite);
        Button from_SQLite_to_rv_btn = findViewById(R.id.from_SQLite_to_rv_btn);

        //onClickListeners.
        retrieve_json_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { retrieveJsonDataAsync();
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
            public void onClick(View view) { transferFromLocalFileToSQLiteDB();
            }
        });
        from_SQLite_to_rv_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cursor allItemsFromSQLiteDB = getAllItemsFromSQLiteDB();
                setAdapter(allItemsFromSQLiteDB);
            }
        });
    }

    @Override
    public void asyncResult(String output) {
        Toast.makeText(this, "New JSON Data Received", Toast.LENGTH_SHORT).show();
        this.jsonString = output;
    }

    private void retrieveJsonDataAsync() {
        Async async = new Async(this);
        async.delegate = this;
        async.execute();
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
        Toast.makeText(this, "Successfully Saved Data To JSON File", Toast.LENGTH_SHORT).show();
    }

    private void transferFromLocalFileToSQLiteDB() {
        String item_name, picture_link;
        int sale_nis, quantity;

        //transfer from file to variables.
        try {
            String jsonFormat = readFromFile();
            JSONObject jsonObject = new JSONObject(jsonFormat);
            JSONObject data = jsonObject.getJSONObject("data");
            JSONArray item_list2 = data.getJSONArray("item_list");
            for (int i = 0; i < item_list2.length(); i++) {
                item_name = item_list2.getJSONObject(i).getString("item_name");
                picture_link = item_list2.getJSONObject(i).getString("picture_link");
                sale_nis = item_list2.getJSONObject(i).getInt("sale_nis");
                quantity = item_list2.getJSONObject(i).getInt("quantity");

                //Store in SQLite
                ContentValues cv = new ContentValues();
                cv.put(EasySaleContract.EasySaleEntry.COLUMN_NAME, item_name);
                cv.put(EasySaleContract.EasySaleEntry.COLUMN_IMAGE, picture_link);
                cv.put(EasySaleContract.EasySaleEntry.COLUMN_PRICE, sale_nis);
                cv.put(EasySaleContract.EasySaleEntry.COLUMN_QUANTITY, quantity);

                mDatabase.insert(EasySaleContract.EasySaleEntry.TABLE_NAME, null, cv);

            }
        } catch (JSONException e) { }
        Toast.makeText(this, "Successfully Added Data To SQLite DB", Toast.LENGTH_SHORT).show();
    }

    private Cursor getAllItemsFromSQLiteDB(){
        return mDatabase.query(
                EasySaleContract.EasySaleEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );
    }

    private void setAdapter(Cursor cursor){
        recyclerView.setVisibility(View.VISIBLE);
        adapter = new EasySaleAdapter(this, cursor);
        recyclerView.setAdapter(adapter);
    }
}
