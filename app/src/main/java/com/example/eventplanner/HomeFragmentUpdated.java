package com.example.eventplanner;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

/**
 * HomeFragmentUpdated is a Fragment that represents the home screen of the application.
 * Within it, it contains three other fragments, AllEventsFragment, MyEventsFragment, and
 * OrganizeEventsFragment.
 * HomeFragmentUpdated allows the user to navigate between these via the viewPager2 widget, where
 * the user can select specific tabs that will take them to these fragments.
 */
public class HomeFragmentUpdated extends Fragment {

    private TabLayout tabLayout; // the tabLayout
    private ViewPager2 viewPager2; // the viewPager2 widget

    /**
     * Creates the view for HomeFragmentUpdated.
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return The view specific to HomeFragmentUpdated
     */
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // inflate the layout for the updated home fragment
        View view = inflater.inflate(R.layout.fragment_home_updated, container, false);

        // connect tabLayout to the actual TabLayout in fragment_home_updated.xml
        tabLayout = view.findViewById(R.id.tab_layout);

        // connect viewPager2 to the actual ViewPager2 in fragment_home_updated.xml
        viewPager2 = view.findViewById(R.id.view_pager);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager(), getLifecycle());
        viewPagerAdapter.addFragment(new AllEventsFragment(), "All Events");
        viewPagerAdapter.addFragment(new MyEventsFragment(), "My Events");
        viewPagerAdapter.addFragment(new OrganizeEventsFragment(), "Organize");

        viewPager2.setAdapter(viewPagerAdapter);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });

        return view;
    }
}
