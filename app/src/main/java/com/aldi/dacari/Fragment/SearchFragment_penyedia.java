package com.aldi.dacari.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.aldi.dacari.R;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;


public class SearchFragment_penyedia extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_search_penyedia, container, false);

        // tab layout *****************************************************
        TabLayout tabLayout = viewGroup.findViewById(R.id.tab_layout_penyedia);
        ViewPager viewPager = viewGroup.findViewById(R.id.pager_penyedia);
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        // tab layout // ***************************************************

        return viewGroup;
    }

    private void setupViewPager(ViewPager viewPager) {
        AccountFragment.ViewPagerAdapter adapter = new AccountFragment.ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new MyprojectsFragment_penyedia(), "Jasa");
        viewPager.setAdapter(adapter);
    }

    public static class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        ViewPagerAdapter(FragmentManager manager) {
            super(manager);
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

        void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
