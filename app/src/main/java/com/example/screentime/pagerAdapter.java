package com.example.screentime;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class pagerAdapter extends FragmentStatePagerAdapter {
    @NonNull


    int noOfTabs;
    public pagerAdapter(FragmentManager fm,int noOfTabs )
    {
        super(fm);
        this.noOfTabs=noOfTabs;
    }
    public Fragment getItem(int position) {
        switch(position)
        {
            case 0:
                Tab1 tab1 =new Tab1();
                return tab1;
            case 1:
                Tab2 tab2=new Tab2();
                return tab2;

            default:
                return null;
        }

    }

    @Override
    public int getCount()
    {
        return noOfTabs;
    }
}
