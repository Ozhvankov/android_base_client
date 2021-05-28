package com.test.test.Adapters;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.test.test.Models.Item;
import com.test.test.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ItemsAdapter extends ArrayAdapter<Item> {

    private final ArrayList<Item> mItems;
    private final LayoutInflater mInflater;

    public ItemsAdapter(@NonNull Context context, int resource, ArrayList<Item> items) {
        super(context, resource);
        this.mItems = items;
        mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row = mInflater.inflate(R.layout.spinner_item_row, parent, false);
        TextView t = row.findViewById(R.id.text);
        t.setText(mItems.get(position).id + ". " + mItems.get(position).Item_No + " - " + mItems.get(position).Item_article);
        return row;
    }

    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        View row = mInflater.inflate(R.layout.spinner_item_row, parent, false);
        TextView t = row.findViewById(R.id.text);
        t.setText(mItems.get(position).id + ". " + mItems.get(position).Item_No + " - " + mItems.get(position).Item_article);
        return row;
    }

    @Override
    public int getCount(){
        return mItems.size();
    }

    @Override
    public Item getItem(int position){
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position){
        return Long.valueOf(mItems.get(position).id);
    }

}
