package com.test.test.Activities.Inbound;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Spinner;

import com.test.test.Models.Item;
import com.test.test.R;

import java.util.ArrayList;

public class AddPalletActivity extends AppCompatActivity {

    private ArrayList<Item> mItems = new ArrayList<Item>();

    private EditText packaging_wight_edit;
    private EditText pallet_wight_edit;
    private Spinner spinner_items;
    private Spinner spinner_footprint;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pallet);
        packaging_wight_edit = findViewById(R.id.packaging_wight_edit) ;
        pallet_wight_edit = findViewById(R.id.pallet_wight_edit);
        spinner_items = findViewById(R.id.spinner_items);
        spinner_footprint = findViewById(R.id.spinner_footprint);
        mItems = getIntent().getParcelableExtra("items");
    }
}