package com.ziko.isaac.easysalepost.Adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.squareup.picasso.Picasso;
import com.ziko.isaac.easysalepost.R;
import com.ziko.isaac.easysalepost.SQLite.EasySaleContract;

public class EasySaleAdapter extends RecyclerView.Adapter<EasySaleAdapter.EasySaleViewHolder> {
    private Context mContext;
    private Cursor mCursor;

    public EasySaleAdapter(Context context, Cursor cursor) {
        this.mContext = context;
        this.mCursor = cursor;
    }

    @NonNull
    @Override
    public EasySaleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new EasySaleViewHolder(LayoutInflater.from(mContext)
                .inflate(R.layout.single_row_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull EasySaleViewHolder holder, int position) {

        //extract to variables
        if(!mCursor.moveToPosition(position)){
            return;
        }
        String name = mCursor.getString(mCursor.getColumnIndex(EasySaleContract.EasySaleEntry.COLUMN_NAME));
        String url = mCursor.getString(mCursor.getColumnIndex(EasySaleContract.EasySaleEntry.COLUMN_IMAGE));
        int price = mCursor.getInt(mCursor.getColumnIndex(EasySaleContract.EasySaleEntry.COLUMN_PRICE));
        int quantity = mCursor.getInt(mCursor.getColumnIndex(EasySaleContract.EasySaleEntry.COLUMN_QUANTITY));

        //set data to views
        holder.name.setText(name);
        holder.price.setText(String.valueOf(price));
        holder.quantity.setText(String.valueOf(quantity));

        if(!url.isEmpty()&&url!="null"){
            Picasso.get().load(url).fit().centerInside().into(holder.item_iv);
        } else{
            holder.item_iv.setImageResource(R.drawable.circlecropped3);
            Toast.makeText(mContext, "Empty URL Detected", Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public int getItemCount() {
       return mCursor.getCount();
    }

    public class EasySaleViewHolder extends RecyclerView.ViewHolder {
        public TextView name, price, quantity;
        public ImageView item_iv;

        public EasySaleViewHolder(@NonNull View itemView) {
            super(itemView);
            item_iv = itemView.findViewById(R.id.item_iv);
            name = itemView.findViewById(R.id.name_tv);
            price = itemView.findViewById(R.id.price_tv);
            quantity = itemView.findViewById(R.id.quantity_tv);
        }
    }
}

