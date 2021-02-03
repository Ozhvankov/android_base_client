package com.test.test.Adapters;

import android.os.Bundle;

import com.test.test.Fragments.InboundDetailsFragment;
import com.test.test.Models.ItemModel;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;

public class ItemPageAdapter extends FragmentStatePagerAdapter {
    ArrayList<ItemModel> mItemModels;

    public ItemPageAdapter(@NonNull FragmentManager fm, ArrayList<ItemModel> itemModels) {
        super(fm);
        mItemModels = itemModels;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        InboundDetailsFragment detailsFragment = new InboundDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("data", mItemModels.get(position));
        detailsFragment.setArguments(bundle);
        return detailsFragment;
    }

    @Override
    public int getCount() {
        return mItemModels.size();
    }
    @Override
    public int getItemPosition(Object object){
        return PagerAdapter.POSITION_NONE;
    }
}


