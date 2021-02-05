package com.test.test.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.test.test.Models.Footprint;
import com.test.test.Models.PalletType;
import com.test.test.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class PalletTypeAdapter extends ArrayAdapter<PalletType> {

    private final ArrayList<PalletType> mPalletType;
    private final LayoutInflater mInflater;

    public PalletTypeAdapter(@NonNull Context context, int resource, ArrayList<PalletType> items) {
        super(context, resource);
        this.mPalletType = items;
        mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row = mInflater.inflate(R.layout.spinner_item_row, parent, false);
        TextView t = row.findViewById(R.id.text);
        t.setText(mPalletType.get(position).id + ". " + mPalletType.get(position).Pallet_Type);
        return row;
    }

    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        View row = mInflater.inflate(R.layout.spinner_item_row, parent, false);
        TextView t = row.findViewById(R.id.text);
        t.setText(mPalletType.get(position).id + ". " + mPalletType.get(position).Pallet_Type);
        return row;
    }

    @Override
    public int getCount(){
        return mPalletType.size();
    }

    @Override
    public PalletType getItem(int position){
        return mPalletType.get(position);
    }

    @Override
    public long getItemId(int position){
        return Long.valueOf(mPalletType.get(position).id);
    }

}
