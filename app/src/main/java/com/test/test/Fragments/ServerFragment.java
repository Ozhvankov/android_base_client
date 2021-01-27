package com.test.test.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.fxn.stash.Stash;
import com.test.test.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ServerFragment extends Fragment {

    private View mBaseView;
    private EditText mHostEdit;

    public ServerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBaseView = inflater.inflate(R.layout.fragment_server, container, false);
        mHostEdit = mBaseView.findViewById(R.id.domain_edit);
        mHostEdit.setText(Stash.getString("domain"));
        mHostEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Stash.put("domain", charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        return mBaseView;
    }
}
