package com.test.test.Activities.Stock;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.test.test.Models.PalletType;
import com.test.test.Models.StatusModel;
import com.test.test.R;
import com.test.test.Repository.DataRepo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class ModifyActivity extends AppCompatActivity {

    private TextView mInboundNumTxt, mSupplierTxt, mClientTxt, mItemsTxt, mArticlesTxt, mLpnTxt,
            mInboundDateTxt, mManufactureDateTxt;
    private EditText mEditText, mTransportNumberEdit;
    private Spinner mPalletTypeSpinner, mPalletStatusSpinner;
    private AutoCompleteTextView mPalletLocationAutocomplete;
    private ProgressBar mProgressBar;
    private Button mSaveBtn;

    private DataRepo.getData mDataRepo;
    private HashMap<String, String> mCells;
    private List<String> mCellLocs;
    private int mId;
    private String mCellId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify);
        mInboundNumTxt = findViewById(R.id.inbound_number_text);
        mSupplierTxt = findViewById(R.id.supplier_text);
        mClientTxt = findViewById(R.id.client_text);
        mItemsTxt = findViewById(R.id.items_text);
        mArticlesTxt = findViewById(R.id.articles_text);
        mLpnTxt = findViewById(R.id.lpn_text);
        mInboundDateTxt = findViewById(R.id.inbound_date_text);
        mManufactureDateTxt = findViewById(R.id.manufacture_date_text);

        mEditText = findViewById(R.id.number_batch_edit);
        mTransportNumberEdit = findViewById(R.id.trasport_equip_num_edit);

        mPalletTypeSpinner = findViewById(R.id.pallet_type_spinner);
        mPalletStatusSpinner = findViewById(R.id.pallet_status_spinner);

        mPalletLocationAutocomplete = findViewById(R.id.pallet_location_autocomplete);
        mProgressBar = findViewById(R.id.progress);

        mSaveBtn = findViewById(R.id.save_btn);

        Intent intent = getIntent();
        if (intent != null) {
            try {
                final JSONObject jsonObject = new JSONObject(Objects.requireNonNull(intent.getStringExtra("data")));
                mInboundNumTxt.setText(jsonObject.getString("Inbound_shipment_number"));
                mSupplierTxt.setText(jsonObject.getString("Supplier"));
                mClientTxt.setText(jsonObject.getString("Client"));
                mItemsTxt.setText(jsonObject.getString("item_subgroup"));
                mArticlesTxt.setText(jsonObject.getString("Items_name"));
                mLpnTxt.setText(jsonObject.getString("Initial_PRINTED_LPN"));
                mInboundDateTxt.setText(jsonObject.getString("inbound_date"));
                mManufactureDateTxt.setText(jsonObject.getString("Manufacturing_Date"));
                mEditText.setText(jsonObject.getString("Lot_number_batch"));
                mPalletLocationAutocomplete.setText(jsonObject.getString("Location"));
                mTransportNumberEdit.setText(jsonObject.getString("Transport_Equipment_Number"));
                setPalletSpinner(mPalletTypeSpinner, jsonObject.getInt("Pallet_Type"));
                setStatusSpinner(mPalletStatusSpinner, jsonObject.getInt("Item_inventory_status"));
                mId = jsonObject.getInt("id");
                mCellId = jsonObject.getString("cell_id");
                mDataRepo = new DataRepo.getData(new DataRepo.onDataListener() {
                    @Override
                    public void returnData(String data) {
                        if (!data.isEmpty()) {
                            Log.d("asdasd", data);
                            mCells = new HashMap<>();
                            mProgressBar.setVisibility(View.GONE);
                            try {
                                JSONObject jsonObject1 = new JSONObject(data);
                                JSONArray array = jsonObject1.getJSONArray("rows");
                                final HashMap<String, String> celldata = new HashMap<>();
                                for (int i = 0; i < array.length(); i++) {
                                    jsonObject1 = array.getJSONObject(i);
                                    celldata.put(jsonObject1.getString("id"), jsonObject1.getString("Location"));
                                    mCells.put(jsonObject1.getString("Location"), jsonObject1.getString("id"));
                                }
                                mCellLocs = new ArrayList<>(mCells.keySet());
                                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(ModifyActivity.this,
                                        android.R.layout.simple_dropdown_item_1line,
                                        mCellLocs);
                                mPalletLocationAutocomplete.setAdapter(arrayAdapter);
                                mPalletLocationAutocomplete.setOnItemClickListener(
                                        new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> adapterView, View view, int i,
                                                    long l) {
                                                mCellId = mCells.get(mPalletLocationAutocomplete.getText().toString());
                                            }
                                        });

                                mSaveBtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        if (mPalletLocationAutocomplete.getText().toString().isEmpty()) {
                                            Toast.makeText(ModifyActivity.this,
                                                    "Enter cell data!", Toast.LENGTH_SHORT)
                                                    .show();
                                        } else if (celldata.size() == 0) {
                                            Toast.makeText(ModifyActivity.this,
                                                    "Cell data err!", Toast.LENGTH_SHORT)
                                                    .show();
                                        } else {
                                            saveData(mId,
                                                    mInboundDateTxt.getText().toString(),
                                                    mManufactureDateTxt.getText().toString(),
                                                    mEditText.getText().toString(),
                                                    mTransportNumberEdit.getText().toString(),
                                                    String.valueOf(mPalletTypeSpinner.getSelectedItemPosition() + 1),
                                                    String.valueOf(mPalletStatusSpinner.getSelectedItemPosition() + 1),
                                                    mCellId);
                                        }
                                    }
                                });
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
                mDataRepo.getCellsList(jsonObject.getString("wrh_zone"));
                mDataRepo.start();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


    }

    void setStatusSpinner(Spinner statusSpinner, int status) {

        List<StatusModel> models = new ArrayList<>();
        models.add(new StatusModel(1, "Blocked"));
        models.add(new StatusModel(2, "Available Product"));
        models.add(new StatusModel(3, "Blocked for Return"));
        models.add(new StatusModel(4, "Damaged Product"));
        models.add(new StatusModel(5, "Relabling"));
        models.add(new StatusModel(6, "Sorting out"));
        models.add(new StatusModel(7, "Repacking"));

        ArrayAdapter<StatusModel> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, models);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statusSpinner.setAdapter(adapter);
        statusSpinner.setSelection(status - 1);
    }

    void setPalletSpinner(Spinner spinner, int id) {

        List<PalletType> models = new ArrayList<>();
        models.add(new PalletType(1, "EURO"));
        models.add(new PalletType(2, "FIN"));

        ArrayAdapter<PalletType> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, models);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(id - 1);
    }

    void saveData(int ID, String inboundDate, String manufactureDate, String lotNum, String TransportEqNum,
            String palletType, String itemInventStatus, String cellId) {

        HashMap<String, String> sendMap = new HashMap<>();
        sendMap.put("inbound_date", inboundDate);
        sendMap.put("Manufacturing_Date", manufactureDate);
        sendMap.put("Lot_number_batch", lotNum);
        sendMap.put("Transport_Equipment_Number", TransportEqNum);
        sendMap.put("Pallet_Type", palletType);
        sendMap.put("Item_inventory_status", itemInventStatus);
        sendMap.put("cell_id", cellId);
        DataRepo.getData getData = new DataRepo.getData(new DataRepo.onDataListener() {
            @Override
            public void returnData(String data) {
                Toast.makeText(ModifyActivity.this, "Ok", Toast.LENGTH_SHORT).show();
            }
        });
        getData.setModify(ID, sendMap);
        getData.start();
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Are you sure?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ModifyActivity.super.onBackPressed();
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
