package com.test.test.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.test.test.Models.InventoryStatus;
import com.test.test.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class InventoryStatusAdapter extends ArrayAdapter<InventoryStatus> {

    private final ArrayList<InventoryStatus> mInventoryStatus;
    private final LayoutInflater mInflater;

    public InventoryStatusAdapter(@NonNull Context context, int resource, ArrayList<InventoryStatus> items) {
        super(context, resource);
        this.mInventoryStatus = items;
        mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row = mInflater.inflate(R.layout.spinner_item_row, parent, false);
        TextView t = row.findViewById(R.id.text);
        t.setText(mInventoryStatus.get(position).id + ". " + mInventoryStatus.get(position).inventory_status);
        return row;
    }

    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        View row = mInflater.inflate(R.layout.spinner_item_row, parent, false);
        TextView t = row.findViewById(R.id.text);
        t.setText(mInventoryStatus.get(position).id + ". " + mInventoryStatus.get(position).inventory_status);
        return row;
    }

    @Override
    public int getCount(){
        return mInventoryStatus.size();
    }

    @Override
    public InventoryStatus getItem(int position){
        return mInventoryStatus.get(position);
    }

    @Override
    public long getItemId(int position){
        return Long.valueOf(mInventoryStatus.get(position).id);
    }

}
