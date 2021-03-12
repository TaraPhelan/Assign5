package com.example.sdaassign52021taraphelan;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

/**
 * ViewPageAdapter
 * @author Chris Coughlan 2019
 * updated by Tara Phelan 2021
 */
public class ViewPageAdapter extends FragmentPagerAdapter {

    private Context context;

    ViewPageAdapter(FragmentManager fm, int behavior, Context context) {
        super(fm, behavior);
        this.context = context;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        Fragment fragment = new Fragment();

        //finds the tab position (note array starts at 0)
        position = position+1;

        //finds the fragment
        switch (position)
        {
            case 1:
                //code
                fragment = new Chart();
                break;
            case 2:
                //code
                fragment = new Actions();
                break;
            case 3:
                //code
                fragment = new LifeAreas();
                break;
        }

        return fragment;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        position = position+1;

        CharSequence tabTitle;

        //finds the fragment
        switch (position)
        {
            case 1:
                //code
                tabTitle = context.getString(R.string.chart);
                break;
            case 2:
                //code
                tabTitle = context.getString(R.string.actions);
                break;
            case 3:
                //code
                tabTitle = context.getString(R.string.life_areas);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + position);
        }

        return tabTitle;
    }
}
