package com.test.test.Activities.Inbound;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.fxn.stash.Stash;
import com.google.gson.JsonArray;
import com.test.test.Adapters.ItemPageAdapter;
import com.test.test.Adapters.PageAdapter;
import com.test.test.Fragments.InboundDetailsFragment;
import com.test.test.Models.Item;
import com.test.test.Models.ItemModel;
import com.test.test.Models.Item_shipment_unit_type;
import com.test.test.Models.ListModel;
import com.test.test.Models.Lot;
import com.test.test.Models.PalletType;
import com.test.test.R;
import com.test.test.Repository.DataRepo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

public class DetailActivity extends AppCompatActivity {

    public static final int REQUEST_ADD_PALLETE_CODE = 2;

    private DataRepo.getData mDataRepo;
    private ViewPager mViewPager;
    private ProgressBar mProgressBar;
    private ListModel mListModel;
    private ArrayList<Item> mItems = new ArrayList<Item>();
    private ArrayList<Lot> mLots = new ArrayList<Lot>();
    private ArrayList<PalletType> mPalletType = new ArrayList<PalletType>();
    private ArrayList<ItemModel> mItemModels = new ArrayList<ItemModel>();
    private ItemPageAdapter mItemPageAdapter;
    private ItemModel mCopyItem;
    private boolean mIsLoad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mListModel = getIntent().getParcelableExtra("mListModel");

        mProgressBar = findViewById(R.id.progress);

        getSupportActionBar().setTitle(mListModel.Inbound_shipment_number);
        getSupportActionBar().setSubtitle("Inbound shipment number");

