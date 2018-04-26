package com.example.cse6324.university_bazaar_system;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Map;
import java.util.ArrayList;

public class MyRecyclerViewAdapter extends RecyclerView
        .Adapter<MyRecyclerViewAdapter
        .DataObjectHolder>
        implements Filterable {
    private static String LOG_TAG = "MyRecyclerViewAdapter";

    @SuppressWarnings("unchecked")
    private ArrayList<Map<String, Object>> items, mFilteredList;
    private Map<String, byte[]> images;
    private static MyClickListener myClickListener;

    public class DataObjectHolder extends RecyclerView.ViewHolder
            implements View
            .OnClickListener {
        ImageView img;
        TextView item_name;
        TextView item_desc;

        public DataObjectHolder(View itemView) {
            super(itemView);
            img = (ImageView) itemView.findViewById(R.id.item_pic);
            item_name = (TextView) itemView.findViewById(R.id.item_name);
            item_desc = (TextView) itemView.findViewById(R.id.item_desc);
            //Log.i(LOG_TAG, "Adding Listener");
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            myClickListener.onItemClick(MyRecyclerViewAdapter.this.mFilteredList.get(getAdapterPosition()), v);
        }
    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    public MyRecyclerViewAdapter(ArrayList<Map<String, Object>> its, Map<String, byte[]> imgs) {
        items = its;
        mFilteredList = its;
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
        byte[] img = images.get(mFilteredList.get(position).get("imgPath"));
        holder.img.setImageBitmap(BitmapFactory.decodeByteArray(img, 0, img.length));
        holder.item_name.setText(mFilteredList.get(position).get("etName").toString());
        holder.item_desc.setText(mFilteredList.get(position).get("etDesc").toString());
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
        return mFilteredList.size();
    }

    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();

                if (charString.isEmpty()) {
                    mFilteredList = items;
                } else {
                    ArrayList<Map<String, Object>> filteredList = new ArrayList<>();

                    for (Map<String, Object> item : items) {
                        if (((String)item.get("etName")).toLowerCase().contains(charString) || ((String)item.get("etDesc")).toLowerCase().contains(charString)) {
                            filteredList.add(item);
                        }
                    }
                    mFilteredList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mFilteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mFilteredList = (ArrayList<Map<String, Object>>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface MyClickListener {
        public void onItemClick(Map<String, Object> item, View v);
    }
}