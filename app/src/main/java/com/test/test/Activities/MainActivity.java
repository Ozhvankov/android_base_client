package com.test.test.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

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
import com.test.test.Repository.DataRepo;
import com.test.test.Utils.FragmentUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
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

        PrimaryDrawerItem mainWarehouseItem = new PrimaryDrawerItem().withIdentifier(0).withName("Main").withTag("Main ");
        PrimaryDrawerItem fishWarehouseItem = new PrimaryDrawerItem().withIdentifier(1).withName("Fish").withTag("Fish ");



        PrimaryDrawerItem exitAccountItem = new PrimaryDrawerItem().withIdentifier(2).withName("Exit Account");
        PrimaryDrawerItem exitAppItem = new PrimaryDrawerItem().withIdentifier(3).withName("Exit App");

        mDrawer = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(mToolbar)
                .withCloseOnClick(false)
                .withMultiSelect(false)
                .withAccountHeader(header)
                .addDrawerItems(mainWarehouseItem, fishWarehouseItem, exitAccountItem, exitAppItem)
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        switch (position) {
                            case 1:
                            case 2:
                                setWareHouse(drawerItem);
                                return false;
                            case 3:
                                mDrawer.closeDrawer();
                                Stash.clearAll();
                                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                                return true;

                            case 4:
                                mDrawer.closeDrawer();
                                System.exit(0);
                                return true;

                            default:
                                return false;
                        }
                    }
                })
                .build();

        switch (Stash.getString("warehouse")){
            case "Main ":
                mDrawer.setSelection(0);
                break;
            case "Fish ":
                mDrawer.setSelection(1);
                break;
        }

    }

    private void setWareHouse(final IDrawerItem drawerItem) {
        DataRepo.getData dataRepo = new DataRepo.getData(new DataRepo.onDataListener() {
            @Override
            public void returnData(String data) {
                if (data != null && !data.isEmpty()) {
                    try {
                        JSONObject object = new JSONObject(data);
                        if (object.has("data")) {
                            if (object.getString("data").equals("success")) {
                                mDrawer.closeDrawer();
                                Stash.put("warehouse", (String)drawerItem.getTag());
                                Stash.put("warehouse_id", (int)(((PrimaryDrawerItem)drawerItem).getIdentifier() + 1));
                                getSupportActionBar().setTitle(Stash.getString("warehouse"));
                                mToolbar.setTitle(Stash.getString("warehouse"));
                            } else {
                                Toast.makeText(MainActivity.this, R.string.err, Toast.LENGTH_SHORT).show();
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
        dataRepo.setWarehouse(String.valueOf(((PrimaryDrawerItem)drawerItem).getIdentifier() + 1), Stash.getString("user_id"));
        dataRepo.start();

    }

    public interface toolbarChangeListener{
        void onChange(String s);
    }
}
