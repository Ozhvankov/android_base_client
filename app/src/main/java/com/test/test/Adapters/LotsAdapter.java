package com.test.test.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.test.test.Models.Item;
import com.test.test.Models.Lot;
import com.test.test.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class LotsAdapter extends ArrayAdapter<Lot> {

    private final ArrayList<Lot> mLots;
    private final LayoutInflater mInflater;

    public LotsAdapter(@NonNull Context context, int resource, ArrayList<Lot> items) {
        super(context, resource);
        this.mLots = items;
        mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row = mInflater.inflate(R.layout.spinner_item_row, parent, false);
        TextView t = row.findViewById(R.id.text);
        t.setText(mLots.get(position).id + ". " + mLots.get(position).Name);
        return row;
    }

    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        View row = mInflater.inflate(R.layout.spinner_item_row, parent, false);
        TextView t = row.findViewById(R.id.text);
        t.setText(mLots.get(position).id + ". " + mLots.get(position).Name);
        return row;
    }

    @Override
    public int getCount(){
        return mLots.size();
    }

    @Override
    public Lot getItem(int position){
        return mLots.get(position);
    }

    @Override
    public long getItemId(int position){
        return Long.valueOf(mLots.get(position).id);
    }

}
