package com.test.test.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.test.test.Models.ItemModel;
import com.test.test.R;
import com.test.test.Repository.DataRepo;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;


public class DetailsFragment extends Fragment {

    private ItemModel mItemModel;

    private View mBaseView;
    private setResponse mSetResponse;
    private AutoCompleteTextView mDestinationLocationAutocomplete;
    private ProgressBar mProgressBar;
    private HashMap<String, String> mListMap;
    private boolean mIsLoaded = false;

    DataRepo.getData getData;

    public DetailsFragment(setResponse setResponse) {
        this.mSetResponse = setResponse;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        if (getArguments() != null) {
            mItemModel = (ItemModel) getArguments().getSerializable("data");

            if (Objects.equals(getArguments().getString("status_id"), "0")) {

                mBaseView = inflater.inflate(R.layout.layout_input_fact, container, false);
                TextView footprintTxt = mBaseView.findViewById(R.id.footprint_text);
                TextView initialTxt = mBaseView.findViewById(R.id.initial_text);
                TextView lifedaysTxt = mBaseView.findViewById(R.id.lifedays_text);
                TextView implementPeriodTxt = mBaseView.findViewById(R.id.impl_period_text);
                TextView planWeightTxt = mBaseView.findViewById(R.id.plan_weight_text);
                TextView planBoxTxt = mBaseView.findViewById(R.id.plan_box_text);
                TextView pageTxt = mBaseView.findViewById(R.id.page_text);
                TextView ariclesTxt = mBaseView.findViewById(R.id.articles_text);

                final EditText factWeightEdit = mBaseView.findViewById(R.id.fact_weight_edit);
                InputMethodManager imm = (InputMethodManager) Objects.requireNonNull(getActivity()).getSystemService(
                        Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                factWeightEdit.requestFocus();

                final EditText factBoxEdit = mBaseView.findViewById(R.id.fact_box_edit);
                final EditText factEmptyBoxEdit = mBaseView.findViewById(R.id.fact_emptybox_edit);
                final EditText factEmptyPalletEdit = mBaseView.findViewById(R.id.fact_emptypallet_edit);
                final EditText manufactDateEdit = mBaseView.findViewById(R.id.manufact_date_edit);
                final EditText inboundDateEdit = mBaseView.findViewById(R.id.inb_date_edit);
                final EditText numOfPartyEdit = mBaseView.findViewById(R.id.num_party_edit);
                final EditText lotNumEdit = mBaseView.findViewById(R.id.num_lot_edit);
                final EditText transEqNumEdit = mBaseView.findViewById(R.id.trans_num_edit);
                final EditText palletTypeEdit = mBaseView.findViewById(R.id.pallet_type_edit);
                Button saveBtn = mBaseView.findViewById(R.id.save_btn);

                ariclesTxt.setText(mItemModel.item_article);
                lifedaysTxt.setText(mItemModel.shelf_life_days);
                footprintTxt.setText(mItemModel.footprint_name);
                initialTxt.setText(mItemModel.initial_PRINTED_LPN);
                implementPeriodTxt.setText(mItemModel.implementation_period);
                planWeightTxt.setText(mItemModel.plan_item_weight);
                planBoxTxt.setText(mItemModel.plan_item_box);
                factWeightEdit.setText(mItemModel.fact_item_weight);
                factBoxEdit.setText(mItemModel.fact_item_box);
                factEmptyBoxEdit.setText(mItemModel.fact_weight_empty_box);
                factEmptyPalletEdit.setText(mItemModel.fact_weight_empty_pallet);
                manufactDateEdit.setText(mItemModel.manufacturing_Date);
                inboundDateEdit.setText(mItemModel.inbound_date);
                numOfPartyEdit.setText(mItemModel.number_party);
                lotNumEdit.setText(mItemModel.lot_number_batch);
                transEqNumEdit.setText(mItemModel.transport_Equipment_Number);
                palletTypeEdit.setText(mItemModel.pallet_Type);

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
                                manufactDateEdit.getText().toString());
                        hashMap.put("data[" + mItemModel.id + "][inbound_date]", inboundDateEdit.getText().toString());
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

            } else if (Objects.equals(getArguments().getString("status_id"), "2")) {

                mBaseView = inflater.inflate(R.layout.layout_put_away, container, false);
                TextView footprintTxt = mBaseView.findViewById(R.id.footprint_text);
                TextView initialTxt = mBaseView.findViewById(R.id.initial_text);
                TextView articleTxt = mBaseView.findViewById(R.id.articles_text);
                TextView pageTxt = mBaseView.findViewById(R.id.page_text);
                TextView lifeDaysTxt = mBaseView.findViewById(R.id.lifedays_text);
                TextView implementPeriodTxt = mBaseView.findViewById(R.id.impl_period_text);
                TextView planWeightTxt = mBaseView.findViewById(R.id.plan_weight_text);
                TextView planBoxTxt = mBaseView.findViewById(R.id.plan_box_text);
                TextView factWeightTxt = mBaseView.findViewById(R.id.fact_weight_text);
                TextView factBoxTxt = mBaseView.findViewById(R.id.fact_box_text);
                TextView factEmptyBoxTxt = mBaseView.findViewById(R.id.fact_emptybox_text);
                TextView factEmptyPalletTxt = mBaseView.findViewById(R.id.fact_emptypallet_text);
                TextView manufactDateTxt = mBaseView.findViewById(R.id.manufact_date_text);
                TextView inbDateTxt = mBaseView.findViewById(R.id.inb_date_text);
                TextView numPartyTxt = mBaseView.findViewById(R.id.num_party_edit);
                TextView numLotTxt = mBaseView.findViewById(R.id.num_lot_text);
                TextView transNumTxt = mBaseView.findViewById(R.id.trans_num_text);
                TextView palletTypeTxt = mBaseView.findViewById(R.id.pallet_type_text);
                mProgressBar = mBaseView.findViewById(R.id.progress);
                mDestinationLocationAutocomplete = mBaseView.findViewById(R.id.location_autocomplete);
                final Button saveBtn = mBaseView.findViewById(R.id.save_btn);

                footprintTxt.setText(mItemModel.footprint_name);
                initialTxt.setText(mItemModel.initial_PRINTED_LPN);
                articleTxt.setText(mItemModel.item_article);
                lifeDaysTxt.setText(mItemModel.shelf_life_days);
                implementPeriodTxt.setText(mItemModel.implementation_period);
                planWeightTxt.setText(mItemModel.plan_item_weight);
                planBoxTxt.setText(mItemModel.plan_item_box);
                factWeightTxt.setText(mItemModel.fact_item_weight);
                factBoxTxt.setText(mItemModel.fact_item_box);
                factEmptyBoxTxt.setText(mItemModel.fact_weight_empty_box);
                factEmptyPalletTxt.setText(mItemModel.fact_weight_empty_pallet);
                manufactDateTxt.setText(mItemModel.manufacturing_Date);
                inbDateTxt.setText(mItemModel.inbound_date);
                numPartyTxt.setText(mItemModel.number_party);
                numLotTxt.setText(mItemModel.lot_number_batch);
                transNumTxt.setText(mItemModel.transport_Equipment_Number);
                palletTypeTxt.setText(mItemModel.pallet_Type);
                pageTxt.setText(String.valueOf(getArguments().getInt("page") + 1));

                saveBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (mIsLoaded) {
                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("data[" + mItemModel.id + "][cell_id]",
                                    mListMap.get(mDestinationLocationAutocomplete.getText().toString()));
                            mSetResponse.set(hashMap);
                            //Toast.makeText(getContext(), "Saved!", Toast.LENGTH_SHORT).show();
                            if (getArguments() != null && getArguments().getBoolean("last_frag")) {
                                mSetResponse.send("putaway");
                            }
                        } else {
                            Toast.makeText(getContext(), "Wait till list load", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }
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
