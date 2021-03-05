package com.test.test.Activities.Stock;

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

import com.fxn.stash.Stash;
import com.test.test.Adapters.AdapterCells;
import com.test.test.Models.Cell;
import com.test.test.Models.EditableItem;
import com.test.test.Models.ItemModel;
import com.test.test.R;
import com.test.test.Repository.DataRepo;
import com.test.test.Utils.RecyclerItemClickListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class SelectCellActivity extends AppCompatActivity {

    private DataRepo.getData mDataRepo;

    private ProgressBar mProgressBar;
    private RecyclerView mCellsList;
    private EditText mSearchCell;

    private ArrayList<Cell> mCells = new ArrayList<Cell>();
    private ItemModel model;
    private EditableItem item;
    private TextView empty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_cell);
        setResult(-1);
        mSearchCell = (EditText)findViewById(R.id.search_cell);
        mCellsList = (RecyclerView)findViewById(R.id.cells_list);
        item = (EditableItem)getIntent().getParcelableExtra("item");
        model = (ItemModel)getIntent().getSerializableExtra("model");
        empty = findViewById(R.id.empty);
        ((TextView)findViewById(R.id.pallete)).setText(model.Initial_PRINTED_LPN + " current cell " + item.cell_name);
        mProgressBar = (ProgressBar)findViewById(R.id.progress3);
        mProgressBar.setVisibility(View.INVISIBLE);
        mSearchCell.setEnabled(true);
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
                new AlertDialog.Builder(SelectCellActivity.this)
                        .setTitle("Are you sure?")
                        .setMessage("Pallete " + model.Initial_PRINTED_LPN + " change to cell " + cell.Location)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                Intent intent = new Intent();
                                intent.putExtra("Location", cell.Location);
                                intent.putExtra("id", cell.id);
                                setResult(0, intent);
                                finish();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
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
                        AdapterCells adapter = new AdapterCells(SelectCellActivity.this, mCells);
                        adapter.setCurrent(item.cell_id);
                        mCellsList.setAdapter(adapter);
                        int index = -1;
                        for(int i1 = 0,cnt1 = mCells.size();i1<cnt1;i1++){
                            if(item.cell_id == mCells.get(i1).id){
                                index = i1;
                                break;
                            }
                        }
                        mCellsList.scrollToPosition(index);
                        adapter.setMask(mSearchCell.getText().toString());
                        mSearchCell.setEnabled(true);
                    } catch (JSONException e) {
                        Toast.makeText(SelectCellActivity.this, "Error: not load cell list: " + e.toString(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(SelectCellActivity.this, "Error: load cell list empty", Toast.LENGTH_LONG).show();
                }
                mSearchCell.setEnabled(true);
                if(mCells.size() > 0)
                    empty.setVisibility(View.GONE);
                else
                    empty.setVisibility(View.VISIBLE);
            }
        });
        mDataRepo.getCells(1, item.wrh_zone, Stash.getInt("warehouse_id"), like);
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