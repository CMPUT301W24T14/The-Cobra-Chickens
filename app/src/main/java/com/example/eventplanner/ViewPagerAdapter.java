package com.example.eventplanner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;

/**
 * The custom adapter class that manages the fragments of ViewPager2, which is location in
 * HomeFragment.
 */
public class ViewPagerAdapter extends FragmentStateAdapter {

    /* General information on implementing a tab layout for a ViewPager
    Reference:
    Author        : Foxandroid
    Date Accessed : 3/8/2024
    License       : Creative Commons
    URL           : https://www.youtube.com/watch?v=ziJ6-AT3ymg&ab_channel=Foxandroid
    Used in       : Throughout entire class.
    */

    private ArrayList<Fragment> fragmentArrayList = new ArrayList<>();
    private ArrayList<String> fragmentTitle = new ArrayList<>();
    public ViewPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    /**
     * Creates the fragment that is found at the given position.
     * @param position The position of the fragment in ViewPager2.
     * @return The fragment that is created.
     */
    @NonNull
    @Override
    public Fragment createFragment(int position) {

        return fragmentArrayList.get(position);
    }

    /**
     * Returns the number of fragments in fragmentArrayList.
     * @return number of fragments in fragmentArrayList.
     */
    @Override
    public int getItemCount() {

        return fragmentArrayList.size();
    }

    /**
     * Adds a new fragment to the adapter, specified by a title.
     * @param fragment The title of the fragment.
     * @param title The title of the fragment.
     */
    public void addFragment(Fragment fragment, String title) {

        fragmentArrayList.add(fragment);
        fragmentTitle.add(title);
    }

    public CharSequence getPageTitle(int position) {

        return fragmentTitle.get(position);
    }
}
