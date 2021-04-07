package com.test.test.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.KeyEvent;
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

public class ListAdapter extends AbstractDpadAdapter<ListAdapter.ListViewHolder> {

    private List<ListModel> mListModels;
    private Context mContext;

    public ListAdapter(Context context, List<ListModel> model) {
        this.mContext = context;
        this.mListModels = model;
    }

    @Override
    public void onAttachedToRecyclerView(final RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

        // Handle key up and key down and attempt to move selection
        recyclerView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                RecyclerView.LayoutManager lm = recyclerView.getLayoutManager();

                // Return false if scrolled to the bounds and allow focus to move off the list
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
                        return tryMoveSelection(lm, 1);
                    } else if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
                        return tryMoveSelection(lm, -1);
                    }
                }

                return false;
            }
        });
    }

    public void clearAll() {
        mListModels.clear();
        notifyDataSetChanged();
    }

    public void changeList(List<ListModel> models) {
        this.mListModels = models;
        notifyDataSetChanged();
    }

    public List<ListModel> getListModels() {
        return mListModels;
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
