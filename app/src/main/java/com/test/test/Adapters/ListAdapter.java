package com.test.test.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.test.test.Activities.Inbound.DetailAWAYActivity;
import com.test.test.Activities.Inbound.DetailActivity;
import com.test.test.Models.ListModel;
import com.test.test.R;

import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ListViewHolder> {

    private List<ListModel> mListModels;
    private Context mContext;

    public ListAdapter(Context context, List<ListModel> model) {
        this.mContext = context;
        this.mListModels = model;
    }

    public void clearAll() {
        mListModels.clear();
        notifyDataSetChanged();
    }

    public void changeList(List<ListModel> models) {
        this.mListModels = models;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rv_item, parent, false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, final int position) {
        holder.shipmentNmber.setText(mListModels.get(position).Inbound_shipment_number);
        holder.articles.setText("");//mListModels.get(position).Item_articles);
        holder.status.setText(String.valueOf(mListModels.get(position).status_id));
        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mListModels.get(position).status_id == ListModel.STATUS_INPUT_FACT) {
                    Intent intent = new Intent(mContext, DetailActivity.class);
                    intent.putExtra("mListModel", mListModels.get(position));
                    mContext.startActivity(intent);
                } else if(mListModels.get(position).status_id == ListModel.STATUS_PUT_AWAY) {

                    Intent intent = new Intent(mContext, DetailAWAYActivity.class);
                    intent.putExtra("mListModel", mListModels.get(position));
                    mContext.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mListModels.size();
    }

    class ListViewHolder extends RecyclerView.ViewHolder {

        TextView shipmentNmber;
        TextView articles;
        TextView status;
        LinearLayout container;

        ListViewHolder(@NonNull View itemView) {
            super(itemView);
            shipmentNmber = itemView.findViewById(R.id.shipment_number_text);
            articles = itemView.findViewById(R.id.articles_text);
            status = itemView.findViewById(R.id.status_text);
            container = itemView.findViewById(R.id.container);
        }
    }
}
