package com.test.test.Activities.Inbound;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.test.test.Adapters.PageAdapter;
import com.test.test.Fragments.DetailsFragment;
import com.test.test.Models.ExtraItemModel;
import com.test.test.Models.ItemModel;
import com.test.test.R;
import com.test.test.Repository.DataRepo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class DetailActivity extends AppCompatActivity {

    private DataRepo.getData mDataRepo, mGetData;
    private ExtraItemModel mExtraItemModel;

    private ViewPager mViewPager;
    private ProgressBar mProgressBar;
    private PageAdapter mAdapter;
    private HashMap<String, String> mResponseData;
    private DetailsFragment.setResponse mSetResponse;

    private HashMap<String, String> mCells = new HashMap<>();

    private TextView isn, supplier, client, items, status;

    private int mSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        isn = findViewById(R.id.isn_text);
        supplier = findViewById(R.id.supplier_text);
        client = findViewById(R.id.client_text);
        items = findViewById(R.id.items_text);
        status = findViewById(R.id.status_text);
        mProgressBar = findViewById(R.id.progress);

        mResponseData = new HashMap<>();
        mSetResponse = new DetailsFragment.setResponse() {
            @Override
            public void set(HashMap<String, String> response) {
                for (Map.Entry<String, String> entry : response.entrySet()) {
                    if (entry.getValue() == null || entry.getKey() == null) {
                        Toast.makeText(DetailActivity.this, "Empty data!", Toast.LENGTH_SHORT).show();
                    } else if (entry.getKey().isEmpty() || entry.getValue().isEmpty()) {
                        Toast.makeText(DetailActivity.this, "Empty data!", Toast.LENGTH_SHORT).show();
                    } else {
                        mResponseData.put(entry.getKey(), entry.getValue());
                    }
                }
                Toast.makeText(DetailActivity.this, "Saved", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void send(String action) {
                Log.d("asdasdaction", action);

                if (mResponseData == null || mResponseData.size() == 0) {
                    Toast.makeText(DetailActivity.this, "Empty data!", Toast.LENGTH_SHORT).show();
                } else {
                    if (action.equals("fact")) {
                        if (mResponseData.size() == mSize * 10) {
                            Toast.makeText(DetailActivity.this, "Ok", Toast.LENGTH_SHORT).show();
                            mDataRepo = new DataRepo.getData(new DataRepo.onDataListener() {
                                @Override
                                public void returnData(String data) {
                                }
                            });
                            mDataRepo.updateItem(mResponseData, getIntent().getStringExtra("list_id"), action);
                            mDataRepo.start();
                        } else {
                            Toast.makeText(DetailActivity.this, "Err", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        if (mResponseData.size() == mSize) {
                            Toast.makeText(DetailActivity.this, "Ok", Toast.LENGTH_SHORT).show();
                            mDataRepo = new DataRepo.getData(new DataRepo.onDataListener() {
                                @Override
                                public void returnData(String data) {
                                }
                            });
                            mDataRepo.updateItem(mResponseData, getIntent().getStringExtra("list_id"), action);
                            mDataRepo.start();
                        } else {
                            Toast.makeText(DetailActivity.this, "Err", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        };
        if (getIntent() != null) {
            mViewPager = findViewById(R.id.pager);
            mDataRepo = new DataRepo.getData(new DataRepo.onDataListener() {
                @Override
                public void returnData(String data) {
                    if (!data.isEmpty()) {
                        mProgressBar.setVisibility(View.GONE);
                        try {
                            JSONObject jsonResponse = new JSONObject(data);
                            setExtraData(jsonResponse);
                            setListData(jsonResponse.getJSONArray("Inbound_shipment"));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            mDataRepo.getListById(getIntent().getStringExtra("list_id"));
            mDataRepo.start();
        }
    }

    public void setListData(JSONArray array) {
        mSize = array.length();
        mAdapter = new PageAdapter(getSupportFragmentManager());
        for (int i = 0; i < array.length(); i++) {
            JSONObject varObj;
            try {
                varObj = array.getJSONObject(i);
                final DetailsFragment detailsFragment = new DetailsFragment(mSetResponse);
                final Bundle bundle = new Bundle();
                ItemModel model = new ItemModel(
                        varObj.getString("Initial_PRINTED_LPN"),
                        varObj.getString("Inbound_shipment_number"),
                        varObj.getString("item_article"),
                        varObj.getString("footprint_name"),
                        varObj.getString("inventory_status"),
                        varObj.getString("name"),
                        varObj.getString("shelf_life_days"),
                        varObj.getString("Implementation_period"),
                        varObj.getString("item_weight"),
                        varObj.getString("item_box"),
                        varObj.getString("id"),
                        varObj.getString("fact_item_weight"),
                        varObj.getString("fact_item_box"),
                        varObj.getString("fact_weight_empty_box"),
                        varObj.getString("fact_weight_empty_pallet"),
                        varObj.getString("production_date"),
                        varObj.getString("number_party"),
                        varObj.getString("item_id"),
                        varObj.getString("wrh_zone"),
                        varObj.getString("Manufacturing_Date"),
                        varObj.getString("inbound_date"),
                        varObj.getString("Lot_number_batch"),
                        varObj.getString("Transport_Equipment_Number"),
                        varObj.getString("Pallet_Type"),
                        varObj.getString("pallet_name"),
                        varObj.getString("plan_item_weight"),
                        varObj.getString("plan_item_box")
                );
                bundle.putSerializable("data", model);
                bundle.putInt("page", i);
                bundle.putString("status_id", mExtraItemModel.status);
                if (Objects.equals(getIntent().getStringExtra("status_id"), "2") && i == 0) {
                    getCellList(model.wrh_zone);
                }
                passData(i, array, bundle, detailsFragment);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Bundle bundle = new Bundle();
        bundle.putBoolean("last_frag", true);
        //fragments.get(fragments.size() - 1).setArguments(bundle);

        mViewPager.setAdapter(mAdapter);
    }

    void getCellList(String wrh_zone) {
        mGetData = new DataRepo.getData(new DataRepo.onDataListener() {
            @Override
            public void returnData(String data) {
                if (data != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(data);
                        JSONArray array = jsonObject.getJSONArray("rows");
                        for (int i = 0; i < array.length(); i++) {
                            jsonObject = array.getJSONObject(i);
                            mCells.put(jsonObject.getString("Location"), jsonObject.getString("id"));
                        }
                        if (mViewPager.getCurrentItem() == 0) {
                            mAdapter.updateFragments(mCells, 0);
                        }
                        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                            @Override
                            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                            }

                            @Override
                            public void onPageSelected(int position) {
                                mAdapter.updateFragments(mCells, position);
                            }

                            @Override
                            public void onPageScrollStateChanged(int state) {

                            }
                        });


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        mGetData.getCellsList(wrh_zone);
        mGetData.start();
    }

    void passData(int i, JSONArray array, Bundle bundle, DetailsFragment detailsFragment) {
        if (i == array.length() - 1) {
            bundle.putBoolean("last_frag", true);
        } else {
            bundle.putBoolean("last_frag", false);
        }
        detailsFragment.setArguments(bundle);
        mAdapter.addFragments(detailsFragment);
    }

    public void setExtraData(JSONObject jsonObject) {
        try {
            mExtraItemModel = new ExtraItemModel(
                    jsonObject.getString("Supplier"),
                    jsonObject.getString("Client"),
                    jsonObject.getString("Items"),
                    jsonObject.getString("Articles"),
                    jsonObject.getString("Inbound_shipment_number"),
                    jsonObject.getString("status_id")
            );
            isn.setText(mExtraItemModel.initial);
            client.setText(mExtraItemModel.client);
            supplier.setText(mExtraItemModel.supplier);
            items.setText(mExtraItemModel.items);
            //articles.setText(extraItemModel.articles);
            status.setText(mExtraItemModel.status);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        if (mGetData != null) {
            mGetData.release();
        }
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Are you sure?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        DetailActivity.super.onBackPressed();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                })
                .show();
    }
}
