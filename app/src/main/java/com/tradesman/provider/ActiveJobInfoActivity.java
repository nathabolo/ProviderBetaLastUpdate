package com.tradesman.provider;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.Target;
import com.tradesman.provider.dialog.ImageDialog;
import com.tradesman.provider.parse.MultiPartRequester;
import com.tradesman.provider.utils.AppLog;
import com.tradesman.provider.utils.Const;

import com.tradesman.provider.dialog.CustomDialogNoEdt;
import com.tradesman.provider.model.ActiveJob;
import com.tradesman.provider.parse.AsyncTaskCompleteListener;
import com.tradesman.provider.parse.HttpRequester;
import com.tradesman.provider.utils.Andyutils;
import com.jimmiejobscreative.tradesman.provider.R;
import com.tradesman.provider.utils.Validation;

import java.util.HashMap;

public class ActiveJobInfoActivity extends ActionBarBaseActivity implements View.OnClickListener, AsyncTaskCompleteListener {

    private ActiveJob activeJob;
    private int position;
    private ImageView imgService;
    private ImageView imgUser;
    private ImageView imgIssueImage;
    private TextView txtJobTitle;
    private TextView txtJobDescription;
    private TextView txtAddress;
    private TextView txtUserName;
    private TextView txtDistance;
    private TextView txtDistanseUnit;
    private TextView txtJobTime;
    private RatingBar userRating;
    private Button btnProvideQuote;
    private Button btnCancel;
    private CustomDialogNoEdt cancelJobDialog;
    private ImageView imgeIssueimageProgress;
    private Dialog provideQuoteDialog,buyTokensDialog;
    private int quoteToken = 5;
    private int jobDoneToken = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.available_job_fragment);
        Intent intent = getIntent();
        activeJob = intent.getParcelableExtra(Const.ACTIVE_JOB);
        position = intent.getIntExtra(Const.Param.POSITION,0);
        initRequire();
        final Handler handler=new Handler();

        final Runnable updateTask=new Runnable() {
            @Override
            public void run() {

                handler.postDelayed(this,1000);
            }
        };

        handler.postDelayed(updateTask,1000);
    }

    public void initRequire(){
        initToolBar();
        btnDrawerToggle.setVisibility(View.INVISIBLE);
        setToolBarTitle(getString(R.string.my_job_info));
        imgeIssueimageProgress = (ImageView) findViewById(R.id.imgeProgressIssueImage);
        imgeIssueimageProgress.setAnimation(AnimationUtils.loadAnimation(this , R.anim.rotation_animation));
        imgService = (ImageView) findViewById(R.id.imgavilableJobfrg);
        txtJobTime = (TextView) findViewById(R.id.txtJobTimefrg);
        imgUser = (ImageView) findViewById(R.id.imgUserPhotofrg);
        imgIssueImage = (ImageView) findViewById(R.id.imgavilablejobIssue);
        imgIssueImage.setOnClickListener(this);
        txtJobTitle = (TextView) findViewById(R.id.txtJobTitlefrg);
        txtJobDescription = (TextView) findViewById(R.id.txtJobDescriptipnfrg);
        txtAddress = (TextView) findViewById(R.id.txtavilablejobAddress);
        txtUserName = (TextView) findViewById(R.id.txtUserNameavilablelJob);
        txtDistance = (TextView) findViewById(R.id.txtJobMilesNumberfrg);
        txtDistanseUnit = (TextView) findViewById(R.id.txtjobMilesfrg);
        userRating = (RatingBar) findViewById(R.id.avilable_job_rating);
        btnProvideQuote = (Button) findViewById(R.id.btnProvideQuote);
        btnCancel = (Button) findViewById(R.id.btnCancelJob);
        btnCancel.setOnClickListener(this);
        btnProvideQuote.setOnClickListener(this);
        ImageView imgPin = (ImageView) findViewById(R.id.imgpin);
        imgPin.setOnClickListener(this);
        txtAddress.setOnClickListener(this);
        setdata();
        setButtonText();
        setCancelRequestButton();


    }

    public void setButtonText(){

        switch (activeJob.getRequestStatus()){
            case "1":
                btnProvideQuote.setText(getString(R.string.provider_on_the_way));
                break;
            case "2":
                btnProvideQuote.setText(getString(R.string.provider_arrived));
                break;
            case "3":
                btnProvideQuote.setText(getString(R.string.provider_job_start));
                break;
            case "4":
                btnProvideQuote.setText(getString(R.string.provider_job_done));
                break;
            default:
                AppLog.Log("tag","Default case - Set Button text");

        }

    }



    public void setdata(){

        txtJobTime.setText(Andyutils.formateDate(activeJob.getStartTime(),false));
        txtJobTitle.setText(activeJob.getJobTitle());
        txtJobDescription.setText(activeJob.getDescription());
        txtAddress.setText(activeJob.getAddress());
        txtUserName.setText(activeJob.getUserName());
        txtDistance.setText(String.valueOf(Html.fromHtml(activeJob.getCurrency()))+Andyutils.formateDigit(activeJob.getQuotation()));
        txtDistanseUnit.setText(getString(R.string.you_will_receive));
        AppLog.Log("tag","rating - "+Float.parseFloat(activeJob.getUserRating()));
        userRating.setRating(Float.parseFloat(activeJob.getUserRating()));

        Glide.with(this)
                .load(activeJob.getServiceImageUrl())
                .skipMemoryCache(true)
                .placeholder(getResources().getDrawable(R.drawable.place_holder))
                .into(imgService);
        imgService.setColorFilter(Color.parseColor(Andyutils.showColor(position%6,getApplicationContext())));

        Glide.with(this)
                .load(activeJob.getIssueImgeUrl())
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
                .load(activeJob.getUserImageUrl())
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
    @Override
    public void onClick(View view) {
        int getId = view.getId();
        if(getId == R.id.btnProvideQuote){
            switch (view.getId()){
            case R.id.btnProvideQuote:
                switch (activeJob.getRequestStatus()){

                    case "1":
                        tripStartedService();
                        startProgressDialog();
                        break;

                    case "2":
                        providerArrivedService();
                        startProgressDialog();
                        break;
                    case "3":
                        jobStartService();
                        startProgressDialog();
                        break;
                    case "4":
                        if(Integer.parseInt(preferenceHelper.getUserToken())<jobDoneToken){
                            CreateJobDoneDialog(" Tokens In order to Send an Invoice\nNOTE: You may also proceed and the amount will be deducted from the total amount to be received", jobDoneToken);
                        }else {
                            jobFinishService();
                            startProgressDialog();
                        }
                            break;


                    default:
                        AppLog.Log("tag","Default");
                        if(Integer.parseInt(preferenceHelper.getUserToken())<quoteToken){
                            CreateProvideBuyTokenDialog(" Tokens In order to make a Quote", quoteToken);
                        }else {
                            CreateProvideQuoteDialog();
                        }
                        break;


                }
                break;
        }
        }else if (getId == R.id.btnCancelJob){
            createCancelJobdialog();
        }else if(getId == R.id.txtavilablejobAddress || getId == R.id.imgpin){

            if(!TextUtils.isEmpty(activeJob.getLatitude()) && !TextUtils.isEmpty(activeJob.getLongitude())
                        && !TextUtils.isEmpty(preferenceHelper.getLatitude()) && !TextUtils.isEmpty(preferenceHelper.getLatitude())){
                Andyutils.openGoogleMap(this,activeJob.getLatitude(),activeJob.getLongitude(),
                        preferenceHelper.getLatitude(),preferenceHelper.getLongitude());
            }else {
                Andyutils.showToast(this,"Something Went Wrong");
            }
        }else if (getId == R.id.imgavilablejobIssue && !TextUtils.isEmpty(activeJob.getIssueImgeUrl())){
            new ImageDialog(this,activeJob.getIssueImgeUrl()).show();
        }

    }



    public void addQuoteService(String quote){
        HashMap<String,String > map = new HashMap<>();
        map.put(Const.Param.URL,Const.ServiceType.ADD_QUOTE);
        map.put(Const.Param.TOKEN, preferenceHelper.getToken());
        map.put(Const.Param.USER_ID,preferenceHelper.getUSERID());
        map.put(Const.Param.REQUEST_ID,activeJob.getRequestId());
        map.put(Const.Param.QUOTE,quote);
        map.put("token_bal",(Integer.parseInt(preferenceHelper.getUSERID())-quoteToken)+"");
        new HttpRequester(this,map,Const.ServiceCode.ADD_QUOTE,Const.HttpMethod.POST,this);
        Andyutils.showCustomProgressDialog(this,false);


    }



    private void onTaskAddQuote(String response){
        if (parseContent.issucess(response)) {
            Andyutils.showToast(this, getString(R.string.sucesfully_submited));
            Intent intent = new Intent();
            intent.putExtra("position", position);
            setResult(Const.ACTIVITY_QUOTE_PROVIDED, intent);
            provideQuoteDialog.dismiss();
            Intent intent1 = new Intent(getApplicationContext(),AvailableJobActivity.class);
            startActivity(intent1);
        }
    }
    public void CreateProvideBuyTokenDialog(String buyTokenMessage,  int tokenValue){
        ImageView imgClose;
        Button btnSubmit;
        buyTokensDialog = new Dialog(ActiveJobInfoActivity.this);
        buyTokensDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        buyTokensDialog.setContentView(R.layout.provider_buy_tokens_dialog);
        buyTokensDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.MATCH_PARENT);
        buyTokensDialog.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.color_white_lessOpacity)));
        btnSubmit = (Button) buyTokensDialog.findViewById(R.id.btnBuyTokens);


        imgClose = (ImageView) buyTokensDialog.findViewById(R.id.imgCLosePopup);
        TextView txtAdminPrice = (TextView) buyTokensDialog.findViewById(R.id.txtadminPriceDialog);
        txtAdminPrice.setText("Buy at least "+(tokenValue-Integer.parseInt(preferenceHelper.getUserToken()))+buyTokenMessage);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Andyutils.showToast(getApplicationContext(),"Buying Tokens");

            }
        });
        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buyTokensDialog.dismiss();
            }
        });
        buyTokensDialog.show();
    }

    public void CreateJobDoneDialog(String buyTokenMessage,  int tokenValue){
        ImageView imgClose;
        Button btnSubmit,btnComplete;
        buyTokensDialog = new Dialog(ActiveJobInfoActivity.this);
        buyTokensDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        buyTokensDialog.setContentView(R.layout.provider_job_done_dialog);
        buyTokensDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.MATCH_PARENT);
        buyTokensDialog.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.color_white_lessOpacity)));
        btnSubmit = (Button) buyTokensDialog.findViewById(R.id.btnBuyTokens);
        btnComplete = (Button) buyTokensDialog.findViewById(R.id.btnBuyTokensForJobDone);

        imgClose = (ImageView) buyTokensDialog.findViewById(R.id.imgCLosePopup);
        TextView txtAdminPrice = (TextView) buyTokensDialog.findViewById(R.id.txtadminPriceDialog);
        txtAdminPrice.setText("Buy at least "+(tokenValue-Integer.parseInt(preferenceHelper.getUserToken()))+buyTokenMessage);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Andyutils.showToast(getApplicationContext(),"Buying Tokens");

            }
        });

        btnComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Andyutils.showToast(getApplicationContext(),"Complete Job Without Buying Tokens");

            }
        });

        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buyTokensDialog.dismiss();
            }
        });
        buyTokensDialog.show();
    }

    public void CreateProvideQuoteDialog(){

        final EditText edtPrice;
        ImageView imgClose;
        final double adminPrice = Double.parseDouble(Andyutils.formateDigit(activeJob.getAdminPrice()));

        Button btnSubmit;
        provideQuoteDialog = new Dialog(ActiveJobInfoActivity.this);
        provideQuoteDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        provideQuoteDialog.setContentView(R.layout.provide_quote_dialog);
        provideQuoteDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.MATCH_PARENT);
        provideQuoteDialog.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.color_white_lessOpacity)));
        btnSubmit = (Button) provideQuoteDialog.findViewById(R.id.btnProvideQuoteDialog);
        edtPrice = (EditText) provideQuoteDialog.findViewById(R.id.edtPricing);
        imgClose = (ImageView) provideQuoteDialog.findViewById(R.id.imgCLosePopup);
        TextView txtAdminPrice = (TextView) provideQuoteDialog.findViewById(R.id.txtadminPriceDialog);

