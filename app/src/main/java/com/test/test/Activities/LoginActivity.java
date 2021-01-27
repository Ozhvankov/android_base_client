package com.test.test.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.fxn.stash.Stash;
import com.test.test.Fragments.LoginFragment;
import com.test.test.Fragments.ServerFragment;
import com.test.test.R;

public class LoginActivity extends AppCompatActivity {

    LinearLayout mContainer;
    FragmentManager mFragmentManager;
    LoginFragment mLoginFragment;
    ServerFragment mServerFragment;
    boolean mIsChange = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mContainer = findViewById(R.id.container);
        if (Stash.getString("email") != null && !Stash.getString("email").isEmpty()) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        } else {
            mFragmentManager = getSupportFragmentManager();
            mLoginFragment = new LoginFragment(new LoginFragment.FragmentTransactiion() {
                @Override
                public void change() {
                    mIsChange = true;
                    mFragmentManager.beginTransaction()
                            .replace(R.id.container, mServerFragment)
                            .commit();
                }
            });
            mServerFragment = new ServerFragment();
            mFragmentManager.beginTransaction()
                    .add(R.id.container, mLoginFragment)
                    .add(R.id.container, mServerFragment)
                    .show(mLoginFragment)
                    .commit();
        }
    }

    @Override
    public void onBackPressed() {
        if (mIsChange) {
            mFragmentManager.beginTransaction()
                    .replace(R.id.container, mLoginFragment)
                    .commit();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
