package com.test.test.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.style.BackgroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.test.test.Models.Cell;
import com.test.test.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;

public class AdapterCells extends AbstractDpadAdapter<AdapterCells.TypeTransportViewHolder>{

    private ArrayList<Cell> mItemModels;
    private final ArrayList<Cell> mOriginalItemModels;
    private String mMask="";
    private Context mContext;
    private int mSelectId;

    public void setCurrent(int id){
        mSelectId = id;
    }

    public AdapterCells(Context context, ArrayList<Cell> myDataset) {
        mContext = context;
        mItemModels = myDataset;
        mSelectId = -1;
        mOriginalItemModels = new ArrayList<>(myDataset.size());
        for(int i1 = 0, cnt1 = myDataset.size(); i1< cnt1; i1++){
            Cell i = myDataset.get(i1);
            Cell item = new Cell(
                    i.id,
                    i.Location,
                    i.Area,
                    i.Aisle,
                    i.Row,
                    i.Section,
                    i.Level,
                    i.Positon,
                    i.Max_Hight_cm,
                    i.Max_Width_cm,
                    i.Height_from_floor_m,
                    i.Max_Weight_kg,
                    i.Cell_Type,
                    i.WrhZone,
                    i.Deep_level,
                    i.pallet_type,
                    i.Warehouse);
            mOriginalItemModels.add(item);
        }
    }

    public void setMask(String mask){
        mMask = mask;
        ArrayList<Cell> mItemModelsWithMask = new ArrayList<>();
        for (int i1 = 0,cnt1=mOriginalItemModels.size();i1<cnt1;i1++){
            Cell i = mOriginalItemModels.get(i1);
            if(i.Location.indexOf(mask) != -1){
                Cell item = new Cell(
                        i.id,
                        i.Location,
                        i.Area,
                        i.Aisle,
                        i.Row,
                        i.Section,
                        i.Level,
                        i.Positon,
                        i.Max_Hight_cm,
                        i.Max_Width_cm,
                        i.Height_from_floor_m,
                        i.Max_Weight_kg,
                        i.Cell_Type,
                        i.WrhZone,
                        i.Deep_level,
                        i.pallet_type,
                        i.Warehouse);
                mItemModelsWithMask.add(item);
            }
            mItemModels = mItemModelsWithMask;
            notifyDataSetChanged();
        }
    }

    @Override
    public TypeTransportViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_cell, parent, false);
        TypeTransportViewHolder typeTransportViewHolder = new TypeTransportViewHolder(v);
        return typeTransportViewHolder;
    }

    @Override
    public void onBindViewHolder(TypeTransportViewHolder holder, final int position) {
        String Location = mItemModels.get(position).Location;
        if(mItemModels.get(position).id == mSelectId){
            holder.Location.setText(Location + " (CurrentSelected)");
        } else
            holder.Location.setText(Location);
        if(mMask.length() > 0) {
            int start = -1;
            int end = -1;
                start = Location.lastIndexOf(mMask);
                end = start + mMask.length();
                if (end > Location.length())
                    end = Location.length();
                int flag = Spannable.SPAN_INCLUSIVE_INCLUSIVE;
                holder.Location.getText().setSpan(new BackgroundColorSpan(Color.YELLOW), start, end, flag);
        }
        holder.Max_Width_cm.setText(String.valueOf(mItemModels.get(position).Max_Width_cm));
        holder.Max_Hight_cm.setText(String.valueOf(mItemModels.get(position).Max_Hight_cm));
        holder.Max_Weight_kg.setText(String.valueOf(mItemModels.get(position).Max_Weight_kg));

    }

    @Override
    public int getItemCount() {
        return mItemModels.size();
    }

    public Cell getCell(int position) {
        return mItemModels.get(position);
    }

    public class TypeTransportViewHolder extends RecyclerView.ViewHolder  {
        public EditText Location;
        public TextView Max_Width_cm;
        public TextView Max_Hight_cm;
        public TextView Max_Weight_kg;
        public View container;
        public TypeTransportViewHolder(View v) {
            super(v);
            container = v;
            Location = v.findViewById(R.id.Location);
            Max_Width_cm = v.findViewById(R.id.Max_Width_cm);
            Max_Hight_cm = v.findViewById(R.id.Max_Hight_cm);
            Max_Weight_kg = v.findViewById(R.id.Max_Weight_kg);
        }
    }

    public ArrayList<Cell> getItems(){
        return mItemModels;
    }
}
