package com.test.test.Adapters;

import android.os.Bundle;
import android.view.ViewGroup;

import com.test.test.Fragments.InboundDetailsFragment;
import com.test.test.Models.ItemModel;
import com.test.test.Models.ListModel;
import com.test.test.Models.PalletType;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;

public class ItemPageAdapter extends FragmentPagerAdapter  {
    private final ListModel mListModel;
    private ArrayList<PalletType> mPalletType;
    private ArrayList<ItemModel> mItemModels;
    private long baseId = 0;

    public ItemPageAdapter(@NonNull FragmentManager fm, ListModel listModel, ArrayList<ItemModel> itemModels, ArrayList<PalletType> palletType) {
        super(fm);
        mItemModels = itemModels;
        this.mPalletType = palletType;
        this.mListModel = listModel;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        InboundDetailsFragment detailsFragment = new InboundDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("data", mItemModels.get(position));
        bundle.putParcelableArrayList("pallete_types", mPalletType);
        bundle.putInt("page", position);
        bundle.putInt("count", mItemModels.size());
        bundle.putParcelable("mListModel", mListModel);
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

    @Override
    public long getItemId(int position) {
        // give an ID different from position when position has been changed
        return baseId + position;
    }

    public void setPallettypes(ArrayList<PalletType> palletType) {
        this.mPalletType = palletType;
        this.notifyDataSetChanged();
    }
    /**
     * Notify that the position of a fragment has been changed.
     * Create a new ID for each position to force recreation of the fragment
     * @param n number of items which have been changed
     */
    public void notifyChangeInPosition(int n) {
        // shift the ID returned by getItemId outside the range of all previous fragments
        baseId += getCount() + n;
    }
}


