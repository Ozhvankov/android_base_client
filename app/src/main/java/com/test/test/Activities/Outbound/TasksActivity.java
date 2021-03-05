package com.test.test.Activities.Outbound;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.test.test.R;
import com.test.test.Repository.DataRepo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TasksActivity extends AppCompatActivity {

    private Button mRefillBtn, mPartialBtn,
            mReturnBtn, mStagingBtn;
    private TextView mNameTxt;
    private ProgressBar mProgressBar;
    private DataRepo.getData mDataRepo;
    private int mId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);
        mRefillBtn = findViewById(R.id.refill_btn);
        mPartialBtn = findViewById(R.id.partial_btn);
        mReturnBtn = findViewById(R.id.return_btn);
        mStagingBtn = findViewById(R.id.staging_btn);
        mNameTxt = findViewById(R.id.task_name_text);
        mProgressBar = findViewById(R.id.progress);

        mRefillBtn.setEnabled(false);
        mPartialBtn.setEnabled(false);
        mReturnBtn.setEnabled(false);
        mStagingBtn.setEnabled(false);

        mId = getIntent().getIntExtra("id", -1);

        mRefillBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getTask(mId,
                        "refill",
                        "1");
            }
        });
        mPartialBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getTask(mId,
                        "partial",
                        "2");
            }
        });

        mReturnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getTask(mId,
                        "return",
                        "3");
            }
        });

        mStagingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getTask(mId,
                        "staging",
                        "4");
            }
        });

        load();



    }

    private void load() {
        mProgressBar.setVisibility(View.VISIBLE);
        mRefillBtn.setEnabled(false);
        mPartialBtn.setEnabled(false);
        mReturnBtn.setEnabled(false);
        mStagingBtn.setEnabled(false);
        mDataRepo = new DataRepo.getData(new DataRepo.onDataListener() {
            @Override
            public void returnData(String data) {
                mProgressBar.setVisibility(View.GONE);
                mRefillBtn.setEnabled(false);
                mRefillBtn.setText("Refill there are no tasks");
                mRefillBtn.setEnabled(false);
                mPartialBtn.setText("Partial there are no tasks");
                mReturnBtn.setEnabled(false);
                mReturnBtn.setText("Return there are no tasks");
                mStagingBtn.setEnabled(false);
                mStagingBtn.setText("Staging there are no tasks");
                if (data != null && !data.isEmpty()) {
                    JSONObject object = null;
                    try {
                        object = new JSONObject(data);
                        mNameTxt.setText(object.getString("OutboundShipmentNumber"));
                        JSONArray array = object.getJSONArray("tasks");
                        for (int i = 0; i < array.length(); i++) {
                            String code = array.getJSONObject(i).getString("code");
                            int sum = array.getJSONObject(i).getInt("sum");
                            if (code.equals("refill") && sum > 0) {
                                mRefillBtn.setEnabled(true);
                                mRefillBtn.setText("Refill" + " (" + sum + ")");
                            }
                            if (code.equals("partial") && sum > 0) {
                                mPartialBtn.setEnabled(true);
                                mPartialBtn.setText("Partial" + " (" + sum + ")");
                            }
                            if (code.equals("return") && sum > 0) {
                                mReturnBtn.setEnabled(true);
                                mReturnBtn.setText("Return" + " (" + sum + ")");
                            }
                            if (code.equals("staging") && sum > 0) {
                                mStagingBtn.setEnabled(true);
                                mStagingBtn.setText("Staging" + " (" + sum + ")");
                            }
                        }
                    } catch (JSONException e) {
                        Toast.makeText(TasksActivity.this, "Error parse tasks: " + e.toString(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(TasksActivity.this, "Empty tasks", Toast.LENGTH_SHORT).show();
                }
            }
        });
        mDataRepo.getOutbound(mId);
        mDataRepo.start();
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    public void getTask(final int id, final String action, String filter) {
        DataRepo.getData getData = new DataRepo.getData(new DataRepo.onDataListener() {
            @Override
            public void returnData(String data) {
                if (data != null && !data.isEmpty()) {
                    Log.d("asdasd", data);
                    try {
                        JSONObject object = new JSONObject(data);
                        JSONArray array = object.getJSONArray("tasks");
                        Intent intent = new Intent(TasksActivity.this, TaskActivity.class);
                        intent.putExtra("id", id);
                        intent.putExtra("action", action);
                        intent.putExtra("data", array.toString());
                        intent.putExtra("outbound", object.getString("OutboundShipmentNumber"));
                        startActivityForResult(intent, 100);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
        getData.getOutboundData(id, filter);
        getData.start();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100 && resultCode == 100) {
            load();
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
