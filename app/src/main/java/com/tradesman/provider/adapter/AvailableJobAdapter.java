package com.tradesman.provider.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.jimmiejobscreative.tradesman.provider.R;
import com.bumptech.glide.Glide;

import com.tradesman.provider.model.AvailableJob;
import com.tradesman.provider.utils.Andyutils;
import com.tradesman.provider.utils.PreferenceHelper;

import java.util.List;

/**
 * Created by Elluminati Mohit on 1/23/2017.
 */

public class AvailableJobAdapter extends RecyclerView.Adapter<AvailableJobAdapter.MyViewHolder> {

    private List<AvailableJob> availableJobArrayList;
    private Context context;
    private AvailableJob availableJob;

    public AvailableJobAdapter(Context context, List<AvailableJob> availableJobArrayList){
        this.context = context;
        this.availableJobArrayList = availableJobArrayList;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.avilable_job_list_raw,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        availableJob = availableJobArrayList.get(position);

        holder.txtJobTitle.setText(availableJob.getJobTitle());
        holder.txtJobTime.setText(Andyutils.formateDate(availableJob.getStartTime(),false));
        holder.txtjobDescription.setText(availableJob.getDescription());
        holder.txtjobDistance.setText(Andyutils.formateDigit(availableJob.getDistance())+" KM Away");
        //holder.txtjobDistanceUnit.setText(availableJob.getDistanceUnit() + context.getString(R.string.away));
        holder.txtjobDistanceUnit.setText("R "+availableJob.getQ()+"");

        Glide.with(context)
                .load(availableJob.getServiceImageUrl())
                .placeholder(context.getResources().getDrawable(R.drawable.place_holder))
                .dontAnimate()
                .skipMemoryCache(true)
                .into(holder.imgjob);


        holder.imgjob.setColorFilter(Color.parseColor(Andyutils.showColor((position%6),context)));
        holder.txtJobTitle.setTextColor(Color.parseColor(Andyutils.showColor((position%6),context)));
        holder.layout.setBackgroundColor(Color.parseColor(Andyutils.showListColor((position%2),context)));
        holder.txtjobDistance.setTextSize(10);


    }

    @Override
    public int getItemCount() {
        return availableJobArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imgjob;
        TextView txtJobTitle;
        TextView txtJobTime;
        TextView txtjobDescription;
        TextView txtjobDistance;
        TextView  txtjobDistanceUnit;
        LinearLayout layout;
        public MyViewHolder(View view) {
            super(view);
            layout = (LinearLayout) view.findViewById(R.id.availableLayout);
            imgjob = (ImageView) view.findViewById(R.id.imgavilableJob);
            txtJobTitle = (TextView) view.findViewById(R.id.txtJobTitle);
            txtJobTime = (TextView) view.findViewById(R.id.txtJobTime);
            txtjobDescription = (TextView) view.findViewById(R.id.txtJobDescriptipn);
            txtjobDistance = (TextView) view.findViewById(R.id.txtJobMilesNumber);
            txtjobDistanceUnit = (TextView) view.findViewById(R.id.txtjobMiles);
        }
    }
}