        mViewPager = findViewById(R.id.pager);
        mItemPageAdapter = new ItemPageAdapter(getSupportFragmentManager(), mListModel, mItemModels, mPalletType);
        mViewPager.setAdapter(mItemPageAdapter);
        load(-1);
        mProgressBar.setVisibility(View.VISIBLE);
        mPalletType = Stash.getArrayList("PalletType", PalletType.class);
        if(mPalletType != null && mPalletType.size() != 0) {
            mItemPageAdapter.setPallettypes(mPalletType);
        } else {
            mDataRepo = new DataRepo.getData(new DataRepo.onDataListener() {

                @Override
                public void returnData(String data) {
                    mProgressBar.setVisibility(View.INVISIBLE);
                    if (data != null) {
                        if (!data.isEmpty()) {
                            try {
                                JSONObject jsonResponse = new JSONObject(data);
                                JSONArray objects = jsonResponse.getJSONArray("rows");
                                for (int i1 = 0; i1 < objects.length(); i1++) {
                                    JSONObject o = objects.getJSONObject(i1);
                                    int id = o.getInt("id");
                                    String Pallet_Type = o.getString("Pallet_Type");
                                    PalletType item = new PalletType(id, Pallet_Type);
                                    mPalletType.add(item);
                                }
                                mItemPageAdapter.setPallettypes(mPalletType);
                                Stash.put("PalletType", mPalletType);
                            } catch (JSONException e) {
                                Toast.makeText(DetailActivity.this, "Error: not parse PalletType list!", Toast.LENGTH_LONG).show();
                                finish();
                            }

                        } else {
                            Toast.makeText(DetailActivity.this, "Error: PalletType list is empty!", Toast.LENGTH_LONG).show();
                            finish();
                        }
                    } else {
                        Toast.makeText(DetailActivity.this, "Error: not load PalletType list!", Toast.LENGTH_LONG).show();
                        finish();
                    }
                }
            });
            mDataRepo.getPalletType();
            mDataRepo.start();
        }
    }

    @SuppressLint("RestrictedApi")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_menu, menu);
        if (menu instanceof MenuBuilder) {
            ((MenuBuilder) menu).setOptionalIconsVisible(true);
        }
        menu.findItem(R.id.add).setEnabled(!mIsLoad);
        menu.findItem(R.id.delete).setEnabled(!mIsLoad);
        menu.findItem(R.id.copy).setEnabled(!mIsLoad);
        menu.findItem(R.id.insert).setEnabled(!mIsLoad);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.add:
                addPallete();
                break;
            case R.id.delete:
                deletePallete();
                break;
            case R.id.copy:
                copyCurrent();
                break;
            case R.id.insert:
                insertInCurrent();
                break;
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void addPallete() {
        Intent intent = new Intent(DetailActivity.this, AddPalletActivity.class);
        intent.putExtra("items", mItems);
        intent.putExtra("lots", mLots);
        intent.putExtra("list_id", mListModel.id);
        startActivityForResult(intent, REQUEST_ADD_PALLETE_CODE);
    }

    private void deletePallete() {
        if(mItemModels.size() == 1) {
            Toast.makeText(DetailActivity.this, "Don't delete last pallete!", Toast.LENGTH_LONG).show();
            return;
        }
        new AlertDialog.Builder(DetailActivity.this)
                .setTitle("Are you sure?")
                .setMessage("Pallete " + mItemModels.get(mViewPager.getCurrentItem()).id + ". " + mItemModels.get(mViewPager.getCurrentItem()).Initial_PRINTED_LPN +" will be deleted")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        mProgressBar.setVisibility(View.VISIBLE);
                        mIsLoad = true;
                        invalidateOptionsMenu();
                        int id = mItemModels.get(mViewPager.getCurrentItem()).id;
                        mDataRepo = new DataRepo.getData(new DataRepo.onDataListener() {

                            @Override
                            public void returnData(String data) {
                                if(data != null && data.equals("\"success\"")){
                                    mItemModels.remove(mViewPager.getCurrentItem());
                                    mItemPageAdapter.notifyChangeInPosition(mViewPager.getCurrentItem());
                                    mItemPageAdapter.notifyDataSetChanged();
                                }
                                mProgressBar.setVisibility(View.INVISIBLE);
                                mIsLoad = false;
                                invalidateOptionsMenu();
                            }
                        });
                        mDataRepo.deletePallet(id);
                        mDataRepo.start();
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

    private void copyCurrent() {
        ItemModel i = mItemModels.get(mViewPager.getCurrentItem());
        mCopyItem = new ItemModel(
                i.Initial_PRINTED_LPN,
                i.Inbound_shipment_number,
                i.item_article,
                i.footprint_name,
                i.inventory_status,
                i.name,
                i.shelf_life_days,
                i.Implementation_period,
                i.item_weight,
                i.item_box,
                i.id,
                i.fact_item_weight,
                i.fact_item_box,
                i.fact_weight_empty_box,
                i.fact_weight_empty_pallet,
                i.production_date,
                i.number_party,
                i.item_id,
                i.wrh_zone,
                i.Manufacturing_Date,
                i.inbound_date,
                i.Lot_number_batch,
                i.Transport_Equipment_Number,
                i.Pallet_Type,
                i.pallet_name,
                i.plan_item_weight,
                i.plan_item_box,
                i.cell_id,
                i.netto,
                i.staging_location,
                i.item_no
        );
        Toast.makeText(DetailActivity.this, "copy ok", Toast.LENGTH_SHORT).show();
    }


    private void insertInCurrent() {
        new AlertDialog.Builder(DetailActivity.this)
                .setTitle("Are you sure insert?")
                .setMessage("Pallete " + mItemModels.get(mViewPager.getCurrentItem()).Initial_PRINTED_LPN)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        ItemModel im = mItemModels.get(mViewPager.getCurrentItem());
                        im.fact_item_weight = -1;
                        im.fact_item_box= -1;
                        im.fact_weight_empty_box= mCopyItem.fact_weight_empty_box;
                        im.fact_weight_empty_pallet= mCopyItem.fact_weight_empty_pallet;
                        im.number_party= mCopyItem.number_party;
                        im.Manufacturing_Date= mCopyItem.Manufacturing_Date;
                        im.inbound_date= mCopyItem.inbound_date;
                        //im.Lot_number_batch= mCopyItem.Lot_number_batch;
                        im.Pallet_Type= mCopyItem.Pallet_Type;
                        im.Transport_Equipment_Number = mCopyItem.Transport_Equipment_Number;
                        //im.netto = mCopyItem.netto;
                        ((InboundDetailsFragment)mItemPageAdapter.getCurrentFragment()).setItemModel(im);
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

    private void load(final int index){
        mIsLoad = true;
        invalidateOptionsMenu();
        mItems.clear();
        mLots.clear();
        mItemPageAdapter.notifyDataSetChanged();
        mDataRepo = new DataRepo.getData(new DataRepo.onDataListener() {
            @Override
            public void returnData(String data) {
                if (!data.isEmpty()) {
                    mProgressBar.setVisibility(View.INVISIBLE);
                    try {
                        JSONObject jsonResponse = new JSONObject(data);
                        JSONObject objects = jsonResponse.getJSONObject("items");
                        for(int i1 = 0; i1 < objects.names().length();i1++){
                            String id = objects.names().getString(i1);
                            JSONObject o = objects.getJSONObject(objects.names().getString(i1));
                            String Item_No = o.getString("Item_No");
                            String Item_article = o.getString("Item_article");
                            Item item = new Item(id, Item_No, Item_article);
                            mItems.add(item);
                        }
                        if(jsonResponse.isNull("lots"))
                        {
                            mLots.add(Lot.getEmptyLot());
                        } else {
                            try {
                                objects = jsonResponse.getJSONObject("lots");
                                if (objects.names().length() == 0) {
                                    mLots.add(Lot.getEmptyLot());
                                } else {
                                    for (int i1 = 0; i1 < objects.names().length(); i1++) {
                                        String id = objects.names().getString(i1);
                                        JSONObject o = objects.getJSONObject(objects.names().getString(i1));
                                        String Name = o.getString("Name");
                                        int Unit_id = o.getInt("Unit_id");
                                        Lot lot = new Lot(id, Name, Unit_id);
                                        mLots.add(lot);
                                    }
                                }
                            }
                            catch (JSONException e) {
                                mLots.add(Lot.getEmptyLot());
                            }
                        }
                        JSONArray array = jsonResponse.getJSONArray("Inbound_shipment");
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject varObj = array.getJSONObject(i);
                            ItemModel model = parseVarObj(varObj);
                            boolean contain = false;
                            for(int i2=0;i2< mItemModels.size();i2++) {
                                if(model.id == mItemModels.get(i2).id) {
                                    contain = true;
                                    break;
                                }
                            }
                            if(!contain)
                                mItemModels.add(model);
                        }
                        mItemPageAdapter.notifyDataSetChanged();
                        if(index != -1 && index < mItemModels.size())
                            mViewPager.setCurrentItem(index);

                    } catch (JSONException e) {
                        mIsLoad = false;
                        invalidateOptionsMenu();
                        Toast.makeText(DetailActivity.this, "Error: not load pallet list: " + e.toString(), Toast.LENGTH_LONG).show();
                        return;
                    }
                }
                mIsLoad = false;
                invalidateOptionsMenu();
            }
        });
        mDataRepo.getListById(String.valueOf(mListModel.id));
        mDataRepo.start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == REQUEST_ADD_PALLETE_CODE && resultCode == 0) {
            Toast.makeText(DetailActivity.this, "ADDED", Toast.LENGTH_SHORT).show();
            load(mItemModels.size());
        } else
            super.onActivityResult(requestCode, resultCode, data);
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
            if(!varObj.isNull("item_article"))
                item_article =varObj.getString("item_article");
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
            double netto = -1.0;
            if(!varObj.isNull("netto"))
                netto = varObj.getDouble("netto");
            String staging_location = null;
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
