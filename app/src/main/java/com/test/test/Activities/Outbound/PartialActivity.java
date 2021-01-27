package com.test.test.Activities.Outbound;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.test.test.R;
import com.test.test.Repository.DataRepo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class PartialActivity extends AppCompatActivity {

    private TextView mOutboundNumberTxt, mTaskNameTxt,
            mItemIdTxt, mNameTaskTxt, mArticleTaskTxt,
            mBoxTask, mKgTask, mLPNTask, mLocationTask;
    private AutoCompleteTextView mLPNAutocomplete;
    private ProgressBar mProgressBar;
    private DataRepo.getData mDataRepo;
    private HashMap<String, String> mLpnsMap;
    private Button mSaveBtn;

    private String mLpnId;
    private String mCellId;
    private String mItemId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partial);
        mOutboundNumberTxt = findViewById(R.id.num_text);
        mTaskNameTxt = findViewById(R.id.task_text);
        mItemIdTxt = findViewById(R.id.item_id_text);
        mNameTaskTxt = findViewById(R.id.name_task_text);
        mArticleTaskTxt = findViewById(R.id.article_task_text);
        mBoxTask = findViewById(R.id.box_task_text);
        mKgTask = findViewById(R.id.kg_task_text);
        mLPNTask = findViewById(R.id.lpnTask);
        mLocationTask = findViewById(R.id.location_task_text);
        mSaveBtn = findViewById(R.id.save_btn);
        mLPNAutocomplete = findViewById(R.id.Lpn_Autocomplete);
        mProgressBar = findViewById(R.id.progress);
        Intent dataIntent = getIntent();
        if (dataIntent != null) {
            mItemId = dataIntent.getStringExtra("id");
            final String LPN = dataIntent.getStringExtra("lpn");
            mTaskNameTxt.setText(dataIntent.getStringExtra("action"));
            mOutboundNumberTxt.setText(LPN);
            mDataRepo = new DataRepo.getData(new DataRepo.onDataListener() {
                @Override
                public void returnData(String data) {
                    if (data != null && !data.isEmpty()) {
                        try {
                            JSONObject object = new JSONObject(data);
                            JSONObject infoObject = object.getJSONObject("info");
                            mLpnId = infoObject.getString("lpn_id");
                            mArticleTaskTxt.setText(infoObject.getString("item_article"));
                            mNameTaskTxt.setText(infoObject.getString("name"));
                            mItemIdTxt.setText(infoObject.getString("item_article"));
                            mBoxTask.setText(infoObject.getString("box"));
                            mKgTask.setText(infoObject.getString("kg"));
                            JSONObject cellObject = infoObject.getJSONObject("cell");
                            mCellId = cellObject.getString("id");
                            mLocationTask.setText(cellObject.getString("name"));
                            mLPNTask.setText(LPN);
                            //LPNAutocomplete.setText(LPN);
                            setLPNAutocomplete();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            mDataRepo.getTask(mItemId);
            mDataRepo.start();
        }
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
                            mLpnsMap.put(array.getJSONObject(i).getString("Initial_PRINTED_LPN"),
                                    array.getJSONObject(i).getString("id"));
                        }
                        List<String> lpnList = new ArrayList<>(mLpnsMap.keySet());
                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                                PartialActivity.this,
                                android.R.layout.simple_dropdown_item_1line, lpnList);
                        mLPNAutocomplete.setAdapter(arrayAdapter);
                        mProgressBar.setVisibility(View.GONE);
                        mSaveBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                save(mCellId, mLpnId, mLpnsMap.get(mLPNAutocomplete.getText().toString()));
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        mDataRepo.getLpnList();
        mDataRepo.start();
    }

    void save(String cellId, String lpnId, String lpn_id_partial) {
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
            }
        });
        mDataRepo.saveTask(mItemId, "2", cellId, lpnId, lpn_id_partial);
        mDataRepo.start();
    }
}
