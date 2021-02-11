package com.test.test.Activities.Stock;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;

import com.test.test.Models.EditableItem;
import com.test.test.Models.ItemModel;
import com.test.test.R;

public class StockInventaryActivity extends AppCompatActivity {

    private EditText kg_current_edit;
    private EditText box_current_edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_inventary);
        final EditableItem item = getIntent().getParcelableExtra("item");
        ItemModel model = (ItemModel)getIntent().getSerializableExtra("mItemModel");
        kg_current_edit = (EditText)findViewById(R.id.kg_current_edit);
        box_current_edit = (EditText)findViewById(R.id.box_current_edit);
        kg_current_edit.setText(String.valueOf(item.kg_current));
        box_current_edit.setText(String.valueOf(item.box_current));
    }
}