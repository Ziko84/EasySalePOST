package com.ziko.isaac.easysalepost.AsyncTask;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.widget.Toast;

import com.ziko.isaac.easysalepost.SQLite.EasySaleContract;
import com.ziko.isaac.easysalepost.SQLite.EasySaleDBHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class AsyncForSQLite extends AsyncTask<Void, Void, String> {
    //todo weak reference
    Context context;
    private SQLiteDatabase mDatabase;

    public AsyncForSQLite(Context context) {
        this.context = context;
    }



    @Override
    protected String doInBackground(Void... voids) {
        EasySaleDBHelper easySaleDBHelper = new EasySaleDBHelper(context);

        mDatabase = easySaleDBHelper.getWritableDatabase();

        String item_name, picture_link;
        int sale_nis, quantity;
        File file = new File(context.getFilesDir(), "easysale.json");
        FileReader fileReader = null;
        try {
            fileReader = new FileReader(file);
        } catch (FileNotFoundException e) {
            Toast.makeText(context, "File Not Found :(", Toast.LENGTH_SHORT).show();
        }
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        try {
            line = bufferedReader.readLine();
            while (line != null) {
                stringBuilder.append(line).append("\n");
                line = bufferedReader.readLine();
            }
            bufferedReader.close();
        } catch (IOException e) {
            Toast.makeText(context, "Was a problem reading from the File", Toast.LENGTH_SHORT).show();
        }

        //Json Format String
        String s = stringBuilder.toString();

        //transfer from file to variables.
        try {
            String jsonFormat = s;
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
        } catch (JSONException e) {
        }



        return "Successfully Added Data To SQLite DB";
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
    }
}
