package com.wartechwick.imovie.adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.wartechwick.imovie.fragments.MovieFragment;

/**
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        Fragment fragment;
        switch (position) {
            case 1:
                fragment = new MovieFragment();
                Bundle upcomingBundle = new Bundle();
                upcomingBundle.putString("title", "UPCOMING");
                fragment.setArguments(upcomingBundle);
                break;
            case 2:
                fragment = new MovieFragment();
                Bundle popularBundle = new Bundle();
                popularBundle.putString("title", "POPULAR");
                fragment.setArguments(popularBundle);
                break;
            default:
                fragment = new MovieFragment();
                Bundle inTheatreBundle = new Bundle();
                inTheatreBundle.putString("title", "INTHEATRE");
                fragment.setArguments(inTheatreBundle);
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        // Show 3 total pages.
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return getItem(position).getArguments().getString("title");
    }
}
