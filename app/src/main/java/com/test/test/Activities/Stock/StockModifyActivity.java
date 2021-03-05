package com.test.test.Activities.Stock;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.fxn.stash.Stash;
import com.test.test.Activities.Inbound.AddPalletActivity;
import com.test.test.Activities.Inbound.DetailActivity;
import com.test.test.Adapters.AdapterPalletsOperation;
import com.test.test.Adapters.InventoryStatusAdapter;
import com.test.test.Adapters.PalletTypeAdapter;
import com.test.test.Models.EditableItem;
import com.test.test.Models.InventoryStatus;
import com.test.test.Models.ItemModel;
import com.test.test.Models.PalletType;
import com.test.test.R;
import com.test.test.Repository.DataRepo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class StockModifyActivity extends AppCompatActivity {

    private DataRepo.getData mDataRepo, mGetData;
    private Button mManufactDateEdit;
    private Button mInboundDateEdit;
    private SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private Calendar mCalendar = Calendar.getInstance();
    private ProgressBar mProgressBar;
    private Button mSave;
    private ArrayList<PalletType> mPalletType = new ArrayList<>();
    private ArrayList<InventoryStatus> mInventoryStatus = new ArrayList<>();
    private Spinner palletTypeSpinner;
    private Spinner spinner_inventory_status;
    private EditText mNumLotEdit;
    private EditText mTransNnumEdit;

    private InventoryStatusAdapter mInvetaryStatusAdapter;
    private Button mLocation;
    private ItemModel model;
    private EditableItem item;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_modify);
        item = (EditableItem)getIntent().getParcelableExtra("item");
        model = (ItemModel)getIntent().getSerializableExtra("mItemModel");
        palletTypeSpinner = (Spinner)findViewById(R.id.pallet_type_spinner2);
        spinner_inventory_status = (Spinner)findViewById(R.id.spinner_inventory_status);
        TextView lpn = findViewById(R.id.lpn);
        lpn.setText(model.Initial_PRINTED_LPN);
        mProgressBar = (ProgressBar)findViewById(R.id.progress4);
        mProgressBar.setVisibility(View.INVISIBLE);
        mManufactDateEdit = findViewById(R.id.manufact_date_edit2);
        mInboundDateEdit = findViewById(R.id.inb_date_edit2);
        mManufactDateEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCalendar.setTime(new Date());
                try {
                    Date d = mSimpleDateFormat.parse(mManufactDateEdit.getText().toString());
                    mCalendar.setTime(d);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                new DatePickerDialog(StockModifyActivity.this, mManufactDateEditListener,
                        mCalendar.get(Calendar.YEAR),
                        mCalendar.get(Calendar.MONTH),
                        mCalendar.get(Calendar.DAY_OF_MONTH))
                        .show();
            }
        });

        mInboundDateEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCalendar.setTime(new Date());
                try {
                    Date d = mSimpleDateFormat.parse(mInboundDateEdit.getText().toString());
                    mCalendar.setTime(d);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                new DatePickerDialog(StockModifyActivity.this, mInboundDateEditListener,
                        mCalendar.get(Calendar.YEAR),
                        mCalendar.get(Calendar.MONTH),
                        mCalendar.get(Calendar.DAY_OF_MONTH))
                        .show();
            }
        });
        Date Manufacturing_Date = new Date();
        if (item.Manufacturing_Date.equals("0000-00-00")) {
            mManufactDateEdit.setText(mSimpleDateFormat.format(Manufacturing_Date));
        } else {
            mManufactDateEdit.setText(item.Manufacturing_Date);
        }
        Date inbound_date = new Date();
        if (item.inbound_date.equals("0000-00-00")) {
            mInboundDateEdit.setText(mSimpleDateFormat.format(inbound_date));
        } else {
            mInboundDateEdit.setText(item.inbound_date);
        }
        mSave = (Button)findViewById(R.id.save_btn2);
        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mInboundDateEdit.getText().toString().length() == 0){
                    Toast.makeText(StockModifyActivity.this, "InboundDateEdit empty!", Toast.LENGTH_LONG).show();
                    return;
                }
                if(mManufactDateEdit.getText().toString().length() == 0){
                    Toast.makeText(StockModifyActivity.this, "ManufactDateEdit empty!", Toast.LENGTH_LONG).show();
                    return;
                }
                if(mTransNnumEdit.getText().toString().length() == 0){
                    Toast.makeText(StockModifyActivity.this, "TransNnumEdit empty!", Toast.LENGTH_LONG).show();
                    return;
                }
                if(mNumLotEdit.getText().toString().length() == 0){
                    Toast.makeText(StockModifyActivity.this, "NumLotEdit empty!", Toast.LENGTH_LONG).show();
                    return;
                }
                if(palletTypeSpinner.getSelectedItemId() == -1){
                    Toast.makeText(StockModifyActivity.this, "PalletStatus empty!", Toast.LENGTH_LONG).show();
                    return;
                }
                if(spinner_inventory_status.getSelectedItemId() == -1){
                    Toast.makeText(StockModifyActivity.this, "spinner_inventory_status empty!", Toast.LENGTH_LONG).show();
                    return;
                }
                mProgressBar.setVisibility(View.VISIBLE);
                mSave.setEnabled(false);
                mInboundDateEdit.setEnabled(false);
                mManufactDateEdit.setEnabled(false);
                mTransNnumEdit.setEnabled(false);
                mNumLotEdit.setEnabled(false);
                palletTypeSpinner.setEnabled(false);
                spinner_inventory_status.setEnabled(false);
                mLocation.setEnabled(false);
                mDataRepo = new DataRepo.getData(new DataRepo.onDataListener() {
                    @Override
                    public void returnData(String data) {
                        mProgressBar.setVisibility(View.INVISIBLE);
                        mSave.setEnabled(true);
                        mInboundDateEdit.setEnabled(true);
                        mManufactDateEdit.setEnabled(true);
                        mTransNnumEdit.setEnabled(true);
                        mNumLotEdit.setEnabled(true);
                        palletTypeSpinner.setEnabled(true);
                        spinner_inventory_status.setEnabled(true);
                        mLocation.setEnabled(true);
                        if (!data.isEmpty()) {
                            if(data.contains("[{\"error\":true")) {
                                Toast.makeText(StockModifyActivity.this, "Wrong modify: " + data, Toast.LENGTH_LONG).show();
                                return;
                            } else if(data.contains("{\"data\":\"success\",\"message\":\"Update\"}")) {
                                Toast.makeText(StockModifyActivity.this, "Modify saved: " + model.Initial_PRINTED_LPN, Toast.LENGTH_LONG).show();
                                finish();
                            }
                        }
                    }
                });
                mDataRepo.modifyPallete(item.id,
                        mInboundDateEdit.getText().toString(),
                        mManufactDateEdit.getText().toString(),
                        mTransNnumEdit.getText().toString(),
                        (int)palletTypeSpinner.getSelectedItemId(),
                        (int)spinner_inventory_status.getSelectedItemId(),
                        mNumLotEdit.getText().toString(),
                        item.cell_id);
                mDataRepo.start();
            }
        });

        palletTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        mSave.setEnabled(false);
        mPalletType = Stash.getArrayList("PalletType", PalletType.class);
        if(mPalletType != null && mPalletType.size() != 0) {
            palletTypeSpinner.setAdapter(new PalletTypeAdapter(this,-1,mPalletType));
            int index = -1;
            for(int i1 = 0, cnt1 = mPalletType.size();i1<cnt1;i1++) {
                if(mPalletType.get(i1).id == item.Pallet_Type) {
                    index = i1;
                    break;
                }
            }
            palletTypeSpinner.setSelection(index);
            mSave.setEnabled(mPalletType.size() > 0 && mInventoryStatus.size() > 0);
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
                                palletTypeSpinner.setAdapter(new PalletTypeAdapter(StockModifyActivity.this,-1,mPalletType));
                                Stash.put("PalletType", mPalletType);
                            } catch (JSONException e) {
                                Toast.makeText(StockModifyActivity.this, "Error: not parse PalletType list!", Toast.LENGTH_LONG).show();
                                finish();
                            }

                        } else {
                            Toast.makeText(StockModifyActivity.this, "Error: PalletType list is empty!", Toast.LENGTH_LONG).show();
                            finish();
                        }
                    } else {
                        Toast.makeText(StockModifyActivity.this, "Error: not load PalletType list!", Toast.LENGTH_LONG).show();
                        finish();
                    }
                    mSave.setEnabled(mPalletType.size() > 0 && mInventoryStatus.size() > 0);
                }
            });
            mDataRepo.getPalletType();
            mDataRepo.start();
            mSave.setEnabled(false);
            mProgressBar.setVisibility(View.VISIBLE);
        }

        mInventoryStatus = Stash.getArrayList("InventoryStatus2", InventoryStatus.class);
        if(mInventoryStatus != null && mInventoryStatus.size() != 0) {
            spinner_inventory_status.setAdapter(new InventoryStatusAdapter(this,-1, mInventoryStatus));
            int index = -1;
            for(int i1 = 0, cnt1 = mInventoryStatus.size();i1<cnt1;i1++) {
                if(mInventoryStatus.get(i1).id == item.Item_inventory_status) {
                    index = i1;
                    break;
                }
            }
            spinner_inventory_status.setSelection(index);
            mSave.setEnabled(mPalletType.size() > 0 && mInventoryStatus.size() > 0);
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
                                    String inventory_status = o.getString("inventory_status");
                                    InventoryStatus item = new InventoryStatus(id, inventory_status);
                                    mInventoryStatus.add(item);
                                }
                                spinner_inventory_status.setAdapter(new InventoryStatusAdapter(StockModifyActivity.this,-1, mInventoryStatus));
                                Stash.put("InventoryStatus2", mInventoryStatus);
                            } catch (JSONException e) {
                                Toast.makeText(StockModifyActivity.this, "Error: not parse InventoryStatus list!", Toast.LENGTH_LONG).show();
                                finish();
                            }

                        } else {
                            Toast.makeText(StockModifyActivity.this, "Error: InventoryStatus list is empty!", Toast.LENGTH_LONG).show();
                            finish();
                        }
                    } else {
                        Toast.makeText(StockModifyActivity.this, "Error: not load InventoryStatus list!", Toast.LENGTH_LONG).show();
                        finish();
                    }
                    mSave.setEnabled(mPalletType.size() > 0 && mInventoryStatus.size() > 0);
                }
            });
            mDataRepo.getItemInventoryStatus();
            mDataRepo.start();
        }
        mNumLotEdit = (EditText)findViewById(R.id.num_lot_edit2);
        mTransNnumEdit = (EditText)findViewById(R.id.trans_num_edit2);
        mNumLotEdit.setText(item.Lot_number_batch);
        mTransNnumEdit.setText(item.Transport_Equipment_Number);
        mLocation = (Button)findViewById(R.id.location);
        mLocation.setEnabled(false);
        mLocation.setText(item.cell_name);
        mLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StockModifyActivity.this, SelectCellActivity.class);
                intent.putExtra("item", item);
                intent.putExtra("model", model);
                startActivityForResult(intent, 2);
            }
        });
        loadWrzone(item.item_id);
    }

    private void loadWrzone(int item_id) {
        mProgressBar.setVisibility(View.VISIBLE);
        mDataRepo = new DataRepo.getData(new DataRepo.onDataListener() {
            @Override
            public void returnData(String data) {
                if (!data.isEmpty()) {
                    mProgressBar.setVisibility(View.INVISIBLE);
                    try {
                        JSONObject jsonResponse = new JSONObject(data);
                        item.wrh_zone = jsonResponse.getInt("wrh_zone");
                        mLocation.setEnabled(true);
                    } catch (JSONException e) {
                        Toast.makeText(StockModifyActivity.this, "Error: not load wrh_zone " + e.toString(), Toast.LENGTH_LONG).show();
                        return;
                    }
                }
            }
        });
        mDataRepo.getWrZone(item_id);
        mDataRepo.start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == 2 && resultCode == 0){
            String Location = data.getStringExtra("Location");
            int id = data.getIntExtra("id",-1);
            item.cell_id = id;
            item.cell_name = Location;
            mLocation.setText(item.cell_name);
        } else
            super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Are you sure?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        StockModifyActivity.super.onBackPressed();
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

    private DatePickerDialog.OnDateSetListener mManufactDateEditListener= new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
            mCalendar.set(Calendar.YEAR, year);
            mCalendar.set(Calendar.MONTH, monthOfYear);
            mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            mManufactDateEdit.setText(mSimpleDateFormat.format(mCalendar.getTime()));
        }
    };

    private DatePickerDialog.OnDateSetListener mInboundDateEditListener = new DatePickerDialog.OnDateSetListener(){

        @Override
        public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
            mCalendar.set(Calendar.YEAR, year);
            mCalendar.set(Calendar.MONTH, monthOfYear);
            mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            mInboundDateEdit.setText(mSimpleDateFormat.format(mCalendar.getTime()));
        }
    };

}