package com.test.test.Activities.Stock;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.test.test.R;
import com.test.test.Repository.DataRepo;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Objects;

public class InventoryActivity extends AppCompatActivity {

    private TextView mInboundTxt, mSupplierTxt, mClientTxt,
            mItemsTxt, mArticlesTxt, mLpnTxt, mAvailableQuantityTxt,
            mReservedQuantityTxt, mAvailableBoxTxt, mCurrentQuantityTxt, mReservedQuantityBoxTxt;
    private EditText mCurrentBoxEdit;
    private Intent mIntent;
    private Button mSaveBtn;
    private int mId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);

        mInboundTxt = findViewById(R.id.inboundShipmentTxt);
        mSupplierTxt = findViewById(R.id.supplier_text);
        mClientTxt = findViewById(R.id.client_text);
        mItemsTxt = findViewById(R.id.items_text);
        mArticlesTxt = findViewById(R.id.articles_text);
        mLpnTxt = findViewById(R.id.lpn_text);
        mAvailableQuantityTxt = findViewById(R.id.available_quantity_text);
        mReservedQuantityTxt = findViewById(R.id.reserved_quantity_text);
        mReservedQuantityBoxTxt = findViewById(R.id.reserved_quantity_box_text);
        mAvailableBoxTxt = findViewById(R.id.available_quantity_box_text);
        mCurrentQuantityTxt = findViewById(R.id.current_kg_text);
        mCurrentBoxEdit = findViewById(R.id.current_box_text);
        mSaveBtn = findViewById(R.id.save_btn);

        mIntent = getIntent();
        if (mIntent != null) {
            JSONObject object;
            try {
                object = new JSONObject(Objects.requireNonNull(mIntent.getStringExtra("data")));
                mId = object.getInt("id");
                mInboundTxt.setText(object.getString("Inbound_shipment_number"));
                mSupplierTxt.setText(object.getString("Supplier"));
                mClientTxt.setText(object.getString("Client"));
                mItemsTxt.setText(object.getString("item_subgroup"));
                mArticlesTxt.setText(object.getString("Items_name"));
                mLpnTxt.setText(object.getString("Initial_PRINTED_LPN"));
                mAvailableQuantityTxt.setText(object.getString("kg_avaible"));
                mReservedQuantityTxt.setText(object.getString("kg_reserved"));
                mAvailableBoxTxt.setText(object.getString("box_avaible"));
                mCurrentQuantityTxt.setText(object.getString("kg_current"));
                mCurrentBoxEdit.setText(object.getString("box_current"));
                mReservedQuantityBoxTxt.setText(object.getString("box_reserved"));
                final JSONObject finalObject = object;
                mSaveBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (mCurrentBoxEdit.getText().toString().isEmpty() ||
                                mCurrentQuantityTxt.getText().toString().isEmpty()) {
                            Toast.makeText(InventoryActivity.this,
                                    "Data is empty!",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            saveData(
                                    mId,
                                    mCurrentQuantityTxt.getText().toString(),
                                    mCurrentBoxEdit.getText().toString(),
                                    finalObject
                            );
                        }
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    void saveData(int ID, String currentKg, String currentBox, JSONObject object) {
        HashMap<String, String> sendMap = new HashMap<>();
        sendMap.put("kg_current", currentKg);
        sendMap.put("box_current", currentBox);
        DataRepo.getData getData = new DataRepo.getData(new DataRepo.onDataListener() {
            @Override
            public void returnData(String data) {
                Toast.makeText(InventoryActivity.this, "Ok", Toast.LENGTH_SHORT).show();
            }
        });
        getData.setInventory(ID, sendMap);
        try {
            if (object.getInt("box_avaible") == 0) {
                Toast.makeText(this, "Error, can't add box", Toast.LENGTH_SHORT).show();
            } else {
                getData.start();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Are you sure?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        InventoryActivity.super.onBackPressed();
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
