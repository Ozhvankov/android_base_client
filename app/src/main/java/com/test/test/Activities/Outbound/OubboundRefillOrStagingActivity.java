package com.test.test.Activities.Outbound;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
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
    private String mLpnIdPartial;
    private AutoCompleteTextView mLpnAutocomplete;
    private TextView mNewLpnTxt;
    private TextView mNewLocationTxt;
    private HashMap<String, String> mLpnsMap;
    private String mCode;
    private TextView mFactBoxHeaderTxt;
    private Spinner mFactBoxSpinner;
    private TextView mFactBoxCalculate;
    private int box;
    private double kg;

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
        mFactBoxHeaderTxt = findViewById(R.id.fact_box_header);
        mFactBoxSpinner = (Spinner)findViewById(R.id.spinner_fact_box);
        mFactBoxCalculate = findViewById(R.id.fact_box_calculate);
        mFactBoxSpinner.setVisibility(View.GONE);

        mFactBoxHeaderTxt.setVisibility(View.GONE);
        mFactBoxCalculate.setVisibility(View.GONE);
        mCellsAutocomplete.setEnabled(false);
        Intent dataInent = getIntent();
        mCode = dataInent.getStringExtra("code");
        mLpnAutocomplete.setVisibility(View.GONE);
        mNewLpnTxt.setVisibility(View.GONE);
        mId = dataInent.getStringExtra("id");
        String LPN = dataInent.getStringExtra("lpn");
        mTaskNameTxt.setText("Task: " + dataInent.getStringExtra("name"));
        mItemIdTxt.setText(mId);
        mOutboundNumberTxt.setText(LPN);
        mSaveBtn.setEnabled(false);
        mSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String l;
                if (mCode.equals("partial")) {
                    l = mLpnsMap.get(mLpnAutocomplete.getText().toString());
                    if(l == null) {
                        Toast.makeText(OubboundRefillOrStagingActivity.this, "Lpn not set!", Toast.LENGTH_SHORT).show();
                    }
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
                        mKgTaskTxt.setText(infoObject.getString("kg_current"));
                        mBoxTaskTxt.setText(infoObject.getString("box_current"));
                        JSONObject cellObject = infoObject.getJSONObject("cell");
                        mLocationTaskTxt.setText(cellObject.getString("name"));
                        JSONArray cells_list = infoObject.getJSONArray("cells_list");
                        if(cells_list.length() > 0) {
                            List<String> cellLocs;
                            mCellsMap = new HashMap<>();
                            HashMap<String, String> celldata = new HashMap<>();
                            for (int i = 0; i < cells_list.length(); i++) {
                                JSONArray a = cells_list.getJSONArray(i);
                                celldata.put(String.valueOf(a.getInt(0)), a.getString(1));
                                mCellsMap.put(a.getString(1), String.valueOf(a.getInt(0)));
                            }
                            cellLocs = new ArrayList<>(mCellsMap.keySet());

                            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(OubboundRefillOrStagingActivity.this,
                                    android.R.layout.simple_dropdown_item_1line, cellLocs);
                            mCellsAutocomplete.setAdapter(arrayAdapter);
                            mCellsAutocomplete.setEnabled(true);
                        } else {
                            Toast.makeText(OubboundRefillOrStagingActivity.this, "Cells list is empty", Toast.LENGTH_LONG).show();
                            mProgressBar.setVisibility(View.GONE);
                            mSaveBtn.setEnabled(false);
                            mCellsAutocomplete.setEnabled(false);
                            if (Stash.getBoolean("logger")) {
                                FL.d("Cells list is empty");
                            }
                            return;
                        }
                        JSONArray lpns_list = infoObject.getJSONArray("lpns_list");
                        mLpnsMap = new HashMap<>();
                        if(lpns_list.length() > 0) {
                            for (int i = 0; i < lpns_list.length(); i++) {
                                JSONArray a = lpns_list.getJSONArray(i);
                                mLpnsMap.put(a.getString(1),String.valueOf(a.getInt(0)));
                            }
                            List<String> lpnList = new ArrayList<>(mLpnsMap.keySet());
                            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                                    OubboundRefillOrStagingActivity.this,
                                    android.R.layout.simple_dropdown_item_1line, lpnList);
                            mLpnAutocomplete.setAdapter(arrayAdapter);
                            mLpnAutocomplete.setVisibility(View.VISIBLE);
                            mNewLpnTxt.setVisibility(View.VISIBLE);
                        } else {
                            if(mCode.equals("partial")) {
                                mProgressBar.setVisibility(View.GONE);
                                mSaveBtn.setEnabled(false);
                                Toast.makeText(OubboundRefillOrStagingActivity.this, "Lpn list is empty", Toast.LENGTH_SHORT).show();
                                if (Stash.getBoolean("logger")) {
                                    FL.d("Lpn list is empty");
                                }
                                return;
                            }
                        }
                        if(mCode.equals("partial")) {
                            mFactBoxSpinner.setVisibility(View.VISIBLE);
                            mFactBoxHeaderTxt.setVisibility(View.VISIBLE);
                            mFactBoxCalculate.setVisibility(View.VISIBLE);
                            kg = Double.parseDouble(infoObject.getString("kg"));
                            mFactBoxCalculate.setText(""+kg);
                            box = infoObject.getInt("box");
                            int boxAvailable = infoObject.getInt("box_avaible");
                            ArrayList<String> d = new ArrayList<>();
                            for(int i1 = 1, cnt1 = box + boxAvailable; i1 <= cnt1; i1++) {
                                d.add(""+i1);
                            }
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(OubboundRefillOrStagingActivity.this, android.R.layout.simple_spinner_item, d);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            mFactBoxSpinner.setAdapter(adapter);
                            mFactBoxSpinner.setSelection(box-1);
                            mFactBoxSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                    int v = Integer.parseInt((String)adapterView.getAdapter().getItem(i));
                                        mFactBoxCalculate.setText(String.format("%.3f",+v*kg/box));
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {

                                }
                            });
                        }
                        mProgressBar.setVisibility(View.GONE);
                        mSaveBtn.setEnabled(true);
                    } catch (JSONException e) {
                        Toast.makeText(OubboundRefillOrStagingActivity.this, "Wrong parse info " + e.toString(), Toast.LENGTH_SHORT).show();
                        if (Stash.getBoolean("logger")) {
                            FL.d(e.toString());
                        }
                    }
                }
            }
        });
        mDataRepo.getTask(mId);
        mDataRepo.start();
    }


    void saveTask(final String cellId, final String lpnId, String lpn_id_partial) {
        if(cellId == null) {
            Toast.makeText(OubboundRefillOrStagingActivity.this, "Not select enable cell!", Toast.LENGTH_SHORT).show();
            return;
        }
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
                        if (Stash.getBoolean("logger")) {
                            FL.d("Wrong parse: cellId=" + cellId + " lpnId=" + lpnId);
                        }
                        Toast.makeText(OubboundRefillOrStagingActivity.this, "Wrong parse: cellId=" + cellId + " lpnId=" + lpnId, Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
                if (Stash.getBoolean("logger")) {
                    FL.d("Wrong save: cellId=" + cellId + " lpnId=" + lpnId);
                }
                Toast.makeText(OubboundRefillOrStagingActivity.this, "Wrong save: cellId=" + cellId + " lpnId=" + lpnId, Toast.LENGTH_SHORT).show();
            }
        });
        String taskType;
        if (cellId.length() == 0) {
            Toast.makeText(OubboundRefillOrStagingActivity.this, "New location no set", Toast.LENGTH_SHORT).show();
        } else {
            if (mCode.equals("partial")) {
                String factBox = (String)mFactBoxSpinner.getSelectedItem();
                if (lpn_id_partial.length() == 0) {
                    Toast.makeText(OubboundRefillOrStagingActivity.this, "New Lpn no set", Toast.LENGTH_SHORT).show();
                    return;
                } else if(Integer.valueOf(factBox)==0){
                    Toast.makeText(OubboundRefillOrStagingActivity.this, "Fact box no set", Toast.LENGTH_SHORT).show();
                    return;
                } else
                    mDataRepo.savePartialTask(mId, cellId, lpnId, lpn_id_partial, factBox);
            } else if (mCode.equals("refill")) {
                mDataRepo.saveRefillTask(mId, cellId, lpnId);
            } else if (mCode.equals("staging")) {
                mDataRepo.saveStagingTask(mId, cellId, lpnId, lpn_id_partial);
            } else if (mCode.equals("return")) {
                mDataRepo.saveReturnTask(mId, cellId, lpnId);
            }
        }
        mDataRepo.start();
    }
}
