package com.test.test.Activities.Stock;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bosphere.filelogger.FL;
import com.fxn.stash.Stash;
import com.test.test.Adapters.AdapterPalletsOperation;
import com.test.test.Models.EditableItem;
import com.test.test.Models.ItemModel;
import com.test.test.R;
import com.test.test.Repository.DataRepo;
import com.test.test.Utils.RecyclerItemClickListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class StockOperationActivity extends AppCompatActivity {

    private DataRepo.getData mDataRepo, mGetData;
    private ProgressBar mProgressBar;
    private ArrayList<ItemModel> mItemModels = new ArrayList<ItemModel>();
    private RecyclerView mPalletsList;
    private EditText mSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_operation);
        mProgressBar = findViewById(R.id.progress);
        mSearch = findViewById(R.id.search_pallete);
        mSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                ((AdapterPalletsOperation) mPalletsList.getAdapter()).setMask(mSearch.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        mSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    mPalletsList.requestFocus();
                    return true;
                }
                return false;
            }
        });
        mPalletsList = findViewById(R.id.cells_list);
        mPalletsList.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mPalletsList.setLayoutManager(layoutManager);
        mPalletsList.addOnItemTouchListener(new RecyclerItemClickListener(this, mPalletsList ,new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, final int position) {

                // Initializing a new alert dialog
                final AlertDialog.Builder builder = new AlertDialog.Builder(StockOperationActivity.this);
                // Set the alert dialog title
                builder.setTitle(((AdapterPalletsOperation) mPalletsList.getAdapter()).getPallet(position).Initial_PRINTED_LPN);
                // Initialize a new list of flowers
                final List<String> flowers = new ArrayList<String>();
                flowers.add("Modify");
                flowers.add("Inventory");

                // Initialize a new array adapter instance
                ArrayAdapter arrayAdapter = new ArrayAdapter<String>(
                        StockOperationActivity.this, // Context
                        android.R.layout.simple_list_item_single_choice, // Layout
                        flowers // List
                );
                builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialogInterface) {
                            dialogInterface.dismiss();
                        }
                });
                // Set a single choice items list for alert dialog
                builder.setSingleChoiceItems(
                        arrayAdapter, // Items list
                        -1, // Index of checked item (-1 = no selection)
                        new DialogInterface.OnClickListener() // Item click listener
                        {
                            @Override
                            public void onClick(DialogInterface dialogInterface, final int i) {
                                dialogInterface.dismiss();
                                mProgressBar.setVisibility(View.VISIBLE);
                                mSearch.setEnabled(false);
                                mDataRepo = new DataRepo.getData(new DataRepo.onDataListener() {
                                    @Override
                                    public void returnData(String data) {
                                        mProgressBar.setVisibility(View.INVISIBLE);
                                        mSearch.setEnabled(true);
                                        if (!data.isEmpty()) {
                                                if(data.contains("[{\"error\":true")) {
                                                    Toast.makeText(StockOperationActivity.this, "Wrong select pallete: " + mItemModels.get(position).id +". " + mItemModels.get(position).Initial_PRINTED_LPN, Toast.LENGTH_LONG).show();
                                                    return;
                                                }
                                            try {
                                                JSONObject jsonResponse = new JSONObject(data);
                                                EditableItem item = new EditableItem();
                                                item.id = jsonResponse.getInt("id");
                                                item.Inbound_shipment_number = jsonResponse.getString("Inbound_shipment_number");
                                                item.box_current = jsonResponse.getInt("box_current");
                                                item.inbound_date = jsonResponse.getString("inbound_date");
                                                item.Manufacturing_Date = jsonResponse.getString("Manufacturing_Date");
                                                item.kg_current = jsonResponse.getInt("kg_current");
                                                item.Item_inventory_status = jsonResponse.getInt("Item_inventory_status");
                                                item.Lot_number_batch = jsonResponse.getString("Lot_number_batch");
                                                item.Transport_Equipment_Number = jsonResponse.getString("Transport_Equipment_Number");
                                                item.cell_id = jsonResponse.getInt("cell_id");
                                                item.cell_name = jsonResponse.getString("cell_name");
                                                item.item_id = jsonResponse.getInt("item_id");
                                                item.Pallet_Type = jsonResponse.getInt("Pallet_Type");
                                                item.Supplier= jsonResponse.getString("Supplier");
                                                item.Client= jsonResponse.getString("Client");
                                                item.Item_No = jsonResponse.getInt("Item_No");
                                                item.kg_avaible = jsonResponse.getInt("kg_avaible");
                                                item.kg_reserved = jsonResponse.getInt("kg_reserved");
                                                item.box_avaible = jsonResponse.getInt("box_avaible");
                                                item.box_reserved = jsonResponse.getInt("box_reserved");
                                                item.fact_item_weight = jsonResponse.getString("fact_item_weight");
                                                Intent intent;
                                                if(i == 0) {
                                                    intent = new Intent(StockOperationActivity.this, StockModifyActivity.class);
                                                } else {
                                                    intent = new Intent(StockOperationActivity.this, StockInventaryActivity.class);
                                                }
                                                intent.putExtra("item", item);
                                                intent.putExtra("mItemModel", ((AdapterPalletsOperation) mPalletsList.getAdapter()).getPallet(position));
                                                startActivity(intent);
                                            } catch (JSONException e) {
                                                if(Stash.getBoolean("logger")) {
                                                    FL.d("Wrong load pallete: " + e.toString());
                                                }
                                                Toast.makeText(StockOperationActivity.this, "Wrong load pallete: " + e.toString(), Toast.LENGTH_LONG).show();
                                                return;
                                            }
                                        }
                                    }
                                });
                                mDataRepo.getItem(((AdapterPalletsOperation) mPalletsList.getAdapter()).getPallet(position).id);
                                mDataRepo.start();
                            }
                        });

                // Create the alert dialog
                AlertDialog dialog = builder.create();
                // Finally, display the alert dialog
                dialog.show();


            }
        }));
        load(-1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == 3 && resultCode == 0){
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
                        JSONArray array = jsonResponse.getJSONArray("rows");
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject varObj = array.getJSONObject(i);
                            ItemModel model = parseVarObj(varObj);
                            if(model != null){
                                mItemModels.add(model);
                            }
                        }
                        AdapterPalletsOperation adapter = new AdapterPalletsOperation(StockOperationActivity.this, mItemModels);
                        mPalletsList.setAdapter(adapter);
                        mSearch.setEnabled(true);
                        ((AdapterPalletsOperation) mPalletsList.getAdapter()).setMask(mSearch.getText().toString());
                    } catch (JSONException e) {
                        if(Stash.getBoolean("logger")) {
                            FL.d("Error: not load pallet list: " + e.toString());
                        }
                        Toast.makeText(StockOperationActivity.this, "Error: not load pallet list: " + e.toString(), Toast.LENGTH_LONG).show();
                        return;
                    }
                }
            }
        });
        mDataRepo.getStockLpnList();
        mDataRepo.start();
    }

    private ItemModel parseVarObj(JSONObject varObj) throws JSONException {
            String Initial_PRINTED_LPN = "";
            if(!varObj.isNull("Initial_PRINTED_LPN"))
                Initial_PRINTED_LPN = varObj.getString("Initial_PRINTED_LPN");
            int id = -1;
            if(!varObj.isNull("id"))
                id =varObj.getInt("id");
        return new ItemModel(id, Initial_PRINTED_LPN);
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
                        StockOperationActivity.super.onBackPressed();
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
