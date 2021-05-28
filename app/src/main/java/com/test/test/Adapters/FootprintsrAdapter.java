package com.test.test.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.test.test.Models.Footprint;
import com.test.test.Models.Lot;
import com.test.test.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class FootprintsrAdapter extends ArrayAdapter<Footprint> {

    private final ArrayList<Footprint> mFootprints;
    private final LayoutInflater mInflater;

    public FootprintsrAdapter(@NonNull Context context, int resource, ArrayList<Footprint> items) {
        super(context, resource);
        this.mFootprints = items;
        mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row = mInflater.inflate(R.layout.spinner_item_row, parent, false);
        TextView t = row.findViewById(R.id.text);
        t.setText(mFootprints.get(position).id + ". " + mFootprints.get(position).footprint_name);
        return row;
    }

    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        View row = mInflater.inflate(R.layout.spinner_item_row, parent, false);
        TextView t = row.findViewById(R.id.text);
        t.setText(mFootprints.get(position).id + ". " + mFootprints.get(position).footprint_name);
        return row;
    }

    @Override
    public int getCount(){
        return mFootprints.size();
    }

    @Override
    public Footprint getItem(int position){
        return mFootprints.get(position);
    }

    @Override
    public long getItemId(int position){
        return Long.valueOf(mFootprints.get(position).id);
    }

}
