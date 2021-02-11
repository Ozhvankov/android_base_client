package com.test.test.Activities.Stock;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.fxn.stash.Stash;
import com.test.test.Activities.Inbound.DetailActivity;
import com.test.test.Adapters.PalletTypeAdapter;
import com.test.test.Models.EditableItem;
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
    private Spinner palletTypeSpinner;
    private EditText mNumLotEdit;
    private EditText mTransNnumEdit;
    private EditText mPalletStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_modify);
        final EditableItem item = getIntent().getParcelableExtra("item");
        ItemModel model = (ItemModel)getIntent().getSerializableExtra("mItemModel");
        palletTypeSpinner = (Spinner)findViewById(R.id.pallet_type_spinner2);
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
                if(mPalletStatus.getText().toString().length() == 0){
                    Toast.makeText(StockModifyActivity.this, "PalletStatus empty!", Toast.LENGTH_LONG).show();
                    return;
                }
                mProgressBar.setVisibility(View.VISIBLE);
                mSave.setEnabled(false);
                mDataRepo = new DataRepo.getData(new DataRepo.onDataListener() {
                    @Override
                    public void returnData(String data) {
                        mProgressBar.setVisibility(View.INVISIBLE);
                        mSave.setEnabled(true);
                        if (!data.isEmpty()) {
                            if(data.contains("[{\"error\":true")) {
                                Toast.makeText(StockModifyActivity.this, "Wrong modify", Toast.LENGTH_LONG).show();
                                return;
                            }

                        }
                    }
                });
                mDataRepo.modifyPallete(item.id,
                        mInboundDateEdit.getText().toString(),
                        mManufactDateEdit.getText().toString(),
                        mTransNnumEdit.getText().toString(),
                        (int)palletTypeSpinner.getSelectedItemId(),
                        mNumLotEdit.getText().toString(),
                        mPalletStatus.getText().toString());
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



        mPalletType = Stash.getArrayList("PalletType", PalletType.class);
        if(mPalletType != null && mPalletType.size() != 0) {
            palletTypeSpinner.setAdapter(new PalletTypeAdapter(this,-1,mPalletType));
            int index = -1;
            for(int i1 = 0, cnt1 = mPalletType.size();i1<cnt1;i1++) {
                if(mPalletType.get(i1).id == model.Pallet_Type) {
                    index = i1;
                    break;
                }
            }
            palletTypeSpinner.setSelection(index);
        } else {
            mDataRepo = new DataRepo.getData(new DataRepo.onDataListener() {

                @Override
                public void returnData(String data) {
                    mProgressBar.setVisibility(View.INVISIBLE);
                    mSave.setEnabled(true);
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
                }
            });
            mDataRepo.getPalletType();
            mDataRepo.start();
            mSave.setEnabled(false);
            mProgressBar.setVisibility(View.VISIBLE);
        }

        mNumLotEdit = (EditText)findViewById(R.id.num_lot_edit2);
        mTransNnumEdit = (EditText)findViewById(R.id.trans_num_edit2);
        mPalletStatus = (EditText)findViewById(R.id.pallet_status);
        mNumLotEdit.setText(model.Lot_number_batch);
        mTransNnumEdit.setText(model.Transport_Equipment_Number);
        mPalletStatus.setText(model.inventory_status);
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