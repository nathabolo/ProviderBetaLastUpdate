package com.tradesman.provider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.Target;
import com.tradesman.provider.dialog.ImageDialog;
import com.tradesman.provider.utils.AppLog;
import com.jimmiejobscreative.tradesman.provider.R;;
import com.tradesman.provider.dialog.CustomDialogNoEdt;
import com.tradesman.provider.model.ViewQuote;
import com.tradesman.provider.parse.AsyncTaskCompleteListener;
import com.tradesman.provider.parse.HttpRequester;
import com.tradesman.provider.utils.Andyutils;
import com.tradesman.provider.utils.Const;

import java.util.HashMap;

/**
 * Created by Elluminati Mohit on 1/26/2017.
 */

public class ViewQuoteInfoActivity extends ActionBarBaseActivity implements View.OnClickListener, AsyncTaskCompleteListener {


    private ImageView imgService;
    private ImageView imgUser;
    private ImageView imgIssueImage;
    private TextView txtJobTitle;
    private TextView txtJobDescription;
    private TextView txtAddress;
    private TextView txtUserName;
    private TextView txtDistance;
    private TextView txtDistanseUnit;
    private RatingBar userRating;
    private Button btnProvideQuote;
    private CustomDialogNoEdt quoteDialog ;
    private ViewQuote viewQuote;
    private int position;
    private ImageView imgPinViewQuote;
    private TextView txtPostedBy;
    private ImageView imgeIssueimageProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.available_job_fragment);
        initToolBar();
        setToolBarTitle(getString(R.string.view_quote_info));
        btnDrawerToggle.setVisibility(View.INVISIBLE);

        Intent intent = getIntent();
        viewQuote = intent.getParcelableExtra("viewQuote");
        position = intent.getIntExtra("position",0);

        initRequire();
    }

    public void initRequire(){

        imgeIssueimageProgress = (ImageView) findViewById(R.id.imgeProgressIssueImage);
        imgeIssueimageProgress.setAnimation(AnimationUtils.loadAnimation(this , R.anim.rotation_animation));
        imgPinViewQuote = (ImageView) findViewById(R.id.imgpin);
        imgPinViewQuote.setOnClickListener(this);
        imgService = (ImageView) findViewById(R.id.imgavilableJobfrg);
        txtPostedBy = (TextView) findViewById(R.id.txtpostedby);
        txtPostedBy.setVisibility(View.VISIBLE);
        imgUser = (ImageView) findViewById(R.id.imgUserPhotofrg);
        imgIssueImage = (ImageView) findViewById(R.id.imgavilablejobIssue);
        imgIssueImage.setOnClickListener(this);
        txtJobTitle = (TextView) findViewById(R.id.txtJobTitlefrg);
        txtJobDescription = (TextView) findViewById(R.id.txtJobDescriptipnfrg);
        txtAddress = (TextView) findViewById(R.id.txtavilablejobAddress);
        txtAddress.setOnClickListener(this);
        txtUserName = (TextView) findViewById(R.id.txtUserNameavilablelJob);
        txtDistance = (TextView) findViewById(R.id.txtJobMilesNumberfrg);
        txtDistanseUnit = (TextView) findViewById(R.id.txtjobMilesfrg);

        userRating = (RatingBar) findViewById(R.id.avilable_job_rating);
        btnProvideQuote = (Button) findViewById(R.id.btnProvideQuote);
        btnProvideQuote.setOnClickListener(this);
        btnProvideQuote.setText(getString(R.string.canecl_quote));
        setData();
    }

    public void setData(){

        txtJobTitle.setText(viewQuote.getJobTitle());
        txtDistance.setText(viewQuote.getDistance());
        txtDistanseUnit.setText(viewQuote.getDistanceUnit());
        txtJobDescription.setText(viewQuote.getDescription());

        txtAddress.setText(viewQuote.getAddress());
        txtUserName.setText(viewQuote.getUserName());
        AppLog.Log("tag","rating - "+Float.parseFloat(viewQuote.getUserRating()));
        userRating.setRating(Float.parseFloat(viewQuote.getUserRating()));

        Glide.with(this)
                .load(viewQuote.getServiceImageUrl())
                .skipMemoryCache(true)
                .placeholder(getResources().getDrawable(R.drawable.place_holder))
                .into(imgService);
        imgService.setColorFilter(Color.parseColor(Andyutils.showColor((position%6),getApplicationContext())));


            Glide.with(this)
                    .load(viewQuote.getIssueImageUrl())
                    .skipMemoryCache(true)
                    .placeholder(getResources().getDrawable(R.drawable.no_img))
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            imgeIssueimageProgress.clearAnimation();
                            imgeIssueimageProgress.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            imgeIssueimageProgress.clearAnimation();
                            imgeIssueimageProgress.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .into(imgIssueImage);



        if(!TextUtils.isEmpty(viewQuote.getUserImageUrl())){
            Glide.with(this)
                    .load(viewQuote.getUserImageUrl())
                    .asBitmap()
                    .placeholder(getResources().getDrawable(R.drawable.default_icon))
                    .into(new BitmapImageViewTarget(imgUser){
                        @Override
                        protected void setResource(Bitmap resource) {
                            super.setResource(resource);
                            RoundedBitmapDrawable cirimage = RoundedBitmapDrawableFactory.create(getResources(),resource);
                            cirimage.setCircular(true);
                            imgUser.setImageDrawable(cirimage);
                        }
                    });

        }

    }
    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.btnProvideQuote:
               cancelQuoteDialog();
                break;

            case R.id.txtavilablejobAddress:
            case R.id.imgpin:
                Andyutils.openGoogleMap(this,preferenceHelper.getLatitude(),preferenceHelper.getLongitude(),
                           viewQuote.getLatitude(),viewQuote.getLongitude() );
                break;
            case R.id.imgavilablejobIssue:
                if(!TextUtils.isEmpty(viewQuote.getIssueImageUrl())){
                    new ImageDialog(this,viewQuote.getIssueImageUrl()).show();
                }
        }

    }

    public void cancelQuoteRequest(){

        HashMap<String, String > map = new HashMap<>();

        map.put(Const.Param.URL,Const.ServiceType.CANCEL_QUOTE);
        map.put(Const.Param.USER_ID,preferenceHelper.getUSERID());
        map.put(Const.Param.TOKEN,preferenceHelper.getToken());
        map.put(Const.Param.REQUEST_ID,viewQuote.getId());

        new HttpRequester(this,map,Const.ServiceCode.CANCEL_QUOTE,Const.HttpMethod.POST,this);

        Andyutils.showCustomProgressDialog(this,false);
    }

    @Override
    public void onTaskCompleted(String response, int serviceCode) {


        switch (serviceCode){

            case Const.ServiceCode.CANCEL_QUOTE:
                Andyutils.removeCustomProgressDialog();
                if(parseContent.issucess(response)){
                   quoteDialog.dismiss();
                    setResult(Const.VIEW_QUOTE_CHNAGE);
                    finish();
                }
                break;
        }
    }

    public void cancelQuoteDialog(){

        quoteDialog = new CustomDialogNoEdt(this,getString(R.string.canecl_quote),getString(R.string.cancel_quote_dialog_message)
                                            ,getString(R.string.cancel_quote_btn_ok),getString(R.string.CANCEL),true) {
            @Override
            public void btnOkClickListner() {
                cancelQuoteRequest();
            }

            @Override
            public void btnCancelClickListner() {
                dismiss();
            }
        };

        quoteDialog.show();
    }
}
