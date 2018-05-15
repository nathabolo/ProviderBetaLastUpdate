package com.tradesman.provider.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Handler;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.jimmiejobscreative.tradesman.provider.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import com.tradesman.provider.model.ActiveJob;
import com.tradesman.provider.utils.Andyutils;

import java.util.List;

/**
 * Created by Elluminati Mohit on 1/27/2017.
 */

public class ActiveJobAdapter extends RecyclerView.Adapter<ActiveJobAdapter.MyViewHolde> {

    private List<ActiveJob> activeJobArrayList;
    private Context context;
    private ActiveJob activeJob;

    public ActiveJobAdapter(Context context,List<ActiveJob> activeJobArrayList){
        this.context = context;
        this.activeJobArrayList =activeJobArrayList;

    }

    @Override
    public MyViewHolde onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activej_job_raw,parent,false);
        return new MyViewHolde(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolde holder, int position) {



        activeJob = activeJobArrayList.get(position);
        holder.txtUserName.setText(activeJob.getUserName());
        holder.txtJobTitle.setText(activeJob.getJobTitle());
        holder.txtJobDescription.setText(activeJob.getDescription());
        holder.txtserviceRate.setText(String.valueOf(Html.fromHtml(activeJob.getCurrency()))+Andyutils.formateDigit(activeJob.getQuotation()));

        String getTime = Andyutils.formateDate(activeJob.getStartTime(),false);
        String[] splitTime = getTime.split(",");
        holder.txtServiceTime.setText(splitTime[1].trim()+"\n"+splitTime[0]);

        holder.layout.setBackgroundColor(Color.parseColor(Andyutils.showListColor((position%2),context)));
        holder.txtJobTitle.setTextColor(Color.parseColor(Andyutils.showColor((position%6),context)));
        holder.txtserviceRate.setTextColor(Color.parseColor(Andyutils.showColor((position%6),context)));
        Glide.with(context)
                    .load(activeJob.getUserImageUrl())
                    .asBitmap()
                    .placeholder(context.getResources().getDrawable(R.drawable.default_icon))
                    .dontAnimate()
                    .into(new BitmapImageViewTarget(holder.imgUser){
                        @Override
                        protected void setResource(Bitmap resource) {
                            super.setResource(resource);
                            RoundedBitmapDrawable cirimage = RoundedBitmapDrawableFactory.create(context.getResources(),resource);
                            cirimage.setCircular(true);
                            holder.imgUser.setImageDrawable(cirimage);
                        }
                    });






    }

    @Override
    public int getItemCount() {
        return activeJobArrayList.size();
    }

    public class MyViewHolde extends RecyclerView.ViewHolder {

        ImageView imgUser;
        TextView txtUserName,txtJobTitle,txtJobDescription,txtserviceRate,txtServiceTime;
        RelativeLayout layout;

        public MyViewHolde(View view) {
            super(view);
            imgUser = (ImageView) view.findViewById(R.id.imgUserActiveJob);
            txtUserName = (TextView) view.findViewById(R.id.txtusernameactiveJob);
            txtJobTitle = (TextView) view.findViewById(R.id.txtJobtitleActibeJob);
            txtJobDescription = (TextView) view.findViewById(R.id.txtjobdescriptionActiveJob);
            txtserviceRate = (TextView) view.findViewById(R.id.txtservicePriceActiveJob);
            txtServiceTime = (TextView) view.findViewById(R.id.txttimeActiveJob);
            layout = (RelativeLayout) view.findViewById(R.id.layout_myjob);
        }
    }
}
