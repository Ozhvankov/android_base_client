package com.test.test.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;

import com.test.test.Activities.Inbound.ListActivity;
import com.test.test.Activities.MainActivity;
import com.test.test.Activities.Outbound.OutboundActivity;
import com.test.test.Activities.Stock.StockOperationActivity;
import com.test.test.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {

    private View mBaseView;
    private LinearLayout mInboundListLay, mOutboundListLay, mStockLay;
    private MainActivity.toolbarChangeListener mToolbarChangeListener;
    private ArrayAdapter<String> adapter;

    public MainFragment(MainActivity.toolbarChangeListener toolbarChangeListener) {
        setToolbarChangeListener(toolbarChangeListener);
    }

    public MainFragment() {

    }


    public void setToolbarChangeListener(MainActivity.toolbarChangeListener toolbarChangeListener){
        this.mToolbarChangeListener = toolbarChangeListener;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBaseView = inflater.inflate(R.layout.fragment_main, container, false);
        mInboundListLay = mBaseView.findViewById(R.id.inbound_container);
        mOutboundListLay = mBaseView.findViewById(R.id.outbound_container);
        mStockLay = mBaseView.findViewById(R.id.stock_container);

        mInboundListLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), ListActivity.class));
            }
        });

        mOutboundListLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), OutboundActivity.class));
            }
        });

        mStockLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), StockOperationActivity.class));
            }
        });

        return mBaseView;
    }
}
