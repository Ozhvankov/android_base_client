package com.test.test.Activities.Outbound;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bosphere.filelogger.FL;
import com.fxn.stash.Stash;
import com.test.test.R;
import com.test.test.Repository.DataRepo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OubboundRefillOrStagingActivity extends AppCompatActivity {

    private int Cell_type;

    private TextView mOutboundNumberTxt, mTaskNameTxt,
            mItemIdTxt, mNameTaskTxt, mArticleTaskTxt,
            mLocationTaskTxt, mKgTaskTxt, mBoxTaskTxt;
    private AutoCompleteTextView mCellsAutocomplete;
    private ProgressBar mProgressBar;
    private Button mSaveBtn;
    private DataRepo.getData mDataRepo;
    private HashMap<String, String> mCellsMap;
    private String mId;
    private String mLpnId;
    private String mAction;
    private String mLpnIdPartial;
    private AutoCompleteTextView mLpnAutocomplete;
    private TextView mNewLpnTxt;
    private TextView mNewLocationTxt;
    private HashMap<String, String> mLpnsMap;

    private boolean mFlagCellsLoaded = false;
    private boolean mFlagLpnLoaded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refill);
        mOutboundNumberTxt = findViewById(R.id.num_text);
        mTaskNameTxt = findViewById(R.id.task_text);
        mItemIdTxt = findViewById(R.id.item_id_text);
        mNameTaskTxt = findViewById(R.id.name_task_text);
        mKgTaskTxt = findViewById(R.id.kg_task_text);
        mBoxTaskTxt = findViewById(R.id.box_task_text);
        mArticleTaskTxt = findViewById(R.id.article_task_text);
        mSaveBtn = findViewById(R.id.save_btn);
        mLocationTaskTxt = findViewById(R.id.location_task_text);
        mCellsAutocomplete = findViewById(R.id.pallet_location_autocomplete);
        mProgressBar = findViewById(R.id.progress);
        mLpnAutocomplete = findViewById(R.id.lpn_Autocomplete);
        mNewLpnTxt = findViewById(R.id.new_lpn);
        mNewLocationTxt = findViewById(R.id.new_location);
        mCellsAutocomplete.setEnabled(false);
        Intent dataInent = getIntent();
        mAction = dataInent.getStringExtra("action");
        switch (mAction) {
            case "partial":
            case "refill":
                Cell_type = 3;
                break;
            case "staging":
                Cell_type = 1;
                break;
            case "return":
                Cell_type = 2;
                break;
        }
        mLpnAutocomplete.setVisibility(View.GONE);
        mNewLpnTxt.setVisibility(View.GONE);
        mId = dataInent.getStringExtra("id");
        String LPN = dataInent.getStringExtra("lpn");
        mTaskNameTxt.setText(mAction);
        mItemIdTxt.setText(mId);
        mOutboundNumberTxt.setText(LPN);
        mSaveBtn.setEnabled(false);
        mSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String l;
                if(mAction.equals("partial")){
                    l = mLpnsMap.get(mLpnAutocomplete.getText().toString());
                } else {
                    l = mLpnIdPartial;
                }
                saveTask(mCellsMap.get(mCellsAutocomplete.getText().toString()),
                                mLpnId, l);
            }
        });

        mDataRepo = new DataRepo.getData(new DataRepo.onDataListener() {
            @Override
            public void returnData(String data) {
                if (data != null && !data.isEmpty()) {
                    try {
                        JSONObject object = new JSONObject(data);
                        JSONObject infoObject = object.getJSONObject("info");
                        mLpnId = infoObject.getString("lpn_id");
                        int WrhZone = infoObject.getInt("wrh_zone");
                        int Pallet_Type = infoObject.getInt("Pallet_Type");
                        int fact_item_weight = infoObject.getInt("fact_item_weight");
                        mItemIdTxt.setText(infoObject.getString("item_article"));
                        mArticleTaskTxt.setText(infoObject.getString("item_no"));
                        mNameTaskTxt.setText(infoObject.getString("name"));
                        mLpnIdPartial = infoObject.getString("lpn_id_partial");
                        mKgTaskTxt.setText(infoObject.getString("kg"));
                        mBoxTaskTxt.setText(infoObject.getString("box"));
                        JSONObject cellObject = infoObject.getJSONObject("cell");
                        mLocationTaskTxt.setText(cellObject.getString("name"));
                        switch (mAction) {
                            case "refill":
                            case "staging":
                            case "return":
                                setCellsAutocomplete(1, WrhZone, Stash.getInt("warehouse_id"), Cell_type, Pallet_Type, fact_item_weight, false);
                                break;
                            case "partial":
                                setCellsAutocomplete(1, WrhZone, Stash.getInt("warehouse_id"), Cell_type, Pallet_Type, fact_item_weight, true);
                                setLPNAutocomplete();
                                break;
                        }
                    } catch (JSONException e) {
                        Toast.makeText(OubboundRefillOrStagingActivity.this, "Wrong parse info " + e.toString(), Toast.LENGTH_SHORT).show();
                        if(Stash.getBoolean("logger")) {
                            FL.d(e.toString());
                        }
                    }
                }
            }
        });
        mDataRepo.getTask(mId);
        mDataRepo.start();
    }

    void setLPNAutocomplete() {
        mDataRepo = new DataRepo.getData(new DataRepo.onDataListener() {
            @Override
            public void returnData(String data) {
                if (data != null && !data.isEmpty()) {
                    mLpnsMap = new HashMap<>();
                    try {
                        JSONObject object = new JSONObject(data);
                        JSONArray array = object.getJSONArray("rows");
                        for (int i = 0; i < array.length(); i++) {
                            mLpnsMap.put(array.getJSONObject(i).getString("name"),
                                    String.valueOf(array.getJSONObject(i).getInt("id")));
                        }
                        List<String> lpnList = new ArrayList<>(mLpnsMap.keySet());
                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                                OubboundRefillOrStagingActivity.this,
                                android.R.layout.simple_dropdown_item_1line, lpnList);
                        mLpnAutocomplete.setAdapter(arrayAdapter);
                        mLpnAutocomplete.setVisibility(View.VISIBLE);
                        mNewLpnTxt.setVisibility(View.VISIBLE);
                        if(lpnList.size() == 0){
                            Toast.makeText(OubboundRefillOrStagingActivity.this, "Lpn list is empty", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        mFlagLpnLoaded = true;
                        mLpnAutocomplete.setSelection(0);
                        if(mFlagCellsLoaded && mFlagLpnLoaded) {
                            mProgressBar.setVisibility(View.GONE);
                            mSaveBtn.setEnabled(true);
                        }
                    } catch (JSONException e) {
                        Toast.makeText(OubboundRefillOrStagingActivity.this, "Wrong parse lpn list " + e.toString(), Toast.LENGTH_SHORT).show();
                        if(Stash.getBoolean("logger")) {
                            FL.d("Wrong parse lpn list " + e.toString());
                        }
                        mProgressBar.setVisibility(View.GONE);
                    }
                } else {
                    if(Stash.getBoolean("logger")) {
                        FL.d("data from server wrong ");
                    }
                    Toast.makeText(OubboundRefillOrStagingActivity.this, "data from server wrong ", Toast.LENGTH_SHORT).show();
                    mProgressBar.setVisibility(View.GONE);
                }
            }
        });
        mDataRepo.getLpnList();
        mDataRepo.start();
    }

    void setCellsAutocomplete(int Status, int WrhZone, int Warehouse, int Cell_Type, int pallet_type, int Max_Weight_kg, final boolean flag_control) {
        mDataRepo = new DataRepo.getData(new DataRepo.onDataListener() {
            @Override
            public void returnData(String data) {
                JSONObject jsonObject1;
                try {
                    jsonObject1 = new JSONObject(data);
                    JSONArray array = jsonObject1.getJSONArray("rows");
                    List<String> cellLocs;
                    mCellsMap = new HashMap<>();
                    HashMap<String, String> celldata = new HashMap<>();
                    for (int i = 0; i < array.length(); i++) {
                        jsonObject1 = array.getJSONObject(i);
                        celldata.put(jsonObject1.getString("id"), jsonObject1.getString("Location"));
                        mCellsMap.put(jsonObject1.getString("Location"), jsonObject1.getString("id"));
                    }
                    cellLocs = new ArrayList<>(mCellsMap.keySet());

                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(OubboundRefillOrStagingActivity.this,
                            android.R.layout.simple_dropdown_item_1line, cellLocs);
                    mCellsAutocomplete.setAdapter(arrayAdapter);
                    if(cellLocs.size() == 0){
                        Toast.makeText(OubboundRefillOrStagingActivity.this, "Cells list is empty", Toast.LENGTH_SHORT).show();
                        mProgressBar.setVisibility(View.GONE);
                        mSaveBtn.setEnabled(false);
                        return;
                    }
                    mCellsAutocomplete.setEnabled(true);
                    mFlagCellsLoaded = true;
                    if (flag_control) {
                        if (mFlagCellsLoaded && mFlagLpnLoaded) {
                            mProgressBar.setVisibility(View.GONE);
                            mSaveBtn.setEnabled(true);
                        }
                    } else {
                        mProgressBar.setVisibility(View.GONE);
                        mSaveBtn.setEnabled(true);
                    }
                } catch (JSONException e) {
                    mProgressBar.setVisibility(View.GONE);
                    mSaveBtn.setEnabled(false);
                    Toast.makeText(OubboundRefillOrStagingActivity.this, "Wrong parse cells list " + e.toString(), Toast.LENGTH_SHORT).show();
                    if(Stash.getBoolean("logger")) {
                        FL.d(e.toString());
                    }
                }
            }
        });

        mDataRepo.getCells(Status, WrhZone, Warehouse, Cell_Type, pallet_type, Max_Weight_kg);
        mDataRepo.start();
    }

    void saveTask(final String cellId, final String lpnId, String lpn_id_partial) {
        mDataRepo = new DataRepo.getData(new DataRepo.onDataListener() {
            @Override
            public void returnData(final String data) {
                if (data != null && !data.isEmpty()) {
                    try {
                        JSONObject object = new JSONObject(data);
                        if (object.has("data")) {
                            if (object.getString("data").equals("success")) {
                                setResult(100);
                                finish();
                                Toast.makeText(OubboundRefillOrStagingActivity.this, "Saved: " + data, Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                    } catch (JSONException e) {
                        if(Stash.getBoolean("logger")) {
                            FL.d("Wrong parse: cellId=" + cellId + " lpnId=" + lpnId);
                        }
                        Toast.makeText(OubboundRefillOrStagingActivity.this, "Wrong parse: cellId=" + cellId + " lpnId=" + lpnId, Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
                if(Stash.getBoolean("logger")) {
                    FL.d("Wrong save: cellId=" + cellId + " lpnId=" + lpnId);
                }
                Toast.makeText(OubboundRefillOrStagingActivity.this, "Wrong save: cellId=" + cellId + " lpnId=" + lpnId, Toast.LENGTH_SHORT).show();
            }
        });
        String taskType;
        if(cellId.length() == 0 ) {
            Toast.makeText(OubboundRefillOrStagingActivity.this, "New location no set", Toast.LENGTH_SHORT).show();
        } else {
            switch (mAction) {
                case "partial"://Cell_type = 3;
                    if (lpn_id_partial.length() == 0) {
                        Toast.makeText(OubboundRefillOrStagingActivity.this, "New Lpn no set", Toast.LENGTH_SHORT).show();
                        return;
                    } else
                        mDataRepo.savePartialTask(mId, cellId, lpnId, lpn_id_partial);
                    break;
                case "refill"://Cell_type = 3;
                    mDataRepo.saveRefillTask(mId, cellId, lpnId);
                    break;
                case "staging"://Cell_type = 1;
                    mDataRepo.saveStagingTask(mId, cellId, lpnId, lpn_id_partial);
                    break;
                case "return"://Cell_type = 2;
                    mDataRepo.saveReturnTask(mId, cellId, lpnId);
                    break;
            }
            mDataRepo.start();
        }
    }
}
