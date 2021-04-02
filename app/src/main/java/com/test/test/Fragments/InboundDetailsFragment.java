package com.test.test.Fragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.test.test.Adapters.PalletTypeAdapter;
import com.test.test.Models.ItemModel;
import com.test.test.Models.ListModel;
import com.test.test.Models.PalletType;
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
    private ListModel mListModel;

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
    private EditText factBoxEdit;

    private EditText factEmptyBoxEdit;
    private EditText factEmptyPalletEdit;
    private EditText numOfPartyEdit;
    private EditText lotNumEdit;
    private EditText transEqNumEdit;
    private Spinner palletTypeSpinner;
    private EditText factWeightEdit;
    private EditText Fact_NETTO_weight;
    private MyTextWatcher mMyTextWatcherFact_NETTO_weight;
    private MyTextWatcher mMyTextWatcherfactWeightEdit;
    private MyTextWatcher MyTextWatcherfactBoxEdit;
    private MyTextWatcher MyTextWatcherfactEmptyBoxEdit;
    private MyTextWatcher MyTextWatcherfactEmptyPalletEdit;
    private MyTextWatcher MyTextWatchermManufactDateEdit;
    private MyTextWatcher MyTextWatchermInboundDateEdit;
    private MyTextWatcher MyTextWatchernumOfPartyEdit;
    private MyTextWatcher MyTextWatcherlotNumEdit;
    private MyTextWatcher MyTextWatchertransEqNumEdit;
    private ArrayList<PalletType> mPalleteType;
    private DataRepo.getData mDataRepo;
    private int mListId;
    private EditText staging_location;

    public static class MyTextWatcher implements TextWatcher {
        private Button mEditButton;
        private ItemModel mItemModel;
        private EditText mEditText;

        public MyTextWatcher(EditText editText, ItemModel itemModel) {
            mEditText = editText;
            mItemModel = itemModel;
        }
        public MyTextWatcher(Button editText, ItemModel itemModel) {
            mEditButton = editText;
            mItemModel = itemModel;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            if(mEditButton == null) {
                switch (mEditText.getId()) {
                    case R.id.gross_edit:
                        if (mEditText.getText().toString().length() == 0)
                            mItemModel.fact_item_weight = -1.0;
                        else
                            mItemModel.fact_item_weight = Double.valueOf(mEditText.getText().toString());
                        break;
                    case R.id.fact_box_edit:
                        if (mEditText.getText().toString().length() == 0)
                            mItemModel.fact_item_box = -1;
                        else
                            mItemModel.fact_item_box = Integer.valueOf(mEditText.getText().toString());
                        break;
                    case R.id.fact_emptybox_edit:
                        if (mEditText.getText().toString().length() == 0)
                            mItemModel.fact_weight_empty_box = -1.0;
                        else
                            mItemModel.fact_weight_empty_box = Double.valueOf(mEditText.getText().toString());
                        break;
                    case R.id.fact_emptypallet_edit:
                        mItemModel.fact_weight_empty_pallet = mEditText.getText().toString();
                        break;
                    case R.id.num_party_edit:
                        mItemModel.number_party = mEditText.getText().toString();
                        break;
                    case R.id.num_lot_edit:
                        if (mEditText.getText().toString().length() == 0)
                            mItemModel.Lot_number_batch = "";
                        else
                            mItemModel.Lot_number_batch = mEditText.getText().toString();
                        break;
                    case R.id.trans_num_edit:
                        if (mEditText.getText().toString().length() == 0)
                            mItemModel.Transport_Equipment_Number = "";
                        else
                            mItemModel.Transport_Equipment_Number = mEditText.getText().toString();
                        break;
                    case R.id.Fact_NETTO_weight:
                        if (mEditText.getText().toString().length() == 0)
                            mItemModel.netto = 1;
                        else
                            mItemModel.netto = Double.valueOf(mEditText.getText().toString());
                    case R.id.staging_location:
                        mItemModel.staging_location = mEditText.getText().toString();
                }
            } else {
                switch (mEditButton.getId()) {
                    case R.id.manufact_date_edit:
                        mItemModel.Manufacturing_Date = mEditButton.getText().toString();
                        break;
                    case R.id.inb_date_edit:
                        mItemModel.inbound_date = mEditButton.getText().toString();
                        break;
                }
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    }

    public InboundDetailsFragment() {
        super();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mItemModel = (ItemModel) getArguments().getSerializable("data");
        mPalleteType = getArguments().getParcelableArrayList("pallete_types");
        mListModel = getArguments().getParcelable("mListModel");
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        mBaseView = inflater.inflate(R.layout.layout_input_fact, container, false);
        TextView items = mBaseView.findViewById(R.id.items_text);
        TextView footprintTxt = mBaseView.findViewById(R.id.footprint_text);
        TextView print_lpn_text = mBaseView.findViewById(R.id.print_lpn_text);
        TextView planWeightTxt = mBaseView.findViewById(R.id.plan_weight_text);
        TextView planBoxTxt = mBaseView.findViewById(R.id.plan_box_text);
        TextView pageTxt = mBaseView.findViewById(R.id.page_text);
        TextView ariclesTxt = mBaseView.findViewById(R.id.articles_text);
        final Button saveBtn = mBaseView.findViewById(R.id.save_btn);

        factWeightEdit = mBaseView.findViewById(R.id.gross_edit);
        InputMethodManager imm = (InputMethodManager) Objects.requireNonNull(getActivity()).getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        factWeightEdit.requestFocus();

        factBoxEdit = mBaseView.findViewById(R.id.fact_box_edit);
        factEmptyBoxEdit = mBaseView.findViewById(R.id.fact_emptybox_edit);
        factEmptyPalletEdit = mBaseView.findViewById(R.id.fact_emptypallet_edit);
        mManufactDateEdit = (Button) mBaseView.findViewById(R.id.manufact_date_edit);
        mInboundDateEdit = (Button) mBaseView.findViewById(R.id.inb_date_edit);
        numOfPartyEdit = mBaseView.findViewById(R.id.num_party_edit);
        lotNumEdit = mBaseView.findViewById(R.id.num_lot_edit);
        transEqNumEdit = mBaseView.findViewById(R.id.trans_num_edit);
        Fact_NETTO_weight = mBaseView.findViewById(R.id.Fact_NETTO_weight);
        palletTypeSpinner = mBaseView.findViewById(R.id.pallet_type_spinner);
        staging_location = (EditText)mBaseView.findViewById(R.id.staging_location);

        palletTypeSpinner.setAdapter(new PalletTypeAdapter(getActivity(),-1,mPalleteType));
        palletTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mItemModel.Pallet_Type = (int)l;
                saveBtn.requestFocus();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
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
        items.setText(mItemModel.item_no);
        ariclesTxt.setText(mItemModel.item_article);
        footprintTxt.setText(mItemModel.footprint_name);
        print_lpn_text.setText(mItemModel.Initial_PRINTED_LPN);
        planWeightTxt.setText(mItemModel.plan_item_weight == -1 ? "" : String.valueOf(mItemModel.plan_item_weight));
        planBoxTxt.setText(String.valueOf(mItemModel.plan_item_box));
        factWeightEdit.setText(mItemModel.fact_item_weight == -1 ? "" : String.valueOf(mItemModel.fact_item_weight));
        factBoxEdit.setText(mItemModel.fact_item_box == -1 ? "" : String.valueOf(mItemModel.fact_item_box));
        factEmptyBoxEdit.setText(mItemModel.fact_weight_empty_box == -1 ? "0" : String.valueOf(mItemModel.fact_weight_empty_box));
        factEmptyPalletEdit.setText(mItemModel.fact_weight_empty_pallet.length() == 0 ? "0" : mItemModel.fact_weight_empty_pallet);
        if(mListModel.netto_flag_insert == 0) {
            Fact_NETTO_weight.setVisibility(View.GONE);
            mBaseView.findViewById(R.id.textView63).setVisibility(View.GONE);
        } else {
            String n= "";
            if(mListModel.netto_flag_insert == 1) {
                if(mItemModel.netto != 0.0)
                    n = String.valueOf(mItemModel.netto);
            }
            Fact_NETTO_weight.setText(n);
        }
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
        lotNumEdit.setText(mItemModel.Lot_number_batch);
        if(mItemModel.staging_location.length() != 0){
            staging_location.setEnabled(false);
            if(mItemModel.Lot_number_batch.length() != 0) {
                transEqNumEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_DONE) {
                            transEqNumEdit.clearFocus();
                            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                            palletTypeSpinner.performClick();
                            return true;
                        }
                        return false;
                    }
                });
            }
        } else {
            staging_location.setEnabled(true);
            staging_location.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        staging_location.clearFocus();
                        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                        palletTypeSpinner.performClick();
                        return true;
                    }
                    return false;
                }
            });
        }
        staging_location.setText(mItemModel.staging_location);
        if(mItemModel.Lot_number_batch.length() != 0) {
            lotNumEdit.setEnabled(false);
        } else {
            lotNumEdit.setEnabled(true);
            lotNumEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        lotNumEdit.clearFocus();
                        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                        palletTypeSpinner.performClick();
                        return true;
                    }
                    return false;
                }
            });
        }
        transEqNumEdit.setText(String.valueOf(mItemModel.Transport_Equipment_Number));
        for(int i1= 0; i1 < mPalleteType.size(); i1++) {
            int id = -1;
            if(mItemModel.Pallet_Type > 0)
                id = mItemModel.Pallet_Type;
            if(mPalleteType.get(i1).id == id)
                palletTypeSpinner.setSelection(i1);
        }

        addTextWatchers();

        pageTxt.setText(String.valueOf(getArguments().getInt("page") + 1) + " of " + String.valueOf(getArguments().getInt("count")));

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (factWeightEdit.getText().toString().length() == 0) {
                    Toast.makeText(getContext(), "fact_item_weight is empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (factBoxEdit.getText().toString().length() == 0) {
                    Toast.makeText(getContext(), "fact_item_box is empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(mListModel.netto_flag_insert == 1) {
                    if (Fact_NETTO_weight.getText().toString().length() == 0) {
                        Toast.makeText(getContext(), "Fact_NETTO_weight is empty", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } else {
                    if(factEmptyBoxEdit.getText().toString().length() == 0) {
                        Toast.makeText(getContext(), "fact_weight_empty_box is empty", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(factEmptyPalletEdit.getText().toString().length() == 0) {
                        Toast.makeText(getContext(), "fact_weight_empty_pallet is empty", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                if(mManufactDateEdit.getText().toString().length() == 0) {
                    Toast.makeText(getContext(), "Manufacturing_Date is empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(numOfPartyEdit.getText().toString().length() == 0) {
                    Toast.makeText(getContext(), "number_party is empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(lotNumEdit.getText().toString().length() == 0) {
                    Toast.makeText(getContext(), "Lot_number_batch is empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(transEqNumEdit.getText().toString().length() == 0) {
                    Toast.makeText(getContext(), "Transport_Equipment_Number is empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(staging_location.getText().toString().length() == 0) {
                    Toast.makeText(getContext(), "staging_location is empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                HashMap<String, String> hashMap = new HashMap<>();
                if(mListModel.netto_flag_insert == 1) {
                        hashMap.put("data[" + mItemModel.id + "][netto]", Fact_NETTO_weight.getText().toString());
                        String fact_empty_box = factEmptyBoxEdit.getText().toString();
                        if (fact_empty_box.length() != 0)
                            fact_empty_box = "0";
                        hashMap.put("data[" + mItemModel.id + "][fact_weight_empty_box]", fact_empty_box);
                        String fact_empty_pallet = factEmptyPalletEdit.getText().toString();
                        if (fact_empty_pallet.length() == 0)
                            fact_empty_pallet = "0";
                        hashMap.put("data[" + mItemModel.id + "][fact_weight_empty_pallet]", fact_empty_pallet);
                } else {
                    hashMap.put("data[" + mItemModel.id + "][fact_weight_empty_box]",
                            factEmptyBoxEdit.getText().toString());
                    hashMap.put("data[" + mItemModel.id + "][fact_weight_empty_pallet]",
                            factEmptyPalletEdit.getText().toString());

                }
                hashMap.put("data[" + mItemModel.id + "][fact_item_weight]", factWeightEdit.getText().toString());
                hashMap.put("data[" + mItemModel.id + "][fact_item_box]", factBoxEdit.getText().toString());
                hashMap.put("data[" + mItemModel.id + "][Manufacturing_Date]",
                        mManufactDateEdit.getText().toString());
                hashMap.put("data[" + mItemModel.id + "][inbound_date]", mInboundDateEdit.getText().toString());
                hashMap.put("data[" + mItemModel.id + "][number_party]", numOfPartyEdit.getText().toString());
                hashMap.put("data[" + mItemModel.id + "][Lot_number_batch]", lotNumEdit.getText().toString());
                hashMap.put("data[" + mItemModel.id + "][Transport_Equipment_Number]",
                        transEqNumEdit.getText().toString());
                hashMap.put("data[" + mItemModel.id + "][Pallet_Type]", String.valueOf(palletTypeSpinner.getSelectedItemId()));
                hashMap.put("data[" + mItemModel.id + "][staging_location]", staging_location.getText().toString());


                mDataRepo = new DataRepo.getData(new DataRepo.onDataListener() {
                    @Override
                    public void returnData(String data) {
                        if(data != null) {
                            //{"data":"success","message":{"11107":"update","fact_complete":0}}
                            if(data.contains("success")) {
                                if(data.contains("\"fact_complete\":0")) {

                                    Toast.makeText(getContext(), "SAVE OK", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getContext(), "INPUT COMPLETE", Toast.LENGTH_SHORT).show();
                                    getActivity().finish();
                                    return;
                                }
                            } else {
                                Toast.makeText(getContext(), "SAVE error", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getContext(), "SAVE error", Toast.LENGTH_SHORT).show();
                        }
                        saveBtn.setEnabled(true);
                    }
                });
                saveBtn.setEnabled(false);
                mDataRepo.setModify(mListModel.id, hashMap);
                mDataRepo.start();
            }
        });

        return mBaseView;
    }

    private void addTextWatchers() {
        factWeightEdit.addTextChangedListener(mMyTextWatcherfactWeightEdit = new MyTextWatcher(factWeightEdit, mItemModel));
        factBoxEdit.addTextChangedListener(MyTextWatcherfactBoxEdit = new MyTextWatcher(factBoxEdit, mItemModel));
        factEmptyBoxEdit.addTextChangedListener(MyTextWatcherfactEmptyBoxEdit = new MyTextWatcher(factEmptyBoxEdit, mItemModel));
        factEmptyPalletEdit.addTextChangedListener(MyTextWatcherfactEmptyPalletEdit = new MyTextWatcher(factEmptyPalletEdit, mItemModel));
        mManufactDateEdit.addTextChangedListener(MyTextWatchermManufactDateEdit = new MyTextWatcher(mManufactDateEdit, mItemModel));
        mInboundDateEdit.addTextChangedListener(MyTextWatchermInboundDateEdit = new MyTextWatcher(mInboundDateEdit, mItemModel));
        numOfPartyEdit.addTextChangedListener(MyTextWatchernumOfPartyEdit = new MyTextWatcher(numOfPartyEdit, mItemModel));
        lotNumEdit.addTextChangedListener(MyTextWatcherlotNumEdit = new MyTextWatcher(lotNumEdit, mItemModel));
        transEqNumEdit.addTextChangedListener(MyTextWatchertransEqNumEdit = new MyTextWatcher(transEqNumEdit, mItemModel));
        Fact_NETTO_weight.addTextChangedListener(mMyTextWatcherFact_NETTO_weight = new MyTextWatcher(Fact_NETTO_weight, mItemModel));
    }

    private void removeTextWatchers() {
        factWeightEdit.removeTextChangedListener(mMyTextWatcherfactWeightEdit);
        factBoxEdit.removeTextChangedListener(MyTextWatcherfactBoxEdit);
        factEmptyBoxEdit.removeTextChangedListener(MyTextWatcherfactEmptyBoxEdit);
        factEmptyPalletEdit.removeTextChangedListener(MyTextWatcherfactEmptyPalletEdit);
        mManufactDateEdit.removeTextChangedListener(MyTextWatchermManufactDateEdit);
        mInboundDateEdit.removeTextChangedListener(MyTextWatchermInboundDateEdit);
        numOfPartyEdit.removeTextChangedListener(MyTextWatchernumOfPartyEdit);
        lotNumEdit.removeTextChangedListener(MyTextWatcherlotNumEdit);
        transEqNumEdit.removeTextChangedListener(MyTextWatchertransEqNumEdit);
        Fact_NETTO_weight.removeTextChangedListener(mMyTextWatcherFact_NETTO_weight);
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

    public void setItemModel(ItemModel im) {
        removeTextWatchers();
        mItemModel = im;
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
        lotNumEdit.setText(mItemModel.Lot_number_batch);
        //Fact_NETTO_weight.setText(mItemModel.netto == -1 ? "" : String.valueOf(mItemModel.netto));
        transEqNumEdit.setText(mItemModel.Transport_Equipment_Number);
        for(int i1= 0; i1 < mPalleteType.size(); i1++) {
            int id = -1;
            if(mItemModel.Pallet_Type !=-1)
                id = mItemModel.Pallet_Type;
            if(mPalleteType.get(i1).id == id)
                palletTypeSpinner.setSelection(i1);
        }
        addTextWatchers();
    }



    public interface setResponse {
        void set(HashMap<String, String> response);
        void send(String action);
    }

}
