package com.test.test.Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.test.test.Fragments.InboundDetailsFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PageAdapter extends FragmentPagerAdapter {

    private List<InboundDetailsFragment> mFragmentList = new ArrayList<>();

    public PageAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }


    public void addFragments(InboundDetailsFragment fragment) {
        mFragmentList.add(fragment);
        notifyDataSetChanged();
    }

    public void updateFragments(HashMap<String, String> map, int position) {
        mFragmentList.get(position).updateCellList(map);
    }
}
