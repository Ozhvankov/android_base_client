package com.test.test.Adapters;

import android.os.Bundle;
import android.view.ViewGroup;

import com.test.test.Fragments.InboundDetailsFragment;
import com.test.test.Models.ItemModel;
import com.test.test.Models.PalletType;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;

public class ItemPageAdapter extends FragmentStatePagerAdapter {
    private final int mListId;
    private ArrayList<PalletType> mPalletType;
    ArrayList<ItemModel> mItemModels;

    public ItemPageAdapter(@NonNull FragmentManager fm, int id, ArrayList<ItemModel> itemModels, ArrayList<PalletType> palletType) {
        super(fm);
        mItemModels = itemModels;
        this.mPalletType = palletType;
        this.mListId = id;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        InboundDetailsFragment detailsFragment = new InboundDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("data", mItemModels.get(position));
        bundle.putParcelableArrayList("pallete_types", mPalletType);
        bundle.putInt("page", position);
        bundle.putInt("mListId", mListId);
        detailsFragment.setArguments(bundle);
        return detailsFragment;
    }

    private Fragment mCurrentFragment;

    public Fragment getCurrentFragment() {
        return mCurrentFragment;
    }
    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        if (getCurrentFragment() != object) {
            mCurrentFragment = ((Fragment) object);
        }
        super.setPrimaryItem(container, position, object);
    }

    @Override
    public int getCount() {
        return mItemModels.size();
    }
    @Override
    public int getItemPosition(Object object){
        return PagerAdapter.POSITION_NONE;
    }

    public void setPallettypes(ArrayList<PalletType> palletType) {
        this.mPalletType = palletType;
        this.notifyDataSetChanged();
    }
}


