package com.example.cse6324.university_bazaar_system;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Map;
import java.util.ArrayList;

public class MyRecyclerViewAdapter extends RecyclerView
        .Adapter<MyRecyclerViewAdapter
        .DataObjectHolder> {
    private static String LOG_TAG = "MyRecyclerViewAdapter";
    private ArrayList<Map<String, Object>> items;
    private Map<String, byte[]> images;
    private static MyClickListener myClickListener;

    public static class DataObjectHolder extends RecyclerView.ViewHolder
            implements View
            .OnClickListener {
        ImageView img;
        TextView item_name;

        public DataObjectHolder(View itemView) {
            super(itemView);
            img = (ImageView) itemView.findViewById(R.id.item_pic);
            item_name = (TextView) itemView.findViewById(R.id.item_name);
            Log.i(LOG_TAG, "Adding Listener");
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            myClickListener.onItemClick(getAdapterPosition(), v);
        }
    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    public MyRecyclerViewAdapter(ArrayList<Map<String, Object>> its, Map<String, byte[]> imgs) {
        items = its;
        images = imgs;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view_row, parent, false);

        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
        byte[] img = images.get(items.get(position).get("imgPath"));
        holder.img.setImageBitmap(BitmapFactory.decodeByteArray(img, 0, img.length));
        holder.item_name.setText(items.get(position).get("etName").toString());
    }
/*
    public void addItem(ArrayList<Map<String, Object>> dataObj, int index) {
        items.add(index, dataObj);
        notifyItemInserted(index);
    }

    public void deleteItem(int index) {
        items.remove(index);
        notifyItemRemoved(index);
    }*/

    @Override
    public int getItemCount() {
        return items.size();
    }

    public interface MyClickListener {
        public void onItemClick(int position, View v);
    }
}