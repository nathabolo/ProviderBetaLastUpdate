package com.tradesman.provider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.Target;
import com.jimmiejobscreative.tradesman.provider.R;
import com.tradesman.provider.dialog.ImageDialog;
import com.tradesman.provider.model.PreviousJob;
import com.tradesman.provider.utils.Andyutils;
import com.tradesman.provider.utils.AppLog;

;

public class PreviosJobinfoActivity extends ActionBarBaseActivity implements View.OnClickListener {


    private PreviousJob previousJob;
    private LinearLayout givenFeedbacklayout;
    private int position;
    private ImageView imgService;
    private ImageView imgUser;
    private TextView txtJobTitle;
    private TextView txtJobDescription;
    private TextView txtUserName;
    private TextView txtEarned;
    private TextView txtReceived;
    private TextView txtJobTime;
    private RatingBar userRating;
    private Button btnProvideQuote;
    private TextView txtAddress;
    private TextView txtgivenFeedback;
    private TextView txtcancelStatus;
    private ImageView imgeIssueimageProgress;
    private ImageView imgIssueImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.available_job_fragment);
        Intent intent = getIntent();
        position = intent.getIntExtra("position", 0);
        previousJob = intent.getParcelableExtra("previous_job");
        initRequire();
    }

    public void initRequire() {
        initToolBar();
        btnDrawerToggle.setVisibility(View.INVISIBLE);
        setToolBarTitle(getString(R.string.previous_job));

        givenFeedbacklayout = (LinearLayout) findViewById(R.id.layoutgivenfeedback);
        givenFeedbacklayout.setVisibility(View.VISIBLE);
        imgeIssueimageProgress = (ImageView) findViewById(R.id.imgeProgressIssueImage);
        imgeIssueimageProgress.setAnimation(AnimationUtils.loadAnimation(this, R.anim.rotation_animation));
        txtgivenFeedback = (TextView) findViewById(R.id.txtgivenFeedback);
        imgService = (ImageView) findViewById(R.id.imgavilableJobfrg);
        txtJobTime = (TextView) findViewById(R.id.txtJobTimefrg);
        imgUser = (ImageView) findViewById(R.id.imgUserPhotofrg);
        imgIssueImage = (ImageView) findViewById(R.id.imgavilablejobIssue);
        imgIssueImage.setOnClickListener(this);
        txtJobTitle = (TextView) findViewById(R.id.txtJobTitlefrg);
        txtJobDescription = (TextView) findViewById(R.id.txtJobDescriptipnfrg);
        txtAddress = (TextView) findViewById(R.id.txtavilablejobAddress);
        txtUserName = (TextView) findViewById(R.id.txtUserNameavilablelJob);
        txtEarned = (TextView) findViewById(R.id.txtJobMilesNumberfrg);
        txtReceived = (TextView) findViewById(R.id.txtjobMilesfrg);
        userRating = (RatingBar) findViewById(R.id.avilable_job_rating);
        userRating.setStepSize((float) 0.5);
        btnProvideQuote = (Button) findViewById(R.id.btnProvideQuote);
        txtcancelStatus = (TextView) findViewById(R.id.txtCancelstatus);
        btnProvideQuote.setVisibility(View.GONE);

        setData();
    }

    public void setData() {

        txtJobTime.setText(Andyutils.formateDate(previousJob.getStartTime(), false));
        txtJobTitle.setText(previousJob.getJobTitle());
        txtJobDescription.setText(previousJob.getDescription());
        txtAddress.setText(previousJob.getAddress());
        txtUserName.setText(previousJob.getUserName());
        double adminPrice = Double.parseDouble(previousJob.getAdminPrice());
        double totalPrice = Double.parseDouble(previousJob.getQuotation());
        String providerReceivedPrice = Andyutils.formateDigit(String.valueOf(totalPrice - adminPrice));
        txtEarned.setText(String.valueOf(Html.fromHtml(previousJob.getCurrency()))+providerReceivedPrice );
        txtReceived.setText(getString(R.string.you_received));
        AppLog.Log("tag", "rating - " + (float) previousJob.getUserGivenRating());
        userRating.setRating((float) previousJob.getUserGivenRating());

        switch (previousJob.getRequestStatus()) {
            case "8":
                txtcancelStatus.setVisibility(View.VISIBLE);
                txtcancelStatus.setText(getString(R.string.cancel_by_user));
                break;
            case "9":
                txtcancelStatus.setVisibility(View.VISIBLE);
                txtcancelStatus.setText(getString(R.string.cancel_by_you));
                break;
            default:
                AppLog.Log("tag", "Default case");
                break;
        }
        if (!TextUtils.isEmpty(previousJob.getUserComment())) {
            txtgivenFeedback.setText(previousJob.getUserComment());
        }


        Glide.with(this)
                .load(previousJob.getServiceImageUrl())
                .skipMemoryCache(true)
                .placeholder(getResources().getDrawable(R.drawable.place_holder))
                .into(imgService);
        imgService.setColorFilter(Color.parseColor(Andyutils.showColor((position % 6), getApplicationContext())));
        AppLog.Log("tag", "previous job issue image" + previousJob.getIssueImgeUrl());
        Glide.with(this)
                .load(previousJob.getIssueImgeUrl())
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

        Glide.with(this)
                .load(previousJob.getUserImageUrl())
                .asBitmap()
                .placeholder(getResources().getDrawable(R.drawable.default_icon))
                .into(new BitmapImageViewTarget(imgUser) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        super.setResource(resource);
                        RoundedBitmapDrawable cirimage = RoundedBitmapDrawableFactory.create(getResources(), resource);
                        cirimage.setCircular(true);
                        imgUser.setImageDrawable(cirimage);
                    }
                });
    }

    @Override
    public void onClick(View view) {
        int getId = view.getId();
        if (getId == R.id.imgavilablejobIssue && !TextUtils.isEmpty(previousJob.getIssueImgeUrl())) {
            new ImageDialog(this, previousJob.getIssueImgeUrl()).show();
        }

    }


}
