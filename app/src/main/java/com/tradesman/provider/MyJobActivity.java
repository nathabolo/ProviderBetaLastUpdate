package com.tradesman.provider;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import com.jimmiejobscreative.tradesman.provider.R;
import com.tradesman.provider.adapter.ViewPageAdapter;

public class MyJobActivity extends ActionBarBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_job_activity);
        preferenceHelper.putIsLoadPriviousJob(false);
        initRequire();
    }

    public void initRequire() {
        initToolBar();
        //tvActionBarTitle.setText(getString(R.string.my_job));
        btnDrawerToggle.setVisibility(View.INVISIBLE);
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager_myJob);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_myJob);
        ViewPageAdapter adapter = new ViewPageAdapter(getSupportFragmentManager(), this);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setAdapter(adapter);

    }

    @Override
    public void onBackPressed() {
        if(this.isTaskRoot()){
            goToAvailabeleJob(this);
        }
        super.onBackPressed();

    }


}
