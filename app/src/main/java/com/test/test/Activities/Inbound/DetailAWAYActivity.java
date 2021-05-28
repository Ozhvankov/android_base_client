package com.test.test.Activities.Inbound;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bosphere.filelogger.FL;
import com.fxn.stash.Stash;
import com.test.test.Adapters.AdapterPallets;
import com.test.test.Models.ItemModel;
import com.test.test.Models.ListModel;
import com.test.test.R;
import com.test.test.Repository.DataRepo;
import com.test.test.Utils.RecyclerItemClickListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class DetailAWAYActivity extends AppCompatActivity {

    private DataRepo.getData mDataRepo, mGetData;
    private ProgressBar mProgressBar;
    private TextView isn;
    private ListModel mListModel;
    private ArrayList<ItemModel> mItemModels = new ArrayList<ItemModel>();
    private RecyclerView mPalletsList;
    private EditText mSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_away);

        mListModel = getIntent().getParcelableExtra("mListModel");

        isn = findViewById(R.id.isn_text);

        mProgressBar = findViewById(R.id.progress);
        mSearch = findViewById(R.id.search_pallete);

        mSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                ((AdapterPallets) mPalletsList.getAdapter()).setMask(mSearch.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        mPalletsList = findViewById(R.id.cells_list);
        mPalletsList.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mPalletsList.setLayoutManager(layoutManager);
        mPalletsList.addOnItemTouchListener(new RecyclerItemClickListener(this, mPalletsList ,new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, final int position) {
                Intent intent = new Intent(DetailAWAYActivity.this, SetPalleteToSelectCellActivity.class);
                intent.putExtra("mListModel", mListModel);
                intent.putExtra("mPallete", ((AdapterPallets)mPalletsList.getAdapter()).getPallet(position));
                DetailAWAYActivity.this.startActivityForResult(intent,3);
            }
        }));

        isn.setText(mListModel.Inbound_shipment_number);
        setResult(1);
        load(-1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == 3 && resultCode == 0) {
            setResult(0);
            finish();
        }if(requestCode == 3 && resultCode == 1) {
            load(-1);
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void load(final int index){
        mProgressBar.setVisibility(View.VISIBLE);
        mSearch.setEnabled(false);
        mItemModels.clear();
        mDataRepo = new DataRepo.getData(new DataRepo.onDataListener() {
            @Override
            public void returnData(String data) {
                if (!data.isEmpty()) {
                    mProgressBar.setVisibility(View.INVISIBLE);
                    try {
                        JSONObject jsonResponse = new JSONObject(data);
                        JSONArray array = jsonResponse.getJSONArray("Inbound_shipment");
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject varObj = array.getJSONObject(i);
                            ItemModel model = parseVarObj(varObj);
                            if(model != null){
                                mItemModels.add(model);
                            }
                        }
                        AdapterPallets adapter = new AdapterPallets(DetailAWAYActivity.this, mItemModels);
                        mPalletsList.setAdapter(adapter);
                        mSearch.setEnabled(true);
                        ((AdapterPallets) mPalletsList.getAdapter()).setMask(mSearch.getText().toString());
                    } catch (JSONException e) {
                        if(Stash.getBoolean("logger")) {
                            FL.d("Error: not load pallet list: " + e.toString());
                        }
                        Toast.makeText(DetailAWAYActivity.this, "Error: not load pallet list: " + e.toString(), Toast.LENGTH_LONG).show();
                        return;
                    }
                }
            }
        });
        mDataRepo.getListById(String.valueOf(mListModel.id));
        mDataRepo.start();
    }

    private ItemModel parseVarObj(JSONObject varObj) throws JSONException {
        ItemModel model = null;
            final Bundle bundle = new Bundle();
            String Initial_PRINTED_LPN = "";
            if(!varObj.isNull("Initial_PRINTED_LPN"))
                Initial_PRINTED_LPN = varObj.getString("Initial_PRINTED_LPN");
            String Inbound_shipment_number = "";
            if(!varObj.isNull("Inbound_shipment_number"))
                Inbound_shipment_number =varObj.getString("Inbound_shipment_number");
            String item_article= "";;
            if(!varObj.isNull("Item_article"))
                item_article =varObj.getString("Item_article");
            String footprint_name= "";;
            if(!varObj.isNull("footprint_name"))
                footprint_name =varObj.getString("footprint_name");
            String inventory_status= "";;
            if(!varObj.isNull("inventory_status"))
                inventory_status =varObj.getString("inventory_status");
            String name= "";;
            if(!varObj.isNull("name"))
                name =varObj.getString("name");
            int shelf_life_days= -1;
            if(!varObj.isNull("shelf_life_days"))
                shelf_life_days= varObj.getInt("shelf_life_days");
            String Implementation_period = "";
            if(!varObj.isNull("Implementation_period"))
                Implementation_period= varObj.getString("Implementation_period");
            int item_weight = -1;
            if(!varObj.isNull("item_weight"))
                item_weight =varObj.getInt("item_weight");
            int item_box= -1;
            if(!varObj.isNull("item_box"))
                item_box =varObj.getInt("item_box");
            int id= -1;
            if(!varObj.isNull("id"))
                id =varObj.getInt("id");
            double fact_item_weight= -1.0;
            if(!varObj.isNull("fact_item_weight"))
                fact_item_weight= varObj.getDouble("fact_item_weight");
            int fact_item_box= -1;
            if(!varObj.isNull("fact_item_box"))
                fact_item_box= varObj.getInt("fact_item_box");
            double fact_weight_empty_box= -1.0;
            if(!varObj.isNull("fact_weight_empty_box"))
                fact_weight_empty_box= varObj.getDouble("fact_weight_empty_box");
            String fact_weight_empty_pallet= "";;
            if(!varObj.isNull("fact_weight_empty_pallet"))
                fact_weight_empty_pallet= varObj.getString("fact_weight_empty_pallet");
            String production_date= "";;
            if(!varObj.isNull("production_date"))
                production_date= varObj.getString("production_date");
            String number_party= "";;
            if(!varObj.isNull("number_party"))
                number_party= varObj.getString("number_party");
            int item_id= -1;
            if(!varObj.isNull("item_id"))
                item_id= varObj.getInt("item_id");
            int wrh_zone= -1;
            if(!varObj.isNull("wrh_zone"))
                wrh_zone= varObj.getInt("wrh_zone");
            String Manufacturing_Date= "";;
            if(!varObj.isNull("Manufacturing_Date"))
                Manufacturing_Date= varObj.getString("Manufacturing_Date");
            String inbound_date= "";;
            if(!varObj.isNull("inbound_date"))
                inbound_date= varObj.getString("inbound_date");
            String Lot_number_batch= "";
            if(!varObj.isNull("Lot_number_batch"))
                Lot_number_batch= varObj.getString("Lot_number_batch");
            String Transport_Equipment_Number= "";
            if(!varObj.isNull("Transport_Equipment_Number"))
                Transport_Equipment_Number= varObj.getString("Transport_Equipment_Number");
            int Pallet_Type= -1;
            if(!varObj.isNull("Pallet_Type"))
                Pallet_Type= varObj.getInt("Pallet_Type");
            String pallet_name= "";;
            if(!varObj.isNull("pallet_name"))
                pallet_name= varObj.getString("pallet_name");
            int plan_item_weight= -1;
            if(!varObj.isNull("plan_item_weight"))
                plan_item_weight= varObj.getInt("plan_item_weight");
            int plan_item_box= -1;
            if(!varObj.isNull("plan_item_box"))
                plan_item_box= varObj.getInt("plan_item_box");
            int cell_id = -1;
            if(!varObj.isNull("cell_id"))
                cell_id = varObj.getInt("cell_id");
        int netto = -1;
        if(!varObj.isNull("netto"))
            netto = varObj.getInt("netto");
        String staging_location = "";
        if(!varObj.isNull("staging_location"))
            staging_location = varObj.getString("staging_location");
        String item_no = null;
        if(!varObj.isNull("item_no"))
            item_no = varObj.getString("item_no");
        model = new ItemModel(
                    Initial_PRINTED_LPN,
                    Inbound_shipment_number,
                    item_article,
                    footprint_name,
                    inventory_status,
                    name,
                    shelf_life_days,
                    Implementation_period,
                    item_weight,
                    item_box,
                    id,
                    fact_item_weight,
                    fact_item_box,
                    fact_weight_empty_box,
                    fact_weight_empty_pallet,
                    production_date,
                    number_party,
                    item_id,
                    wrh_zone,
                    Manufacturing_Date,
                    inbound_date,
                    Lot_number_batch,
                    Transport_Equipment_Number,
                    Pallet_Type,
                    pallet_name,
                    plan_item_weight,
                    plan_item_box,
                    cell_id,
                    netto,
                    staging_location,
                    item_no
            );
        return model;
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
                        DetailAWAYActivity.super.onBackPressed();
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
