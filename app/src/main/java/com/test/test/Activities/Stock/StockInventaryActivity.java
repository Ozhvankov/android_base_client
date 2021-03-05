package com.test.test.Activities.Stock;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.test.test.Models.EditableItem;
import com.test.test.Models.ItemModel;
import com.test.test.R;
import com.test.test.Repository.DataRepo;

public class StockInventaryActivity extends AppCompatActivity {

    private EditText kg_current_edit;
    private EditText box_current_edit;
    private TextView mInboundTxt;
    private TextView mSupplierTxt;
    private TextView mClientTxt;
    private TextView mItemsTxt;private TextView mArticlesTxt;
    private TextView kg_avaible;
    private TextView kg_reserved;

    private TextView box_avaible;
    private TextView box_reserved;
    private Button mSave;

    private DataRepo.getData mDataRepo;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_inventary);
        final EditableItem item = getIntent().getParcelableExtra("item");
        final ItemModel model = (ItemModel)getIntent().getSerializableExtra("mItemModel");
        TextView lpn = findViewById(R.id.lpn_text);
        lpn.setText(model.Initial_PRINTED_LPN);
        kg_current_edit = (EditText)findViewById(R.id.kg_current_edit);
        box_current_edit = (EditText)findViewById(R.id.box_current_edit);
        kg_current_edit.setText(String.valueOf(item.kg_current));
        box_current_edit.setText(String.valueOf(item.box_current));

        mInboundTxt = (TextView)findViewById(R.id.inboundShipmentTxt);
        mSupplierTxt = (TextView)findViewById(R.id.supplier_text);
        mClientTxt = (TextView)findViewById(R.id.client_text);
        mItemsTxt = (TextView)findViewById(R.id.items_text);
        mArticlesTxt = (TextView)findViewById(R.id.articles_text);
        kg_avaible = (TextView)findViewById(R.id.kg_avaible);
        kg_reserved = (TextView)findViewById(R.id.kg_reserved);
        box_avaible = (TextView)findViewById(R.id.box_avaible);
        box_reserved = (TextView)findViewById(R.id.box_reserved);

        mInboundTxt.setText(String.valueOf(item.Inbound_shipment_number));
        mSupplierTxt.setText(String.valueOf(item.Supplier));
        mClientTxt.setText(String.valueOf(item.Client));
        mItemsTxt.setText(String.valueOf(item.Item_No));
        kg_avaible.setText(String.valueOf(item.kg_avaible));
        kg_reserved.setText(String.valueOf(item.kg_reserved));
        box_avaible.setText(String.valueOf(item.box_avaible));
        box_reserved.setText(String.valueOf(item.box_reserved));
        mSave = findViewById(R.id.save_btn);
        mProgressBar = findViewById(R.id.progress);
        mProgressBar.setVisibility(View.INVISIBLE);
        //inventaryPallete
        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(kg_current_edit.getText().toString().length() == 0){
                    Toast.makeText(StockInventaryActivity.this, "kg_current empty!", Toast.LENGTH_LONG).show();
                    return;
                }
                if(box_current_edit.getText().toString().length() == 0){
                    Toast.makeText(StockInventaryActivity.this, "box_current empty!", Toast.LENGTH_LONG).show();
                    return;
                }
                mProgressBar.setVisibility(View.VISIBLE);
                mSave.setEnabled(false);
                box_current_edit.setEnabled(false);
                kg_current_edit.setEnabled(false);

                mDataRepo = new DataRepo.getData(new DataRepo.onDataListener() {
                    @Override
                    public void returnData(String data) {
                        mProgressBar.setVisibility(View.INVISIBLE);
                        mSave.setEnabled(true);
                        box_current_edit.setEnabled(true);
                        kg_current_edit.setEnabled(true);
                        if (!data.isEmpty()) {
                            if(data.contains("[{\"error\":true")) {
                                Toast.makeText(StockInventaryActivity.this, "Wrong inventary: " + data, Toast.LENGTH_LONG).show();
                                return;
                            } else if(data.contains("{\"data\":\"success\",\"message\":\"Update\"}")) {
                                Toast.makeText(StockInventaryActivity.this, "Inventary saved: " + model.Initial_PRINTED_LPN, Toast.LENGTH_LONG).show();
                                finish();
                            }
                        }
                    }
                });
                mDataRepo.inventaryPallete(item.id,
                        Integer.valueOf(kg_current_edit.getText().toString()),
                        Integer.valueOf(box_current_edit.getText().toString()));
                mDataRepo.start();
            }
        });
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Are you sure?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        StockInventaryActivity.super.onBackPressed();
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