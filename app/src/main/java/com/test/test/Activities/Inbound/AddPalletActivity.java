package com.test.test.Activities.Inbound;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.test.test.Adapters.FootprintsrAdapter;
import com.test.test.Adapters.ItemsAdapter;
import com.test.test.Adapters.LotsAdapter;
import com.test.test.Adapters.UnitsAdapter;
import com.test.test.Models.Footprint;
import com.test.test.Models.Item;
import com.test.test.Models.Item_shipment_unit_type;
import com.test.test.Models.Lot;
import com.test.test.R;
import com.test.test.Repository.DataRepo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AddPalletActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private ArrayList<Item> mItems = new ArrayList<Item>();
    private ArrayList<Lot> mLots = new ArrayList<Lot>();
    private ArrayList<Footprint> mFootprints = new ArrayList<Footprint>();
    private ArrayList<Item_shipment_unit_type> mItemShipmentUnitType = new ArrayList<Item_shipment_unit_type>();

    private EditText packaging_wight_edit;
    private EditText pallet_wight_edit;
    private Spinner spinner_items;
    private Spinner spinner_lots;
    private Spinner spinner_footprint;
    private Spinner spinner_unit_id;

    private ProgressBar mProgressBar;
    private Button mAddPalletBtn;
    private DataRepo.getData mDataRepo;
    private FootprintsrAdapter mFootprintAdapter;
    private UnitsAdapter mUnitsAdapter;
    private String mListId;
    private EditText mGrossEdit;
    private EditText mPalletWight;
    private EditText mPackagingWight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pallet);
        packaging_wight_edit = findViewById(R.id.packaging_wight_edit) ;
        pallet_wight_edit = findViewById(R.id.pallet_wight_edit);
        spinner_items = findViewById(R.id.spinner_items);
        spinner_footprint = findViewById(R.id.spinner_footprint);
        spinner_unit_id = findViewById(R.id.spinner_unit_id);
        mItems = getIntent().getParcelableArrayListExtra("items");
        ItemsAdapter adapter = new ItemsAdapter(this, -1 , mItems);
        spinner_items.setAdapter(adapter);
        spinner_items.setOnItemSelectedListener(this);
        mLots = getIntent().getParcelableArrayListExtra("lots");
        mListId = String.valueOf(getIntent().getIntExtra("list_id",-1));
        spinner_lots = findViewById(R.id.spinner_lots);
        LotsAdapter lotAdapter = new LotsAdapter(this, -1 , mLots);
        spinner_lots.setAdapter(lotAdapter);
        mFootprintAdapter = new FootprintsrAdapter(this, -1 , mFootprints);
        spinner_footprint.setAdapter(mFootprintAdapter);
        mUnitsAdapter = new UnitsAdapter(this, -1, mItemShipmentUnitType);
        spinner_unit_id.setAdapter(mUnitsAdapter);

        mGrossEdit = (EditText)findViewById(R.id.gross_edit);
        mPalletWight = (EditText)findViewById(R.id.pallet_wight_edit);
        mPackagingWight = (EditText)findViewById(R.id.packaging_wight_edit);

        mProgressBar = findViewById(R.id.progress2);
        mAddPalletBtn = findViewById(R.id.add_btn);
        mAddPalletBtn.setEnabled(false);
        mAddPalletBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAddPalletBtn.setEnabled(false);
                mDataRepo = new DataRepo.getData(new DataRepo.onDataListener() {
                    @Override
                    public void returnData(String data) {
                        mProgressBar.setVisibility(View.GONE);
                        if(data != null) {
                            if (!data.isEmpty()) {
                                if (data.equals("{\"data\":\"success\"}")) {
                                    setResult(0);
                                    finish();
                                    return;
                                }
                            } else {
                                Toast.makeText(AddPalletActivity.this, "Error added pallete:" + data, Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(AddPalletActivity.this, "Error added pallete!", Toast.LENGTH_SHORT).show();
                        }
                        mAddPalletBtn.setEnabled(true);
                    }
                });
                String item_id = String.valueOf(spinner_items.getSelectedItemId());
                String footprint_id = String.valueOf(spinner_footprint.getSelectedItemId());
                String unit_id = String.valueOf(spinner_unit_id.getSelectedItemId());
                String lot_id = String.valueOf(spinner_lots.getSelectedItemId());
                if(mGrossEdit.getText().toString().length() == 0) {
                    Toast.makeText(AddPalletActivity.this, "mGrossEdit is empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(mPalletWight.getText().toString().length() == 0) {
                    Toast.makeText(AddPalletActivity.this, "mPalletWight is empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(mPackagingWight.getText().toString().length() == 0) {
                    Toast.makeText(AddPalletActivity.this, "mPackagingWight is empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                mProgressBar.setVisibility(View.VISIBLE);
                String gross = mGrossEdit.getText().toString();
                String pallet_wight = mPalletWight.getText().toString();
                String packaging_wight = mPackagingWight.getText().toString();
                mDataRepo.addPallete(item_id, footprint_id, unit_id, gross, pallet_wight, packaging_wight, mListId, lot_id);
                mDataRepo.start();
            }
        });
        mDataRepo = new DataRepo.getData(new DataRepo.onDataListener() {
            @Override
            public void returnData(String data) {
                if (!data.isEmpty()) {
                    mProgressBar.setVisibility(View.GONE);
                    try {
                        JSONObject jsonResponse = new JSONObject(data);
                        JSONArray array = jsonResponse.getJSONArray("rows");
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject varObj = array.getJSONObject(i);
                            Item_shipment_unit_type item = new Item_shipment_unit_type(varObj.getInt("id"), varObj.getString("Item_shipment_unit_type"));
                            mItemShipmentUnitType.add(item);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        return;
                    }
                    mUnitsAdapter.notifyDataSetChanged();
                    mAddPalletBtn.setEnabled(true);
                }
            }
        });
        mDataRepo.getItemShipmentUnitType();
        mDataRepo.start();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        mProgressBar.setVisibility(View.VISIBLE);
        mDataRepo = new DataRepo.getData(new DataRepo.onDataListener() {
            @Override
            public void returnData(String data) {
                if (!data.isEmpty()) {
                    mProgressBar.setVisibility(View.GONE);
                    try {
                        mFootprints.clear();
                        JSONObject jsonResponse = new JSONObject(data);
                        JSONArray array = jsonResponse.getJSONArray("rows");
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject varObj = array.getJSONObject(i);
                            Footprint footprint = parseVarObj(varObj);
                            if(footprint != null){
                                mFootprints.add(footprint);
                            }
                        }
                        mFootprintAdapter.notifyDataSetChanged();
                        mAddPalletBtn.setEnabled(true);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        return;
                    }
                }
            }
        });
        mDataRepo.getFootprint((int)l);
        mDataRepo.start();
    }

    private Footprint parseVarObj(JSONObject varObj) throws JSONException {
        return new Footprint(varObj.getInt("id"),varObj.getString("footprint_name") );
    }


    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}