package com.tradesman.provider.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.jimmiejobscreative.tradesman.provider.R;;
import com.tradesman.provider.model.PreviousJob;
import com.tradesman.provider.utils.Andyutils;

import java.util.List;

/**
 * Created by Elluminati Mohit on 1/31/2017.
 */

public class PreviousJobAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{


    private List<PreviousJob> previousJobArrayList;
    private  Activity activity;

    private static final int TYPE_ITEM =1;
    private static final int TYPE_LOAD = 2;
    private boolean isShowLoading = false;
    private PreviousJob previousJob;


    public PreviousJobAdapter(List<PreviousJob> previousJobArrayList, Activity activity){
        this.previousJobArrayList = previousJobArrayList;
        this.activity = activity;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if(viewType == TYPE_ITEM){
            View view = LayoutInflater.from(activity).inflate(R.layout.activej_job_raw,parent,false);
            return new IteamHolder(view);
        }else if(viewType == TYPE_LOAD){
            View view = LayoutInflater.from(activity).inflate(R.layout.recycler_loading,parent,false);
            return new LoadingHolder(view);
        }else {
            return null;
        }

    }

    @Override
    public int getItemViewType(int position) {

        if (position == getItemCount() - 1) {
            return TYPE_LOAD;
        } else {
            return TYPE_ITEM;
        }
    }

    public void isShowLoadingPanel(boolean isShow) {
        this.isShowLoading = isShow;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof LoadingHolder) {

            LoadingHolder loadingHolder = (LoadingHolder) holder;
            if(isShowLoading){
                loadingHolder.progressBar.setVisibility(View.VISIBLE);
                loadingHolder.progressBar.getIndeterminateDrawable().setColorFilter(activity.getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.MULTIPLY);
            }
            else{
                loadingHolder.progressBar.setVisibility(View.GONE);
            }


        } else if (holder instanceof IteamHolder) {

            final IteamHolder itemHolder = (IteamHolder) holder;

            previousJob = previousJobArrayList.get(position);
            double adminPrice = Double.parseDouble(previousJob.getAdminPrice());
            double totalPrice = Double.parseDouble(previousJob.getQuotation());
            String providerReceivedPrice = Andyutils.formateDigit(String.valueOf(totalPrice - adminPrice));
            itemHolder.txtUserName.setText(previousJob.getUserName());
            itemHolder.txtJobTitle.setText(previousJob.getJobTitle());
            itemHolder.txtJobDescription.setText(previousJob.getDescription());
            itemHolder.txtserviceRate.setText(String.valueOf(Html.fromHtml(previousJob.getCurrency()))+providerReceivedPrice);

            String getTime = Andyutils.formateDate(previousJob.getStartTime(),true);
            String[] splitTime = getTime.split(",");
            itemHolder.txtServiceTime.setText(splitTime[1].trim()+"\n"+splitTime[0]);

            itemHolder.layout.setBackgroundColor(Color.parseColor(Andyutils.showListColor((position%2),activity)));
            itemHolder.txtJobTitle.setTextColor(Color.parseColor(Andyutils.showColor((position%6),activity)));
            itemHolder.txtserviceRate.setTextColor(Color.parseColor(Andyutils.showColor((position%6),activity)));

            Glide.with(activity)
                        .load(previousJob.getUserImageUrl())
                        .asBitmap()
                        .placeholder(activity.getResources().getDrawable(R.drawable.default_icon))
                        .skipMemoryCache(true)
                        .dontAnimate()
                        .into(new BitmapImageViewTarget(itemHolder.imgUser){
                            @Override
                            protected void setResource(Bitmap resource) {
                                super.setResource(resource);
                                RoundedBitmapDrawable cirimage = RoundedBitmapDrawableFactory.create(activity.getResources(),resource);
                                cirimage.setCircular(true);
                                itemHolder.imgUser.setImageDrawable(cirimage);
                            }
                        });


        }


    }

    @Override
    public int getItemCount() {
        return previousJobArrayList.size() + 1;
    }

    private class IteamHolder extends RecyclerView.ViewHolder{

        ImageView imgUser;
        TextView txtUserName,txtJobTitle,txtJobDescription,txtserviceRate,txtServiceTime;
        RelativeLayout layout;
        public IteamHolder(View view) {
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

    private class LoadingHolder extends RecyclerView.ViewHolder{
        ProgressBar progressBar;
        public LoadingHolder(View itemView) {
            super(itemView);
            progressBar = (ProgressBar) itemView.findViewById(R.id.recycler_progress);

        }
    }
}
