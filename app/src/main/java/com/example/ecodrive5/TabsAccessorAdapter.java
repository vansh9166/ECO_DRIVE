package com.example.ecodrive5;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class TabsAccessorAdapter extends FragmentPagerAdapter
{
    public TabsAccessorAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        switch (i){

            case 0: DashboardFragment dashboardFragment = new DashboardFragment();
                return dashboardFragment;

            case 1: GroupsFragment groupsFragment = new GroupsFragment();
                return groupsFragment;

            case 2: LeaderBoardFragment leaderBoardFragment = new LeaderBoardFragment();
                return leaderBoardFragment;





            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return 3;
    }



    @Nullable
    @Override
    public CharSequence getPageTitle(int position)
    {
        switch (position){


            case 0: return "Dashboard";

            case 1: return "Groups";

            case 2: return "Leader Board";


            default:
                return null;
        }

    }
}
