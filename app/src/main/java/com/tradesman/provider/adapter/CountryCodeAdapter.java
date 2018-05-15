package com.tradesman.provider.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tradesman.provider.model.CountryCode;


import java.util.ArrayList;
import java.util.List;
import com.jimmiejobscreative.tradesman.provider.R;

/**
 * Created by Elluminati Mohit on 2/4/2017.
 */

public class CountryCodeAdapter extends RecyclerView.Adapter<CountryCodeAdapter.MyViewHolder> {


    private Context context;
    private List<CountryCode>  countryCodeArrayList;
    private CountryCode countryCode;
    public CountryCodeAdapter(Context context, List<CountryCode> countryCodeArrayList){
        this.context = context;
        this.countryCodeArrayList = countryCodeArrayList;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.country_code_raw,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        countryCode = countryCodeArrayList.get(position);
        holder.txtCountrycode.setText(countryCode.getCountryPhoneCode());
        holder.txtCountryName.setText(countryCode.getCountryName());
    }

    @Override
    public int getItemCount() {
        return countryCodeArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtCountrycode,txtCountryName;
        public MyViewHolder(View view) {
            super(view);

            txtCountrycode = (TextView) view.findViewById(R.id.txtcountryPhoneCode);
            txtCountryName = (TextView) view.findViewById(R.id.txtcountryName);
        }
    }


    public void setFilter(List<CountryCode> countryModels) {
        countryCodeArrayList = new ArrayList<>();
        countryCodeArrayList.addAll(countryModels);
        notifyDataSetChanged();
    }
}
