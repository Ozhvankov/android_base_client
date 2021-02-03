package com.test.test.Fragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.test.test.Models.ItemModel;
import com.test.test.R;
import com.test.test.Repository.DataRepo;

import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;


public class InboundDetailsFragment extends Fragment {

    private ItemModel mItemModel;

    private View mBaseView;
    private setResponse mSetResponse;
    private AutoCompleteTextView mDestinationLocationAutocomplete;
    private ProgressBar mProgressBar;
    private HashMap<String, String> mListMap;
    private boolean mIsLoaded = false;
    private SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private Calendar mCalendar = Calendar.getInstance();

    DataRepo.getData getData;

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
    private Button mInboundDateEdit;
    private Button mManufactDateEdit;

    public InboundDetailsFragment() {
        super();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mItemModel = (ItemModel) getArguments().getSerializable("data");
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        mBaseView = inflater.inflate(R.layout.layout_input_fact, container, false);
        TextView footprintTxt = mBaseView.findViewById(R.id.footprint_text);
        TextView print_lpn_text = mBaseView.findViewById(R.id.print_lpn_text);
        TextView lifedaysTxt = mBaseView.findViewById(R.id.lifedays_text);
        TextView implementPeriodTxt = mBaseView.findViewById(R.id.impl_period_text);
        TextView planWeightTxt = mBaseView.findViewById(R.id.plan_weight_text);
        TextView planBoxTxt = mBaseView.findViewById(R.id.plan_box_text);
        TextView pageTxt = mBaseView.findViewById(R.id.page_text);
        TextView ariclesTxt = mBaseView.findViewById(R.id.articles_text);


        final EditText factWeightEdit = mBaseView.findViewById(R.id.gross_edit);
        InputMethodManager imm = (InputMethodManager) Objects.requireNonNull(getActivity()).getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        factWeightEdit.requestFocus();

        final EditText factBoxEdit = mBaseView.findViewById(R.id.fact_box_edit);
        final EditText factEmptyBoxEdit = mBaseView.findViewById(R.id.fact_emptybox_edit);
        final EditText factEmptyPalletEdit = mBaseView.findViewById(R.id.fact_emptypallet_edit);
        mManufactDateEdit = (Button) mBaseView.findViewById(R.id.manufact_date_edit);
        mInboundDateEdit = (Button) mBaseView.findViewById(R.id.inb_date_edit);
        final EditText numOfPartyEdit = mBaseView.findViewById(R.id.num_party_edit);
        final EditText lotNumEdit = mBaseView.findViewById(R.id.num_lot_edit);
        final EditText transEqNumEdit = mBaseView.findViewById(R.id.trans_num_edit);
        final EditText palletTypeEdit = mBaseView.findViewById(R.id.pallet_type_edit);

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

                new DatePickerDialog(getContext(), mManufactDateEditListener,
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

                new DatePickerDialog(getContext(), mInboundDateEditListener,
                        mCalendar.get(Calendar.YEAR),
                        mCalendar.get(Calendar.MONTH),
                        mCalendar.get(Calendar.DAY_OF_MONTH))
                        .show();
            }
        });


        Button saveBtn = mBaseView.findViewById(R.id.save_btn);
        ariclesTxt.setText(mItemModel.item_article);
        lifedaysTxt.setText(mItemModel.shelf_life_days == -1 ? "-" : String.valueOf(mItemModel.shelf_life_days));
        footprintTxt.setText(mItemModel.footprint_name);
        print_lpn_text.setText(mItemModel.Initial_PRINTED_LPN);
        implementPeriodTxt.setText(mItemModel.Implementation_period == -1 ? "-" : String.valueOf(mItemModel.Implementation_period));
        planWeightTxt.setText(mItemModel.plan_item_weight == -1 ? "" : String.valueOf(mItemModel.plan_item_weight));
        planBoxTxt.setText(String.valueOf(mItemModel.plan_item_box));
        factWeightEdit.setText(mItemModel.fact_item_weight == -1 ? "" : String.valueOf(mItemModel.fact_item_weight));
        factBoxEdit.setText(mItemModel.fact_item_box == -1 ? "" : String.valueOf(mItemModel.fact_item_box));
        factEmptyBoxEdit.setText(mItemModel.fact_weight_empty_box == -1 ? "" : String.valueOf(mItemModel.fact_weight_empty_box));
        factEmptyPalletEdit.setText(mItemModel.fact_weight_empty_pallet);
        Date Manufacturing_Date = new Date();
        if (mItemModel.Manufacturing_Date.equals("0000-00-00")) {
            mManufactDateEdit.setText(mSimpleDateFormat.format(Manufacturing_Date));
        } else {
            mManufactDateEdit.setText(mItemModel.Manufacturing_Date);
        }
        Date inbound_date = new Date();
        if (mItemModel.inbound_date.equals("0000-00-00")) {
            mInboundDateEdit.setText(mSimpleDateFormat.format(inbound_date));
        } else {
            mInboundDateEdit.setText(mItemModel.inbound_date);
        }
        numOfPartyEdit.setText(mItemModel.number_party);
        lotNumEdit.setText(String.valueOf(mItemModel.Lot_number_batch));
        transEqNumEdit.setText(String.valueOf(mItemModel.Transport_Equipment_Number));
        palletTypeEdit.setText(mItemModel.Pallet_Type);

        pageTxt.setText(String.valueOf(getArguments().getInt("page") + 1));

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String, String> hashMap = new HashMap<>();

                hashMap.put("data[" + mItemModel.id + "][fact_item_weight]", factWeightEdit.getText().toString());
                hashMap.put("data[" + mItemModel.id + "][fact_item_box]", factBoxEdit.getText().toString());
                hashMap.put("data[" + mItemModel.id + "][fact_weight_empty_box]",
                        factEmptyBoxEdit.getText().toString());
                hashMap.put("data[" + mItemModel.id + "][fact_weight_empty_pallet]",
                        factEmptyPalletEdit.getText().toString());
                hashMap.put("data[" + mItemModel.id + "][Manufacturing_Date]",
                        mManufactDateEdit.getText().toString());
                hashMap.put("data[" + mItemModel.id + "][inbound_date]", mInboundDateEdit.getText().toString());
                hashMap.put("data[" + mItemModel.id + "][number_party]", numOfPartyEdit.getText().toString());
                hashMap.put("data[" + mItemModel.id + "][Lot_number_batch]", lotNumEdit.getText().toString());
                hashMap.put("data[" + mItemModel.id + "][Transport_Equipment_Number]",
                        transEqNumEdit.getText().toString());
                hashMap.put("data[" + mItemModel.id + "][Pallet_Type]", palletTypeEdit.getText().toString());
                mSetResponse.set(hashMap);
                if (getArguments() != null && getArguments().getBoolean("last_frag")) {
                    mSetResponse.send("fact");
                }
            }
        });

        return mBaseView;
    }

    public void updateCellList(HashMap<String, String> cellList) {
        mIsLoaded = true;
        mProgressBar.setVisibility(View.GONE);
        this.mListMap = cellList;
        List<String> cellLocs = new ArrayList<>(cellList.keySet());
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(Objects.requireNonNull(getContext()),
                android.R.layout.simple_dropdown_item_1line, cellLocs);
        mDestinationLocationAutocomplete.setAdapter(arrayAdapter);
    }


    public interface setResponse {
        void set(HashMap<String, String> response);
        void send(String action);
    }

}
