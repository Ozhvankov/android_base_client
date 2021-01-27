package com.test.test.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.fxn.stash.Stash;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.test.test.Fragments.MainFragment;
import com.test.test.R;
import com.test.test.Utils.FragmentUtils;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    Drawer mDrawer;
    Toolbar mToolbar;
    MainFragment mMainFragment;
    toolbarChangeListener mToolbarChangeListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mToolbarChangeListener = new toolbarChangeListener() {
            @Override
            public void onChange(String s) {
                mToolbar.setTitle(s);
            }
        };
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mMainFragment = new MainFragment(mToolbarChangeListener);
        getSupportActionBar().setTitle(Stash.getString("warehouse"));
        mToolbar.setTitle(Stash.getString("warehouse"));

        FragmentUtils.attachFragment(this, mMainFragment, R.id.main_container);
        FragmentUtils.openNewFragment(this, mMainFragment, true, R.id.main_container);

        AccountHeader header = new AccountHeaderBuilder()
                .withActivity(this)
                .addProfiles(new ProfileDrawerItem()
                        .withName(Stash.getString("email"))
                        .withEmail(Stash.getString("domain"))
                        .withIcon(getResources().getDrawable(R.drawable.ic_launcher_background)))
                .build();

        PrimaryDrawerItem exitAccountItem = new PrimaryDrawerItem().withIdentifier(1).withName("Exit Account");
        PrimaryDrawerItem exitAppItem = new PrimaryDrawerItem().withIdentifier(2).withName("Exit App");

        mDrawer = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(mToolbar)
                .withCloseOnClick(true)
                .withMultiSelect(false)
                .withAccountHeader(header)
                .addDrawerItems(exitAccountItem, exitAppItem)
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        switch (position) {
                            case 1:
                                Stash.clearAll();
                                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                                return true;

                            case 2:
                                System.exit(0);
                                return true;

                            default:
                                return false;
                        }
                    }
                })
                .build();

    }

    public interface toolbarChangeListener{
        void onChange(String s);
    }
}