//        txtAdminPrice.setText(getString(R.string.admin_price_for_dialog)+" "+Andyutils.formateDigit(String.valueOf(adminPrice))+
//                String.valueOf(Html.fromHtml(activeJob.getCurrency())));
        txtAdminPrice.setText("");
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String quote = edtPrice.getText().toString();


                if(!TextUtils.isEmpty(quote) && Double.parseDouble(quote) > 0){
                    double providePrice = Double.parseDouble(quote);
                    if(Validation.numberValidate(quote) && !(providePrice- adminPrice <= 0)){

                        addQuoteService(quote);
                    }else if(providePrice- adminPrice <= 0){
                        Andyutils.showToast(getApplicationContext(),getString(R.string.error_admin_price));
                    }else {
                        Andyutils.showToast(getApplicationContext(),getString(R.string.error_price_more));
                    }
                }else if(!TextUtils.isEmpty(quote) && Double.parseDouble(quote) <= 0){
                    Andyutils.showToast(getApplicationContext(),getString(R.string.error_price_zero));
                } else{
                    Andyutils.showToast(getApplicationContext(),getString(R.string.error_add_price));
                }
            }
        });
        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                provideQuoteDialog.dismiss();
                Intent intent = new Intent(getApplicationContext(),AvailableJobActivity.class);
                startActivity(intent);
            }
        });
        provideQuoteDialog.show();
    }


    public void tripStartedService(){

        HashMap<String ,String> map = new HashMap<>();
        map.put(Const.Param.URL,Const.ServiceType.ON_THE_WAY);
        map.put(Const.Param.USER_ID,preferenceHelper.getUSERID());
        map.put(Const.Param.TOKEN,preferenceHelper.getToken());
        map.put(Const.Param.REQUEST_ID,activeJob.getRequestId());

        new HttpRequester(this,map,Const.ServiceCode.ON_THE_WAY, Const.HttpMethod.POST,this);

    }

    public void providerArrivedService(){

        HashMap<String ,String> map = new HashMap<>();
        map.put(Const.Param.URL,Const.ServiceType.HAS_ARRIVED);
        map.put(Const.Param.USER_ID,preferenceHelper.getUSERID());
        map.put(Const.Param.TOKEN,preferenceHelper.getToken());
        map.put(Const.Param.REQUEST_ID,activeJob.getRequestId());

        new HttpRequester(this,map,Const.ServiceCode.HAS_ARRIVED, Const.HttpMethod.POST,this);
    }

    public void jobStartService(){

        HashMap<String ,String> map = new HashMap<>();
        map.put(Const.Param.URL,Const.ServiceType.JOB_START);
        map.put(Const.Param.USER_ID,preferenceHelper.getUSERID());
        map.put(Const.Param.TOKEN,preferenceHelper.getToken());
        map.put(Const.Param.REQUEST_ID,activeJob.getRequestId());

        new HttpRequester(this,map,Const.ServiceCode.JOB_START, Const.HttpMethod.POST,this);
    }

    public void jobFinishService(){

        HashMap<String ,String> map = new HashMap<>();
        map.put(Const.Param.URL,Const.ServiceType.JOB_FINISH);
        map.put(Const.Param.USER_ID,preferenceHelper.getUSERID());
        map.put(Const.Param.TOKEN,preferenceHelper.getToken());
        map.put(Const.Param.REQUEST_ID,activeJob.getRequestId());
        map.put("token_bal",(Integer.parseInt(preferenceHelper.getUSERID())-quoteToken)+"");

        new HttpRequester(this,map,Const.ServiceCode.JOB_FINISH, Const.HttpMethod.POST,this);
    }

    public void updateProfileService(){

        HashMap<String,String> map = new HashMap<>();
        map.put(Const.Param.URL,Const.ServiceType.UPDATE_PROFILE);
        map.put(Const.Param.NAME,preferenceHelper.getUserName());
        map.put(Const.Param.ADDRESS,preferenceHelper.getAddress());
        map.put(Const.Param.EMAIL,preferenceHelper.getEmail());
        map.put(Const.Param.USER_ID,preferenceHelper.getUSERID());
        map.put(Const.Param.TOKEN,preferenceHelper.getToken());
        map.put(Const.Param.PHONE,preferenceHelper.getPhoneNumber());
        map.put(Const.Param.COUNTRY_CODE,preferenceHelper.getCountryCode());


        map.put(Const.Param.NEW_PASS,"");
        map.put(Const.Param.OLD_PASS, "");

        map.put(Const.Param.PICTURE,"");

        new MultiPartRequester(this,map, Const.ServiceCode.UPDATE_PROFILE,this);

        Andyutils.showCustomProgressDialog(this,false);
    }

    @Override
    public void onTaskCompleted(String response, int serviceCode) {
        try{
        if (serviceCode == Const.ServiceCode.ADD_QUOTE) {
            updateProfileService();
            Andyutils.removeCustomProgressDialog();
            onTaskAddQuote(response);

        }
        switch (serviceCode){
            case Const.ServiceCode.ON_THE_WAY:
                stopProgessDialog();
               onTaskOntheWay(response);
                break;

            case Const.ServiceCode.HAS_ARRIVED:
                stopProgessDialog();
                onTaskHasArrived(response);
                break;
            case Const.ServiceCode.JOB_START:
                stopProgessDialog();
                onTaskJobComplete(response);
                break;

            case Const.ServiceCode.JOB_FINISH:
                stopProgessDialog();
                onTaskJobFinish(response);
                break;
            case Const.ServiceCode.CANCEL_JOB:
                stopProgessDialog();
                onTaskCancelJob(response);
                break;
            case Const.ServiceCode.UPDATE_PROFILE:
                Andyutils.removeCustomProgressDialog();

                break;
            default:
                AppLog.Log(Const.TAG,"Default");
                break;
        }
    }catch (Exception e){
            Toast.makeText(ActiveJobInfoActivity.this, "Connection Error\nPlease Try Again", Toast.LENGTH_SHORT).show();
        }}

    public void onTaskOntheWay(String response){
        if(parseContent.issucess(response)){
            btnProvideQuote.setText(getString(R.string.provider_arrived));
            activeJob.setRequestStatus(String.valueOf(Const.ProviderTripStatus.PROVIDER_TRIP_START));
            sendData(Const.ProviderTripStatus.PROVIDER_TRIP_START);
        }
    }

    public void onTaskHasArrived(String response){
        if(parseContent.issucess(response)){
            btnCancel.setVisibility(View.GONE);
            btnProvideQuote.setText(getString(R.string.provider_job_start));
            activeJob.setRequestStatus(String.valueOf(Const.ProviderTripStatus.PROVIDER_HAS_ARRIVED));
            sendData(Const.ProviderTripStatus.PROVIDER_HAS_ARRIVED);
        }
    }

    public void onTaskJobComplete(String response){
        if(parseContent.issucess(response)){
            btnProvideQuote.setText(getString(R.string.provider_job_done));
            activeJob.setRequestStatus(String.valueOf(Const.ProviderTripStatus.PROVIDER_JOB_START));
            sendData(Const.ProviderTripStatus.PROVIDER_JOB_START);
        }
    }
    public void onTaskJobFinish(String response){
        if(parseContent.issucess(response)){
            activeJob.setRequestStatus(String .valueOf(Const.ProviderTripStatus.PROVIDER_JOB_DONE));
            sendData(Const.ProviderTripStatus.PROVIDER_JOB_DONE);
            startFeedbackActivity();
        }
    }
    public void onTaskCancelJob(String response){
        if(parseContent.issucess(response) && cancelJobDialog != null  && cancelJobDialog.isShowing()){
            cancelJobDialog.dismiss();
            Intent intent =new Intent();
            intent.putExtra("position",position);
            setResult(Const.CHANGE_IN_DATA,intent);
            //setResult(Const.REFRESH_LIST);
            this.onBackPressed();
        }
    }

    public void startProgressDialog(){
        Andyutils.showCustomProgressDialog(this,false);
    }

    public void stopProgessDialog(){
        Andyutils.removeCustomProgressDialog();
    }


    public void sendData(int requestStatus){

        Intent intent = new Intent();
        intent.putExtra("position",position);
        intent.putExtra("request_status",requestStatus);
        setResult(Const.PROVIDER_STATE_CHANGE,intent);
    }
    public void startFeedbackActivity(){

        Intent intent = new Intent(this,FeedBackActivity.class);
        intent.putExtra("feedback",activeJob);
        intent.putExtra("position",position);
        intent.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
        startActivity(intent);
        finish();

    }
    public void setCancelRequestButton(){
        int requestStatus = Integer.parseInt(activeJob.getRequestStatus()) ;
        if(requestStatus <= Const.ProviderTripStatus.PROVIDER_TRIP_START){
            btnCancel.setVisibility(View.VISIBLE);
        }else {
            btnCancel.setVisibility(View.GONE);
        }
    }

    public void cancelJobService(){

        HashMap<String ,String> map = new HashMap<>();
        map.put(Const.Param.URL,Const.ServiceType.CANCEL_JOB);
        map.put(Const.Param.USER_ID,preferenceHelper.getUSERID());
        map.put(Const.Param.TOKEN,preferenceHelper.getToken());
        map.put(Const.Param.REQUEST_ID,activeJob.getRequestId());

        new HttpRequester(this,map,Const.ServiceCode.CANCEL_JOB, Const.HttpMethod.POST,this);
        Andyutils.showCustomProgressDialog(this,false);
    }


    public void createCancelJobdialog(){

        cancelJobDialog = new CustomDialogNoEdt(this,getString(R.string.cancel_job),getString(R.string.dialog_logout_message),
                       getString(R.string.OK),getString(R.string.CANCEL),true ) {
            @Override
            public void btnOkClickListner() {
                cancelJobService();
            }

            @Override
            public void btnCancelClickListner() {
                    dismiss();
            }
        };
        cancelJobDialog.show();
    }


}
