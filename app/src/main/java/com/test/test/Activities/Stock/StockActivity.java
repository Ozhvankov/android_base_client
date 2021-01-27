package com.test.test.Activities.Stock;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.test.test.R;
import com.test.test.Repository.DataRepo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class StockActivity extends AppCompatActivity {

    private AutoCompleteTextView mLpnEdit;
    private Button mModifyBtn, mInventoryBtn;
    private ProgressBar mProgressBar;
    private DataRepo.getData mGetData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock);

        mLpnEdit = findViewById(R.id.stock_edit);
        mModifyBtn = findViewById(R.id.modify_btn);
        mInventoryBtn = findViewById(R.id.inventory_btn);
        mProgressBar = findViewById(R.id.progress);
        mProgressBar.setVisibility(View.VISIBLE);
        mGetData = new DataRepo.getData(new DataRepo.onDataListener() {
            @Override
            public void returnData(String data) {
                try {
                    JSONObject object = new JSONObject(data);
                    JSONArray array = object.getJSONArray("rows");
                    ArrayList<String> autocompleteList = new ArrayList<>();
                    for (int i = 0; i < array.length(); i++) {
                        autocompleteList.add(array.getJSONObject(i).getString("Initial_PRINTED_LPN"));
                    }
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(StockActivity.this,
                            android.R.layout.simple_dropdown_item_1line, autocompleteList);
                    mLpnEdit.setAdapter(arrayAdapter);
                    mProgressBar.setVisibility(View.GONE);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        mGetData.getStockLpnList();
        mGetData.start();

        mInventoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mGetData = new DataRepo.getData(new DataRepo.onDataListener() {
                    @Override
                    public void returnData(String data) {
                        if (!data.isEmpty()) {
                            try {
                                JSONObject object = new JSONObject(data);
                                if (object.has("total")) {
                                    if (object.getInt("total") == 0) {
                                        Toast.makeText(StockActivity.this, "No LPN", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Intent intent = new Intent(StockActivity.this, InventoryActivity.class);
                                        intent.putExtra("data", object.getJSONArray("rows").getJSONObject(
                                                0).toString());
                                        startActivity(intent);
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
                mGetData.getStock(mLpnEdit.getText().toString());
                mGetData.start();
            }
        });

        mModifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mGetData = new DataRepo.getData(new DataRepo.onDataListener() {
                    @Override
                    public void returnData(String data) {
                        if (!data.isEmpty()) {
                            try {
                                JSONObject object = new JSONObject(data);
                                if (object.has("total")) {
                                    if (object.getInt("total") == 0) {
                                        Toast.makeText(StockActivity.this, "No LPN", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Intent intent = new Intent(StockActivity.this, ModifyActivity.class);
                                        intent.putExtra("data", object.getJSONArray("rows").getJSONObject(
                                                0).toString());
                                        startActivity(intent);
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
                mGetData.getStock(mLpnEdit.getText().toString());
                mGetData.start();
            }
        });
    }
}
