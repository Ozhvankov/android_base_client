package com.test.test.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

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
        mBaseView.findViewById(R.id.save_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mHostEdit.getText().toString().length() > 0) {
                    Stash.put("domain", mHostEdit.getText().toString());
                    getActivity().onBackPressed();
                } else {
                    Toast.makeText(getContext(), "empty host", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return mBaseView;
    }
}
