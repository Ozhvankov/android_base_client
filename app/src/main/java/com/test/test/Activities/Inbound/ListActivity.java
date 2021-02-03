package com.test.test.Activities.Inbound;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.google.gson.JsonArray;
import com.google.gson.annotations.JsonAdapter;
import com.test.test.Adapters.ListAdapter;
import com.test.test.Models.ListModel;
import com.test.test.R;
import com.test.test.Repository.DataRepo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {
    private ProgressBar mProgressBar;
    private Button mPlanFactBtn, mPutAwayBtn;
    private List<ListModel> mListModels;
    private List<ListModel> filter;
    private RecyclerView mRecyclerView;
    private ListAdapter mListAdapter;
    private int mPages = 0;
    int mPage = 0;

    DataRepo.getData getData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        mListModels = new ArrayList<>();
        mProgressBar = findViewById(R.id.progress);
        mRecyclerView = findViewById(R.id.list);
        mPlanFactBtn = findViewById(R.id.planFactBtn);
        mPutAwayBtn = findViewById(R.id.putAwayBtn);

        mPlanFactBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mProgressBar.setVisibility(View.VISIBLE);
                getData = new DataRepo.getData(new DataRepo.onDataListener() {
                    @Override
                    public void returnData(String data) {
                        mListModels.clear();
                        setList(data);
                        mRecyclerView.setOnScrollListener(null);
                        setBackgroundColorButton(mPlanFactBtn, true);
                        setBackgroundColorButton(mPutAwayBtn, false);
                    }
                });
                getData.getListByStatus(ListModel.STATUS_INPUT_FACT);
                getData.start();

            }
        });


        mPutAwayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mProgressBar.setVisibility(View.VISIBLE);
                getData = new DataRepo.getData(new DataRepo.onDataListener() {
                    @Override
                    public void returnData(String data) {
                        mListModels.clear();
                        setList(data);
                        mRecyclerView.setOnScrollListener(null);
                        setBackgroundColorButton(mPlanFactBtn, false);
                        setBackgroundColorButton(mPutAwayBtn, true);
                    }
                });
                getData.getListByStatus(ListModel.STATUS_PUT_AWAY);
                getData.start();
            }
        });

        getList();
        setBackgroundColorButton(mPlanFactBtn, true);
        setBackgroundColorButton(mPutAwayBtn, false);

        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (layoutManager != null
                        && layoutManager.findLastCompletelyVisibleItemPosition() == mListModels.size() - 1) {
                    if (mPage < mPages) {
                        mPage = mPage + 1;
                        mProgressBar.setVisibility(View.VISIBLE);
                        getData = new DataRepo.getData(new DataRepo.onDataListener() {
                            @Override
                            public void returnData(String data) {
                                setList(data);
                            }
                        });
                        getData.getList(mPage);
                        getData.start();
                    }

                }
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
    }
    private void setBackgroundColorButton(Button btn, boolean selected){
            if(selected) {
                btn.getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
            } else {
                btn.getBackground().setColorFilter(Color.LTGRAY, PorterDuff.Mode.MULTIPLY);
            }
    }

    public void setList(String data) {
        if (!data.isEmpty()) {
            mProgressBar.setVisibility(View.GONE);
            try {
                JSONObject jsonObject = new JSONObject(data);
                mPages = jsonObject.getInt("total") / 10;

                JSONArray array = jsonObject.getJSONArray("rows");
                for (int i = 0; i < array.length(); i++) {
                    JSONObject o = array.getJSONObject(i);
                    mListModels.add(new ListModel(
                            o.getInt("id"),
                            o.getString("Inbound_shipment_number"),
                            o.getInt("Items_amount"),
                            o.getInt("Gate"),
                            o.getString("Item_articles"),
                            o.getString("updated_at"),
                            o.getString("Supplier"),
                            o.getInt("Client"),
                            o.getString("Warehouse"),
                            o.getString("created_at"),
                            o.getInt("status_id")));
                }
                mListAdapter = new ListAdapter(ListActivity.this, mListModels);
                mRecyclerView.setLayoutManager(new LinearLayoutManager(ListActivity.this));
                mRecyclerView.setAdapter(mListAdapter);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    public void getList() {
        getData = new DataRepo.getData(new DataRepo.onDataListener() {
            @Override
            public void returnData(String data) {
                setList(data);
            }
        });
        getData.getList();
        getData.start();

    }
}
