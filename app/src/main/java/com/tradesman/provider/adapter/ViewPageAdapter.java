package com.tradesman.provider.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.tradesman.provider.fragment.ActiveJobFragment;
import com.tradesman.provider.fragment.JobMenuFragment;
import com.tradesman.provider.fragment.PreviousJobFragment;
import com.jimmiejobscreative.tradesman.provider.R;;

/**
 * Created by Elluminati Mohit on 1/31/2017.
 */

public class ViewPageAdapter extends FragmentPagerAdapter {

    private Context context;
    public ViewPageAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context =context;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position){

            case 0 :
                return ActiveJobFragment.newInstance();

            case 1:
                return PreviousJobFragment.newInstanse();

            case 2:
                return JobMenuFragment.newInstanse();

        }

       return null;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {

        if(position == 0){
            return context.getString(R.string.active);
        }
        if(position == 1){
            return "Previous Jobs";
        }
        else {
            return "Menu";
        }
    }


}
