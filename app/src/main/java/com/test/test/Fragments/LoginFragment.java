package com.test.test.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.fxn.stash.Stash;
import com.test.test.Activities.MainActivity;
import com.test.test.R;
import com.test.test.Repository.DataRepo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {

    private View mBaseView;
    private Button mLoginBtn, mServerBtn;
    private EditText mEmailEdit, mPassEdit;
    private FragmentTransactiion mFragmentTransactiion;

    public static LoginFragment newInstance(FragmentTransactiion transactiion) {
        LoginFragment fragment = new LoginFragment(transactiion);
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public LoginFragment(FragmentTransactiion fragmentTransactiion) {
        this.mFragmentTransactiion = fragmentTransactiion;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBaseView = inflater.inflate(R.layout.fragment_login, container, false);
        mEmailEdit = mBaseView.findViewById(R.id.email_edit);
        mPassEdit = mBaseView.findViewById(R.id.pass_edit);
        mServerBtn = mBaseView.findViewById(R.id.server_btn);
        mLoginBtn = mBaseView.findViewById(R.id.login_btn);
        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mEmailEdit.getText().toString().isEmpty() || mPassEdit.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), R.string.empty_cred_err, Toast.LENGTH_SHORT).show();
                } else {
                    DataRepo.getData dataRepo = new DataRepo.getData(new DataRepo.onDataListener() {
                        @Override
                        public void returnData(String data) {
                            try {
                                JSONObject tempObject = new JSONObject(data);
                                if (tempObject.has("total")){
                                    if (tempObject.getInt("total") == 0){
                                        Toast.makeText(getContext(), R.string.wrong_cred_err, Toast.LENGTH_SHORT).show();
                                    }
                                    else {
                                        JSONArray array = tempObject.getJSONArray("rows");
                                        JSONObject userObject = array.getJSONObject(0);
                                        Stash.put("user_id", userObject.getString("id"));
                                        Stash.put("warehouse_id", userObject.getInt("warehouse_id"));
                                        Stash.put("email", mEmailEdit.getText().toString());
                                        Stash.put("pass", mPassEdit.getText().toString());
                                        Stash.put("api_key", userObject.getString("api_key"));
                                        startActivity(new Intent(getContext(), MainActivity.class));
                                        Objects.requireNonNull(getActivity()).finish();
                                    }
                                }
                                else {
                                    Toast.makeText(getContext(), R.string.conn_err, Toast.LENGTH_SHORT).show();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(getContext(),"Wrong server answer: " + e.toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    dataRepo.auth(mEmailEdit.getText().toString(), mPassEdit.getText().toString());
                    dataRepo.start();

                }

            }
        });
        mServerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFragmentTransactiion.change();
            }
        });
        if(Stash.getString("domain").length() == 0)
            mFragmentTransactiion.change();
        else {
            mServerBtn.setText(getText(R.string.change_server) + ": " +Stash.getString("domain"));
        }
        return mBaseView;
    }

    public interface FragmentTransactiion {
        void change();
    }
}
