package com.test.test.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.fxn.stash.Stash;
import com.test.test.Activities.Inbound.ListActivity;
import com.test.test.Activities.MainActivity;
import com.test.test.Activities.Outbound.OutboundActivity;
import com.test.test.Activities.Stock.StockActivity;
import com.test.test.Models.PalletType;
import com.test.test.R;
import com.test.test.Repository.DataRepo;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {

    private View mBaseView;
    private LinearLayout mInboundListLay, mOutboundListLay, mStockLay;
    private Spinner mWarehouseSpinner;
    private Button mApplyBtn;
    private MainActivity.toolbarChangeListener mToolbarChangeListener;
    private ArrayAdapter<String> adapter;

    public MainFragment(MainActivity.toolbarChangeListener toolbarChangeListener) {
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
        mWarehouseSpinner = mBaseView.findViewById(R.id.warehouse_spinner);
        mApplyBtn = mBaseView.findViewById(R.id.apply_btn);

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
                startActivity(new Intent(getContext(), StockActivity.class));
            }
        });

        setWarehouseSpinner(mWarehouseSpinner, getContext());
        mApplyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DataRepo.getData dataRepo = new DataRepo.getData(new DataRepo.onDataListener() {
                    @Override
                    public void returnData(String data) {
                        if (data != null && !data.isEmpty()) {
                            try {
                                JSONObject object = new JSONObject(data);
                                if (object.has("data")) {
                                    if (object.getString("data").equals("success")) {
                                        mToolbarChangeListener.onChange((String) mWarehouseSpinner.getSelectedItem());
                                        Stash.put("warehouse", (String) mWarehouseSpinner.getSelectedItem());
                                        Toast.makeText(getContext(), R.string.success, Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(getContext(), R.string.err, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                });
                dataRepo.setWarehouse(String.valueOf(mWarehouseSpinner.getSelectedItemPosition() + 1), Stash.getString("user_id"));
                dataRepo.start();
            }
        });

        return mBaseView;
    }

    private void setWarehouseSpinner(Spinner warehouseSpinner, Context context) {
        List<String> models = new ArrayList<>();
        models.add("Main ");
        models.add("Fish ");

        adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, models);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        warehouseSpinner.setAdapter(adapter);
        Log.d("asdasd234", String.valueOf(Stash.getString("warehouse")));
        Log.d("asdasd234", String.valueOf(models.indexOf(Stash.getString("warehouse"))));
        warehouseSpinner.setSelection(models.indexOf(Stash.getString("warehouse")));
    }
}
