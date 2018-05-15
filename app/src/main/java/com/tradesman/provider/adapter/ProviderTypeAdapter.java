package com.tradesman.provider.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import com.jimmiejobscreative.tradesman.provider.R;
import com.tradesman.provider.model.ProviderType;
import com.tradesman.provider.utils.Andyutils;
import com.tradesman.provider.utils.PreferenceHelper;

import java.util.List;

/**
 * Created by Elluminati Mohit on 1/21/2017.
 */

public class ProviderTypeAdapter extends RecyclerView.Adapter<ProviderTypeAdapter.MyViewHolder> {

    private List<ProviderType> providerTypeArrayList;
    private ProviderType providerType;
    private Context context;
    private PreferenceHelper preferenceHelper;
    private String getTypeId;
    private String[] splitType;

    public ProviderTypeAdapter(List<ProviderType> providerTypeArrayList, Context context){
        this.providerTypeArrayList = providerTypeArrayList;
        this.context = context;
        preferenceHelper = PreferenceHelper.getInstance(context);

    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.provider_type_raw,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        providerType = providerTypeArrayList.get(position);



        holder.layoutProvider.setBackgroundColor(Color.parseColor(Andyutils.showColor((position%6),context)));
        holder.txtPType.setText(providerType.getName());
        Glide.with(context)
                .load(providerType.getPicture())
                .placeholder(context.getResources().getDrawable(R.drawable.place_holder))
                .skipMemoryCache(true)
                .into(holder.imgePType);
        if(providerType.getSelected()){
            addSelectedType(holder.linearLayout,holder.imgePType,holder.selectedType);
        }

           /* if(!TextUtils.isEmpty(getTypeId) && position == Integer.parseInt(splitType[position])){
                Log.i("position", "onBindViewHolder: "+"Selected == = "+position);
            }
*/


    }

    public void addSelectedType(LinearLayout layout , ImageView imageView,ImageView selectedType){
        layout.setBackgroundColor(context.getResources().getColor(R.color.color_selected));
        imageView.setVisibility(View.VISIBLE);
        selectedType.setVisibility(View.VISIBLE);
    }



    @Override
    public int getItemCount() {
        return providerTypeArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imgePType,selectedType;
        TextView txtPType;
        RelativeLayout layoutProvider;
        LinearLayout linearLayout;

        public MyViewHolder(View itemView) {
            super(itemView);
            imgePType = (ImageView) itemView.findViewById(R.id.imgProviderType);
            txtPType = (TextView) itemView.findViewById(R.id.txtProviderType);
            layoutProvider = (RelativeLayout) itemView.findViewById(R.id.layout_provider);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.layoutRelativeType);
            selectedType = (ImageView) itemView.findViewById(R.id.imgCorrectPType);
        }
    }
}
