package com.jarvis.marshall.view.home.event;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jarvis.marshall.R;
import com.jarvis.marshall.view.home.task.TasksFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class EventMainFragment extends Fragment {
    private View view;
    private Adapter adapter;
    private String eventKey;

    public EventMainFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Bundle bundle = getArguments();
        if(bundle!=null){
            eventKey = bundle.getString("eventKey");
        }

        view = inflater.inflate(R.layout.fragment_event_main, container, false);
        super.onCreate(savedInstanceState);
        final ViewPager viewPager = view.findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        TabLayout tabLayout = ButterKnife.findById(view, R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        return view;
    }

    private void setupViewPager(ViewPager viewPager){
        adapter = new Adapter(getChildFragmentManager());
        TasksFragment tasksFragment = new TasksFragment();
        Bundle bundle = new Bundle();
        bundle.putString("eventKey",eventKey);
        tasksFragment.setArguments(bundle);

        adapter.addFragment(new DetailsFragment(), "Details");
        adapter.addFragment(tasksFragment, "Tasks");
        adapter.addFragment(new CommentsFragment(), "Comments");
        viewPager.setAdapter(adapter);
    }

    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        public Adapter(android.support.v4.app.FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }
    }

}
