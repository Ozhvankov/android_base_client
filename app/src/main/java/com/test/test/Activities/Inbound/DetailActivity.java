package com.test.test.Activities.Inbound;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.test.test.Adapters.ItemPageAdapter;
import com.test.test.Adapters.PageAdapter;
import com.test.test.Fragments.InboundDetailsFragment;
import com.test.test.Models.Item;
import com.test.test.Models.ItemModel;
import com.test.test.Models.ListModel;
import com.test.test.R;
import com.test.test.Repository.DataRepo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

public class DetailActivity extends AppCompatActivity {

    private DataRepo.getData mDataRepo, mGetData;

    private ViewPager mViewPager;
    private ProgressBar mProgressBar;
    private TextView isn, supplier, client, items, status;
    private ListModel mListModel;
    private ArrayList<Item> mItems = new ArrayList<Item>();
    private ArrayList<ItemModel> mItemModels = new ArrayList<ItemModel>();
    private ItemPageAdapter mItemPageAdapter;
    private Button mDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mListModel = getIntent().getParcelableExtra("mListModel");

        isn = findViewById(R.id.isn_text);
        supplier = findViewById(R.id.supplier_text);
        client = findViewById(R.id.client_text);
        items = findViewById(R.id.items_text);
        status = findViewById(R.id.status_text);
        mProgressBar = findViewById(R.id.progress);

        isn.setText(mListModel.Inbound_shipment_number);
        client.setText(String.valueOf(mListModel.Client));
        supplier.setText(String.valueOf(mListModel.Supplier));
        items.setText(mListModel.Item_articles);
        status.setText(String.valueOf(mListModel.status_id));
        mViewPager = findViewById(R.id.pager);
        mItemPageAdapter = new ItemPageAdapter(getSupportFragmentManager(), mItemModels);
        mViewPager.setAdapter(mItemPageAdapter);
        mDelete = findViewById(R.id.delete);
        findViewById(R.id.pallet_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailActivity.this, AddPalletActivity.class);
                intent.putExtra("items", mItems);
                startActivityForResult(intent, 2);
            }
        });
        mDelete.setEnabled(false);
        mDataRepo = new DataRepo.getData(new DataRepo.onDataListener() {
            @Override
            public void returnData(String data) {
                if (!data.isEmpty()) {
                    mProgressBar.setVisibility(View.GONE);
                    try {
                    JSONObject jsonResponse = new JSONObject(data);
                        JSONObject objects = jsonResponse.getJSONObject("items");
                        for(int i1 = 0; i1< objects.names().length();i1++){
                            String id = objects.names().getString(i1);
                            JSONObject o = objects.getJSONObject(objects.names().getString(i1));
                            String Item_No = o.getString("Item_No");
                            String Item_article = o.getString("Item_article");
                            Item Item = new Item(id, Item_No, Item_article);
                            mItems.add(Item);
                        }
                        JSONArray array = jsonResponse.getJSONArray("Inbound_shipment");
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject varObj = array.getJSONObject(i);
                            ItemModel model = parseVarObj(varObj);
                            if(model != null){
                                mItemModels.add(model);
                            }
                        }
                        mItemPageAdapter.notifyDataSetChanged();
                        mDelete.setEnabled(true);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        return;
                    }
                }
            }
        });
        mDataRepo.getListById(String.valueOf(mListModel.id));
        mDataRepo.start();

        mDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mItemModels.size() == 1){
                    Toast.makeText(DetailActivity.this, "Нельзя удалить последнюю паллету!", Toast.LENGTH_LONG).show();
                    return;
                }
                mProgressBar.setVisibility(View.VISIBLE);
                mDelete.setEnabled(false);
                int id = mItemModels.get(mViewPager.getCurrentItem()).id;
                mDataRepo = new DataRepo.getData(new DataRepo.onDataListener() {

                    @Override
                    public void returnData(String data) {
                        if(data != null && data.equals("\"success\"")){
                            mItemModels.remove(mViewPager.getCurrentItem());
                            mItemPageAdapter.notifyDataSetChanged();
                        }
                        mProgressBar.setVisibility(View.GONE);
                        mDelete.setEnabled(true);
                    }
                });
                mDataRepo.deletePallet(id);
                mDataRepo.start();
            }
        });
    }

    private ItemModel parseVarObj(JSONObject varObj) {
        ItemModel model = null;
        try {
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
            int Implementation_period = -1;
            if(!varObj.isNull("Implementation_period"))
                Implementation_period= varObj.getInt("Implementation_period");
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
            int Lot_number_batch= -1;
            if(!varObj.isNull("Lot_number_batch"))
                Lot_number_batch= varObj.getInt("Lot_number_batch");
            int Transport_Equipment_Number= -1;
            if(!varObj.isNull("Transport_Equipment_Number"))
                Transport_Equipment_Number= varObj.getInt("Transport_Equipment_Number");
            String Pallet_Type= "";;
            if(!varObj.isNull("Pallet_Type"))
                Pallet_Type= varObj.getString("Pallet_Type");
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
                    cell_id
            );
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
