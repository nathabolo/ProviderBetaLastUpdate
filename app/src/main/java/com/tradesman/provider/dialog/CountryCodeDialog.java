package com.tradesman.provider.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

import com.tradesman.provider.adapter.CountryCodeAdapter;
import com.tradesman.provider.model.CountryCode;
import com.tradesman.provider.parse.ParseContent;
import com.jimmiejobscreative.tradesman.provider.R;;
import com.tradesman.provider.utils.RecyclerViewTouchListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Elluminati Mohit on 2/4/2017.
 */

public abstract class CountryCodeDialog extends Dialog implements RecyclerViewTouchListener.ClickListener, TextWatcher {

    private Activity activity;
    private List<CountryCode> countryDataList;
    private CountryCodeAdapter adapter;


    protected CountryCodeDialog(Activity activity, List<CountryCode> countryDataList) {
        super(activity);
        this.activity = activity;
        this.countryDataList = countryDataList;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.country_list_dialog);
        WindowManager.LayoutParams param = getWindow().getAttributes();
        param.width = WindowManager.LayoutParams.MATCH_PARENT;
        param.height = WindowManager.LayoutParams.MATCH_PARENT;
        getWindow().setAttributes(param);



        initRequire();
    }

    public void initRequire(){
        ParseContent parseContent = new ParseContent(activity);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_country_code);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(activity);
        recyclerView.setLayoutManager(layoutManager);
         EditText edtSearch = (EditText) findViewById(R.id.edtsearchCountry);
        edtSearch.addTextChangedListener(this);
        countryDataList = parseContent.parseCountryCode();
        adapter = new CountryCodeAdapter(activity,countryDataList);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnItemTouchListener(new RecyclerViewTouchListener(activity,recyclerView,this));
    }

    @Override
    public void onClickRecyclerListIteam(View view, int position) {
            select(view,position);

    }

    public abstract void select(View view,int position);

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        String getData = charSequence.toString();
        adapter.setFilter(filter(countryDataList,getData));

    }

    @Override
    public void afterTextChanged(Editable editable) {

    }


    /*It is use for filter country list as per search country*/
    private List<CountryCode> filter(List<CountryCode> listModel, String query) {
        String queryData = query.toLowerCase();
        final List<CountryCode> filteredModelList = new ArrayList<>();
        for (CountryCode countryCode : listModel) {
            final String text = countryCode.getCountryName().toLowerCase();
            if (text.contains(queryData)) {
                filteredModelList.add(countryCode);
            }
        }
        return filteredModelList;
    }
}
