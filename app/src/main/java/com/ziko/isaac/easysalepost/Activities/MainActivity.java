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

import com.ziko.isaac.easysalepost.AsyncTask.Async;
import com.ziko.isaac.easysalepost.Interfaces.AsyncResponse;
import com.ziko.isaac.easysalepost.R;

public class MainActivity extends AppCompatActivity implements AsyncResponse {

    private String jsonString;
    private Dialog myDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myDialog = new Dialog(this);
        retrieveJsonDataAsync();

    }

    private void retrieveJsonDataAsync() {
        Async async = new Async(this);
        async.delegate = this;
        async.execute();
    }

    @Override
    public void asyncResult(String output) {
        Toast.makeText(this, output, Toast.LENGTH_SHORT).show();
        this.jsonString = output;
    }

    public void showPopup(View view) {
        TextView txtClose;
        Button btnFollow;
        myDialog.setContentView(R.layout.custom_popup);
        txtClose = myDialog.findViewById(R.id.close);
        btnFollow = myDialog.findViewById(R.id.follow_me);

        txtClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDialog.dismiss();
            }
        });
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }
}
