package com.test.test.Activities.Outbound;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.test.test.R;
import com.test.test.Repository.DataRepo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class ReturnActivity extends AppCompatActivity {

    private TextView mOutboundNumberTxt, mTaskNameTxt,
            mItemIdTxt, mNameTaskTxt, mArticleTaskTxt;
    private AutoCompleteTextView mCellsAutocomplete;
    private ProgressBar mProgressBar;
    private Button mSaveBtn;
    private DataRepo.getData mDataRepo;
    private HashMap<String, String> mCellsMap;
    private String mId;
    private String mLpnId;
    private String mWrh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_return);
        mOutboundNumberTxt = findViewById(R.id.num_text);
        mTaskNameTxt = findViewById(R.id.task_text);
        mItemIdTxt = findViewById(R.id.item_id_text);
        mNameTaskTxt = findViewById(R.id.name_task_text);
        mArticleTaskTxt = findViewById(R.id.article_task_text);
        mSaveBtn = findViewById(R.id.save_btn);
        mCellsAutocomplete = findViewById(R.id.pallet_location_autocomplete);
        mProgressBar = findViewById(R.id.progress);
        Intent dataInent = getIntent();
        if (dataInent != null) {
            mId = dataInent.getStringExtra("id");
            String LPN = dataInent.getStringExtra("lpn");
            mTaskNameTxt.setText(dataInent.getStringExtra("action"));
            mOutboundNumberTxt.setText(LPN);
            mDataRepo = new DataRepo.getData(new DataRepo.onDataListener() {
                @Override
                public void returnData(String data) {
                    if (data != null && !data.isEmpty()) {
                        try {
                            JSONObject object = new JSONObject(data);
                            JSONObject infoObject = object.getJSONObject("info");
                            mLpnId = infoObject.getString("lpn_id");
                            mItemIdTxt.setText(infoObject.getString("item_article"));
                            mWrh = infoObject.getString("wrh_zone");
                            mArticleTaskTxt.setText(infoObject.getString("item_article"));
                            mNameTaskTxt.setText(infoObject.getString("name"));
                            JSONObject cellObject = infoObject.getJSONObject("cell");
                            mCellsAutocomplete.setText(cellObject.getString("name"));
                            setCellsAutocomplete();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            mDataRepo.getTask(mId);
            mDataRepo.start();
        }
    }

    void setCellsAutocomplete() {
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

                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                            ReturnActivity.this, android.R.layout.simple_dropdown_item_1line,
                            cellLocs);
                    mCellsAutocomplete.setAdapter(arrayAdapter);
                    mProgressBar.setVisibility(View.GONE);
                    mSaveBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            saveTask(mCellsMap.get(mCellsAutocomplete.getText().toString()),
                                    mLpnId);
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
        mDataRepo.getCellsList(mWrh);
        mDataRepo.start();
    }

    void saveTask(String cellId, String lpnId) {
        mDataRepo = new DataRepo.getData(new DataRepo.onDataListener() {
            @Override
            public void returnData(String data) {
                if (data != null && !data.isEmpty()) {
                    try {
                        JSONObject object = new JSONObject(data);
                        if (object.has("data")) {
                            if (object.getString("data").equals("success")) {
                                mSaveBtn.setText("Back");
                                mSaveBtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        setResult(100);
                                        finish();
                                    }
                                });
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                Toast.makeText(ReturnActivity.this, data, Toast.LENGTH_SHORT).show();
            }
        });
        mDataRepo.saveTask(mId, "3", cellId, lpnId);
        mDataRepo.start();
    }
}
