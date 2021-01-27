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
        setBtns();

        if (getIntent() != null) {
            getInfo();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void getInfo() {
        mDataRepo = new DataRepo.getData(new DataRepo.onDataListener() {
            @Override
            public void returnData(String data) {
                if (data != null && !data.isEmpty()) {
                    mProgressBar.setVisibility(View.GONE);
                    try {
                        JSONObject object = new JSONObject(data);
                        mNameTxt.setText(object.getString("OutboundShipmentNumber"));
                        JSONArray array = object.getJSONArray("tasks");
                        for (int i = 0; i < array.length(); i++) {
                            String code = array.getJSONObject(i).getString("code");
                            int sum = array.getJSONObject(i).getInt("sum");
                            if (code.equals("refill") && sum > 0) {
                                mRefillBtn.setText("Refill" + " (" + sum + ")");
                                mRefillBtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        getTask(getIntent().getStringExtra("id"),
                                                "refill",
                                                "1");
                                    }
                                });
                            } else if (code.equals("partial") && sum > 0) {
                                mPartialBtn.setText("Partial" + " (" + sum + ")");
                                mPartialBtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        getTask(getIntent().getStringExtra("id"),
                                                "partial",
                                                "2");
                                    }
                                });
                            } else if (code.equals("return") && sum > 0) {
                                mReturnBtn.setText("Return" + " (" + sum + ")");
                                mReturnBtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        getTask(getIntent().getStringExtra("id"),
                                                "return",
                                                "3");
                                    }
                                });
                            } else if (code.equals("staging") && sum > 0) {
                                mStagingBtn.setText("Staging" + " (" + sum + ")");
                                mStagingBtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        getTask(getIntent().getStringExtra("id"),
                                                "staging",
                                                "4");
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
        mDataRepo.getOutbound(getIntent().getStringExtra("id"));
        mDataRepo.start();
    }

    public void getTask(final String id, final String action, String filter) {
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
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
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

    public void setBtns() {
        mRefillBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(TasksActivity.this, "there are no tasks of this type", Toast.LENGTH_SHORT).show();
            }
        });

        mPartialBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(TasksActivity.this, "there are no tasks of this type", Toast.LENGTH_SHORT).show();
            }
        });

        mReturnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(TasksActivity.this, "there are no tasks of this type", Toast.LENGTH_SHORT).show();
            }
        });

        mStagingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(TasksActivity.this, "there are no tasks of this type", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 100) {
            setResult(100);
            finish();
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
