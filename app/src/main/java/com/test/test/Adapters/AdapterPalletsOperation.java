package com.test.test.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.style.BackgroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.test.test.Models.ItemModel;
import com.test.test.R;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;

public class AdapterPalletsOperation extends RecyclerView.Adapter<AdapterPalletsOperation.TypeTransportViewHolder>{

    private ArrayList<ItemModel> mItemModels;
    private final ArrayList<ItemModel> mOriginalItemModels;
    private String mMask="";
    private Context mContext;

    public AdapterPalletsOperation(Context context, ArrayList<ItemModel> myDataset) {
        mContext = context;
        mItemModels = myDataset;
        mOriginalItemModels = new ArrayList<>(myDataset.size());
        for(int i1 = 0, cnt1 = myDataset.size(); i1< cnt1; i1++){
            ItemModel i = myDataset.get(i1);
            ItemModel item = new ItemModel(
                    i.Initial_PRINTED_LPN,
                    i.Inbound_shipment_number,
                    i.item_article,
                    i.footprint_name,
                    i.inventory_status,
                    i.name,
                    i.shelf_life_days,
                    i.Implementation_period,
                    i.item_weight,
                    i.item_box,
                    i.id,
                    i.fact_item_weight,
                    i.fact_item_box,
                    i.fact_weight_empty_box,
                    i.fact_weight_empty_pallet,
                    i.production_date,
                    i.number_party,
                    i.item_id,
                    i.wrh_zone,
                    i.Manufacturing_Date,
                    i.inbound_date,
                    i.Lot_number_batch,
                    i.Transport_Equipment_Number,
                    i.Pallet_Type,
                    i.pallet_name,
                    i.plan_item_weight,
                    i.plan_item_box,
                    i.cell_id,
                    i.netto,
                    i.staging_location,
                    i.item_no
            );
            mOriginalItemModels.add(item);
        }
    }

    public void setMask(String mask){
        mMask = mask;
        ArrayList<ItemModel> mItemModelsWithMask = new ArrayList<>();
        for (int i1 = 0,cnt1=mOriginalItemModels.size();i1<cnt1;i1++){
            ItemModel i = mOriginalItemModels.get(i1);
            if(i.Initial_PRINTED_LPN.indexOf(mask) != -1){
                ItemModel item = new ItemModel(
                        i.Initial_PRINTED_LPN,
                        i.Inbound_shipment_number,
                        i.item_article,
                        i.footprint_name,
                        i.inventory_status,
                        i.name,
                        i.shelf_life_days,
                        i.Implementation_period,
                        i.item_weight,
                        i.item_box,
                        i.id,
                        i.fact_item_weight,
                        i.fact_item_box,
                        i.fact_weight_empty_box,
                        i.fact_weight_empty_pallet,
                        i.production_date,
                        i.number_party,
                        i.item_id,
                        i.wrh_zone,
                        i.Manufacturing_Date,
                        i.inbound_date,
                        i.Lot_number_batch,
                        i.Transport_Equipment_Number,
                        i.Pallet_Type,
                        i.pallet_name,
                        i.plan_item_weight,
                        i.plan_item_box,
                        i.cell_id,
                        i.netto,
                        i.staging_location,
                        i.item_no
                );
                mItemModelsWithMask.add(item);
            }
            mItemModels = mItemModelsWithMask;
            notifyDataSetChanged();
        }
    }

    @Override
    public TypeTransportViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_pallete, parent, false);
        TypeTransportViewHolder typeTransportViewHolder = new TypeTransportViewHolder(v);
        return typeTransportViewHolder;
    }

    @Override
    public void onBindViewHolder(TypeTransportViewHolder holder, final int position) {
        holder.in_cell.setVisibility(View.GONE);
        String Initial_PRINTED_LPN = mItemModels.get(position).Initial_PRINTED_LPN;
        holder.name.setText(Initial_PRINTED_LPN);
        if(mMask.length() > 0) {
            int start = Initial_PRINTED_LPN.indexOf(mMask);
            int end = start + mMask.length();
            if(end > Initial_PRINTED_LPN.length())
                end = Initial_PRINTED_LPN.length();
            int flag = Spannable.SPAN_INCLUSIVE_INCLUSIVE;
            holder.name.getText().setSpan(new BackgroundColorSpan(Color.YELLOW), start, end, flag);
        }

    }

    @Override
    public int getItemCount() {
        return mItemModels.size();
    }

    public ItemModel getPallet(int position) {
        return mItemModels.get(position);
    }

    public class TypeTransportViewHolder extends RecyclerView.ViewHolder  {
        public EditText name;
        public View in_cell;
        public View container;
        public TypeTransportViewHolder(View v) {
            super(v);
            container = v;
            name = v.findViewById(R.id.Location);
            in_cell = v.findViewById(R.id.in_cell);
        }
    }

    public ArrayList<ItemModel> getItems(){
        return mItemModels;
    }
}
