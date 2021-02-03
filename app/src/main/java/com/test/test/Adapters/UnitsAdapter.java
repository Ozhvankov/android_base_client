package com.test.test.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.test.test.Models.Item_shipment_unit_type;
import com.test.test.Models.Lot;
import com.test.test.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class UnitsAdapter extends ArrayAdapter<Item_shipment_unit_type> {

    private final ArrayList<Item_shipment_unit_type> mLots;
    private final LayoutInflater mInflater;

    public UnitsAdapter(@NonNull Context context, int resource, ArrayList<Item_shipment_unit_type> items) {
        super(context, resource);
        this.mLots = items;
        mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row = mInflater.inflate(R.layout.spinner_item_row, parent, false);
        TextView t = row.findViewById(R.id.text);
        t.setText(mLots.get(position).id + ". " + mLots.get(position).Item_shipment_unit_type);
        return row;
    }

    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        View row = mInflater.inflate(R.layout.spinner_item_row, parent, false);
        TextView t = row.findViewById(R.id.text);
        t.setText(mLots.get(position).id + ". " + mLots.get(position).Item_shipment_unit_type);
        return row;
    }

    @Override
    public int getCount(){
        return mLots.size();
    }

    @Override
    public Item_shipment_unit_type getItem(int position){
        return mLots.get(position);
    }

    @Override
    public long getItemId(int position){
        return Long.valueOf(mLots.get(position).id);
    }

}
