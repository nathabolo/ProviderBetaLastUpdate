package com.tradesman.provider.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jimmiejobscreative.tradesman.provider.R;;
import com.tradesman.provider.model.ViewQuote;
import com.tradesman.provider.utils.Andyutils;

import java.util.List;

/**
 * Created by Elluminati Mohit on 1/25/2017.
 */

public class ViewQuoteAdapter extends RecyclerView.Adapter<ViewQuoteAdapter.MyAdapter>{

    private List<ViewQuote> viewQuoteArrayList;
    private Context context;
    private ViewQuote viewQuote;

    public ViewQuoteAdapter(Context context, List<ViewQuote> viewQuoteArrayList){
        this.context = context;
        this.viewQuoteArrayList = viewQuoteArrayList;
    }
    @Override
    public MyAdapter onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.avilable_job_list_raw,parent,false);

        return new MyAdapter(view);
    }

    @Override
    public void onBindViewHolder(MyAdapter holder, int position) {
        viewQuote = viewQuoteArrayList.get(position);
        String symbol = String.valueOf(Html.fromHtml(viewQuote.getSymbol()));
        holder.txtJobTitle.setText(viewQuote.getJobTitle());
        holder.txtJobTitle.setTextColor(Color.parseColor(Andyutils.showColor((position%6),context)));
        holder.txtJobTime.setText(Andyutils.formateDate(viewQuote.getStartTime(),false));
        holder.txtjobDescription.setText(viewQuote.getDescription());

        holder.txtQuatation.setText( Andyutils.formateDigit(viewQuote.getQuotation())+ symbol);

        holder.txtjobDistanceUnit.setText(viewQuote.getDistance() +" "+viewQuote.getDistanceUnit() +"\n away");

        Glide.with(context)
                .load(viewQuote.getServiceImageUrl())
                .placeholder(context.getResources().getDrawable(R.drawable.place_holder))
                .skipMemoryCache(true)
                .dontAnimate()
                .into(holder.imgjob);
        holder.imgjob.setColorFilter(Color.parseColor(Andyutils.showColor((position%6),context)));
        holder.layout.setBackgroundColor(Color.parseColor(Andyutils.showListColor((position%2),context)));


    }

    @Override
    public int getItemCount() {
        return viewQuoteArrayList.size();
    }

    public class MyAdapter extends RecyclerView.ViewHolder {
        ImageView imgjob;
        TextView txtJobTitle;
        TextView txtJobTime;
        TextView txtjobDescription;
        TextView txtQuatation;
        TextView  txtjobDistanceUnit;
       LinearLayout layout;
        public MyAdapter(View view) {
            super(view);
            layout = (LinearLayout) view.findViewById(R.id.availableLayout);
            imgjob = (ImageView) view.findViewById(R.id.imgavilableJob);
            txtJobTitle = (TextView) view.findViewById(R.id.txtJobTitle);
            txtJobTime = (TextView) view.findViewById(R.id.txtJobTime);
            txtjobDescription = (TextView) view.findViewById(R.id.txtJobDescriptipn);
            txtQuatation = (TextView) view.findViewById(R.id.txtJobMilesNumber);
            txtjobDistanceUnit = (TextView) view.findViewById(R.id.txtjobMiles);
        }
    }
}
