package com.ziko.isaac.easysalepost.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
    private int quantity = 0;

    public EasySaleAdapter(Context context, Cursor cursor) {
        this.mContext = context;
        this.mCursor = cursor;
    }

    @NonNull
    @Override
    public EasySaleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.single_row_item, parent, false);
        EasySaleViewHolder easySaleViewHolder = new EasySaleViewHolder(v);
        //Dialog init
        final Dialog details_dialog = new Dialog(mContext);
        details_dialog.setContentView(R.layout.item_dialog);
        Window window = details_dialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        easySaleViewHolder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView dialog_name = details_dialog.findViewById(R.id.dialog_name);
                TextView dialog_quantity = details_dialog.findViewById(R.id.dialog_quantity);
                TextView dialog_price = details_dialog.findViewById(R.id.dialog_price);
                ImageView dialog_image = details_dialog.findViewById(R.id.dialog_image);
                dialog_name.setText(R.string.PRODUCT_NAME);
                dialog_quantity.setText(R.string.UNITS);
                dialog_price.setText(R.string.פRICING);
                Picasso.get().load(R.drawable.circlecropped3).fit().centerInside().into(dialog_image);
                details_dialog.show();

            }
        });
        return easySaleViewHolder;


    }

    @Override
    public void onBindViewHolder(@NonNull EasySaleViewHolder holder, int position) {

        //animations
        holder.item_iv.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.fade_translate));
        holder.container.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.fade_scale));

        //extract to variables
        if (!mCursor.moveToPosition(position)) {
            return;
        }
        String name = mCursor.getString(mCursor.getColumnIndex(EasySaleContract.EasySaleEntry.COLUMN_NAME));
        String url = mCursor.getString(mCursor.getColumnIndex(EasySaleContract.EasySaleEntry.COLUMN_IMAGE));
        int price = mCursor.getInt(mCursor.getColumnIndex(EasySaleContract.EasySaleEntry.COLUMN_PRICE));
        int quantity = mCursor.getInt(mCursor.getColumnIndex(EasySaleContract.EasySaleEntry.COLUMN_QUANTITY));
        if (quantity < 0) {
            quantity = 0;
        }

        //get id for swipe to delete mehod
        long id = mCursor.getLong(mCursor.getColumnIndex(EasySaleContract.EasySaleEntry._ID));

        //set data to views
        holder.itemView.setTag(id);
        holder.name.setText(name);
        holder.price.setText(String.valueOf(price + " ₪ "));
        holder.quantity.setText(String.valueOf(quantity + " יחידות "));

        if (!url.isEmpty() && !url.equals("null")) {
            Picasso.get().load(url).fit().centerInside().into(holder.item_iv);
        } else {
            holder.item_iv.setImageResource(R.drawable.not_a);
        }


    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    public void swapCursor(Cursor newCursor) {
        if (mCursor != null) {
            mCursor.close();
        }
        mCursor = newCursor;
        if (newCursor != null) {
            notifyDataSetChanged();

        }
    }

    public static class EasySaleViewHolder extends RecyclerView.ViewHolder {
        public TextView name, price, quantity;
        public ImageView item_iv;
        public RelativeLayout container;


        public EasySaleViewHolder(@NonNull View itemView) {
            super(itemView);
            container = itemView.findViewById(R.id.container);
            item_iv = itemView.findViewById(R.id.item_iv);
            name = itemView.findViewById(R.id.name_tv);
            price = itemView.findViewById(R.id.price_tv);
            quantity = itemView.findViewById(R.id.quantity_tv);
        }
    }
}

