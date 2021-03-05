package com.test.test.Activities.Inbound;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.fxn.stash.Stash;
import com.test.test.Adapters.AdapterCells;
import com.test.test.Models.Cell;
import com.test.test.Models.ItemModel;
import com.test.test.Models.ListModel;
import com.test.test.R;
import com.test.test.Repository.DataRepo;
import com.test.test.Utils.RecyclerItemClickListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SetPalleteToSelectCellActivity extends AppCompatActivity {

    private DataRepo.getData mDataRepo;

    private ProgressBar mProgressBar;
    private RecyclerView mCellsList;
    private EditText mSearchCell;
    private TextView empty;
    private ArrayList<Cell> mCells = new ArrayList<Cell>();
    private ListModel mListModel;
    private ItemModel mPallete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_cell);
        setResult(-1);
        mSearchCell = (EditText)findViewById(R.id.search_cell);
        mCellsList = (RecyclerView)findViewById(R.id.cells_list);
        mListModel= (ListModel) getIntent().getParcelableExtra("mListModel");
        mPallete = (ItemModel)getIntent().getSerializableExtra("mPallete");
        empty = findViewById(R.id.empty);
        ((TextView)findViewById(R.id.pallete)).setText(mListModel.Inbound_shipment_number + "." + mPallete.item_id + ". " + mPallete.Initial_PRINTED_LPN);
        mProgressBar = (ProgressBar)findViewById(R.id.progress3);
        mSearchCell.setEnabled(true);
        mProgressBar.setVisibility(View.INVISIBLE);
        mSearchCell.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(mSearchCell.getText().toString().length() == 2) {
                    load(mSearchCell.getText().toString());
                } else if(mSearchCell.getText().toString().length() > 2 && ((AdapterCells) mCellsList.getAdapter()) != null) {
                    ((AdapterCells) mCellsList.getAdapter()).setMask(mSearchCell.getText().toString());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        mCellsList.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mCellsList.setLayoutManager(layoutManager);
        mCellsList.addOnItemTouchListener(new RecyclerItemClickListener(this, mCellsList ,new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, final int position) {
                final Cell cell = ((AdapterCells) mCellsList.getAdapter()).getCell(position);
                new AlertDialog.Builder(SetPalleteToSelectCellActivity.this)
                        .setTitle("Are you sure?")
                        .setMessage("Pallete " + mPallete.Initial_PRINTED_LPN +" move in " + cell.Location)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                mDataRepo = new DataRepo.getData(new DataRepo.onDataListener() {
                                    @Override
                                    public void returnData(String data) {
                                        mProgressBar.setVisibility(View.INVISIBLE);
                                        if(data != null) {
                                            if (!data.isEmpty()) {
                                                if (data.indexOf("{\"data\":\"success\"") != -1) {
                                                    if(data.contains("\"putaway_complete\":0")) {
                                                        setResult(1);
                                                        Toast.makeText(SetPalleteToSelectCellActivity.this, "Pallete " + mPallete.Initial_PRINTED_LPN +" added in cell " + cell.Location, Toast.LENGTH_SHORT).show();
                                                    } else {
                                                        Toast.makeText(SetPalleteToSelectCellActivity.this, "PUTAWAY COMPLETE", Toast.LENGTH_SHORT).show();
                                                        setResult(0);
                                                    }
                                                    finish();
                                                    return;
                                                }
                                            } else {
                                                Toast.makeText(SetPalleteToSelectCellActivity.this, "Error added pallete:" + data, Toast.LENGTH_SHORT).show();
                                            }
                                        } else {
                                            Toast.makeText(SetPalleteToSelectCellActivity.this, "Error added pallete!", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                                mProgressBar.setVisibility(View.VISIBLE);
                                mDataRepo.setPalleteInCell(mListModel.id, mPallete.id, cell.id);
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
        }));
        if(mCells.size() > 0)
            empty.setVisibility(View.GONE);
        else
            empty.setVisibility(View.VISIBLE);
    }

    private void load(String like) {
        mSearchCell.setEnabled(false);
        mCells.clear();
        mProgressBar.setVisibility(View.VISIBLE);
        empty.setVisibility(View.GONE);
        mDataRepo = new DataRepo.getData(new DataRepo.onDataListener() {
            @Override
            public void returnData(String data) {
                mProgressBar.setVisibility(View.INVISIBLE);
                if (!data.isEmpty()) {
                    try {
                        JSONObject jsonResponse = new JSONObject(data);
                        JSONArray array = jsonResponse.getJSONArray("rows");
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject varObj = array.getJSONObject(i);
                            Cell model = parseVarObj(varObj);
                            if(model != null){
                                mCells.add(model);
                            }
                        }
                        AdapterCells adapter = new AdapterCells(SetPalleteToSelectCellActivity.this, mCells);
                        mCellsList.setAdapter(adapter);
                        adapter.setMask(mSearchCell.getText().toString());
                        mSearchCell.setEnabled(true);

                    } catch (JSONException e) {
                        Toast.makeText(SetPalleteToSelectCellActivity.this, "Error: not load cell list: " + e.toString(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(SetPalleteToSelectCellActivity.this, "Error: load cell empty", Toast.LENGTH_LONG).show();
                }
                mSearchCell.setEnabled(true);
                if(mCells.size() > 0)
                    empty.setVisibility(View.GONE);
                else
                    empty.setVisibility(View.VISIBLE);
            }
        });
        mDataRepo.getCells(1, mPallete.wrh_zone, 2, Stash.getInt("warehouse_id"), like);
        mDataRepo.start();
    }

    private Cell parseVarObj(JSONObject varObj) throws JSONException {
        int id=-1;
        if(!varObj.isNull("id"))
            id= varObj.getInt("id");
        String Location="";
        if(!varObj.isNull("Location"))
            Location = varObj.getString("Location");
        String Area="";
        if(!varObj.isNull("Area"))
            Area = varObj.getString("Area");
        int Aisle=-1;
        if(!varObj.isNull("Aisle"))
            Aisle= varObj.getInt("Aisle");
        int Row=-1;
        if(!varObj.isNull("Row"))
            Row= varObj.getInt("Row");
        int Section=-1;
        if(!varObj.isNull("Section"))
            Section= varObj.getInt("Section");
        int Level=-1;
        if(!varObj.isNull("Level"))
            Level= varObj.getInt("Level");
        int Positon=-1;
        if(!varObj.isNull("Positon"))
            Positon= varObj.getInt("Positon");
        int Max_Hight_cm=-1;
        if(!varObj.isNull("Max_Hight_cm"))
            Max_Hight_cm= varObj.getInt("Max_Hight_cm");
        int Max_Width_cm=-1;
        if(!varObj.isNull("Max_Width_cm"))
            Max_Width_cm= varObj.getInt("Max_Width_cm");
        int Height_from_floor_m=-1;
        if(!varObj.isNull("Height_from_floor_m"))
            Height_from_floor_m= varObj.getInt("Height_from_floor_m");
        int Max_Weight_kg=-1;
        if(!varObj.isNull("Max_Weight_kg"))
            Max_Weight_kg= varObj.getInt("Max_Weight_kg");
        String Cell_Type="";
        if(!varObj.isNull("Cell_Type"))
            Cell_Type = varObj.getString("Cell_Type");
        String WrhZone="";
        if(!varObj.isNull("WrhZone"))
            WrhZone = varObj.getString("WrhZone");
        int Deep_level=-1;
        if(!varObj.isNull("Deep_level"))
            Deep_level= varObj.getInt("Deep_level");
        int pallet_type=-1;
        if(!varObj.isNull("pallet_type"))
            pallet_type= varObj.getInt("pallet_type");
        String Warehouse="";
        if(!varObj.isNull("Warehouse"))
            Warehouse = varObj.getString("Warehouse");
        Cell cell = new Cell(
                id,
                Location,
                Area,
                Aisle,
                Row,
                Section,
                Level,
                Positon,
                Max_Hight_cm,
                Max_Width_cm,
                Height_from_floor_m,
                Max_Weight_kg,
                Cell_Type,
                WrhZone,
                Deep_level,
                pallet_type,
                Warehouse);
        return cell;

    }
}