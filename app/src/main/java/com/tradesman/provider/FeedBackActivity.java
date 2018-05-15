package com.tradesman.provider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.tradesman.provider.dialog.InvoiceDialog;
import com.tradesman.provider.parse.HttpRequester;
import com.tradesman.provider.utils.Andyutils;
import com.tradesman.provider.utils.AppLog;
import com.tradesman.provider.utils.Const;
import com.jimmiejobscreative.tradesman.provider.R;;
import com.tradesman.provider.model.ActiveJob;
import com.tradesman.provider.parse.AsyncTaskCompleteListener;

import java.util.HashMap;

public class FeedBackActivity extends ActionBarBaseActivity implements View.OnClickListener, AsyncTaskCompleteListener {

    private ActiveJob activeJob;
    private int position;
    private ImageView imgService;
    private ImageView imgUser;
    private TextView txtJobTitle;
    private TextView txtJobType;
    private TextView txtJobDescription;
    private TextView txtUserName;
    private TextView txtDistance;
    private TextView txtDistanseUnit;
    private TextView txtJobTime;
    private RatingBar userRating;
    private EditText edtFeedback;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feed_back_activity);
        Intent intent = getIntent();
        activeJob = intent.getParcelableExtra("feedback");
        position = intent.getIntExtra("position",0);

        initRequire();
    }

    public void initRequire(){
        initToolBar();
        setToolBarTitle(getString(R.string.feedback));
        btnDrawerToggle.setVisibility(View.INVISIBLE);
        createInvoiceDialog();
        imgService = (ImageView) findViewById(R.id.imgfeedback);
        txtJobTime = (TextView) findViewById(R.id.txtJobTimefeedback);
        imgUser = (ImageView) findViewById(R.id.imgUserPhotofeedback);
        txtJobTitle = (TextView) findViewById(R.id.txtJobTitlefeedback);
        txtJobType = (TextView) findViewById(R.id.txtjobTypefeedback);
        txtJobDescription = (TextView) findViewById(R.id.txtJobDescriptipnfeedback);
        txtUserName = (TextView) findViewById(R.id.txtUserNamefeedback);
        txtDistance = (TextView) findViewById(R.id.txtJobMilesNumberfeedback);
        txtDistanseUnit = (TextView) findViewById(R.id.txtjobMilesfeedback);
        userRating = (RatingBar) findViewById(R.id.ratingfeedback);
        Button btnSubmit = (Button) findViewById(R.id.btnfeedback);
        edtFeedback = (EditText) findViewById(R.id.edtFeedback);
        btnSubmit.setOnClickListener(this);

        setData();
    }

    public void setData(){
        txtJobTime.setText(Andyutils.formateDate(activeJob.getStartTime(),false));
        txtJobTitle.setText(activeJob.getJobTitle());
        txtJobDescription.setText(activeJob.getDescription());
        txtUserName.setText(activeJob.getUserName());
        txtDistance.setText(Andyutils.currencySigns(activeJob.getCurrency())+Andyutils.formateDigit(activeJob.getQuotation()));
        txtDistanseUnit.setText(getString(R.string.you_received));
        if(TextUtils.equals("0",activeJob.getRequestType())){
            txtJobType.setText(getString(R.string.job_type_repair));
        }else {
            txtJobType.setText(getString(R.string.job_type_installation));
        }

        Glide.with(this)
                .load(activeJob.getServiceImageUrl())
                .skipMemoryCache(true)
                .placeholder(getResources().getDrawable(R.drawable.place_holder))
                .into(imgService);
        imgService.setColorFilter(Color.parseColor(Andyutils.showColor((position%6),getApplicationContext())));

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

        switch (view.getId()){

            case R.id.btnfeedback:
                String comment = edtFeedback.getText().toString();
                float rating = userRating.getRating();
                feedBackService(rating,comment);
                break;
        }
    }

    public void feedBackService(float rating,String comment){

        HashMap<String ,String> map = new HashMap<>();
        map.put(Const.Param.URL,Const.ServiceType.FEEDBACK);
        map.put(Const.Param.USER_ID,preferenceHelper.getUSERID());
        map.put(Const.Param.TOKEN,preferenceHelper.getToken());
        map.put(Const.Param.REQUEST_ID,activeJob.getRequestId());
        map.put(Const.Param.RATING,String.valueOf(rating));
        map.put(Const.Param.COMMENT,comment);

        new HttpRequester(this,map,Const.ServiceCode.FEEDBACK, Const.HttpMethod.POST,this);

       //Andyutils.showCustomProgressDialog(this,false);
    }

    @Override
    public void onTaskCompleted(String response, int serviceCode) {

        switch (serviceCode){

            case Const.ServiceCode.FEEDBACK:
                Andyutils.removeCustomProgressDialog();
                if(parseContent.issucess(response)){
                    sendDatatoActivity();
                    onBackPressed();
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
       sendDatatoActivity();
        super.onBackPressed();
    }

    public void sendDatatoActivity(){
        Intent intent = new Intent();
        intent.putExtra("position",position);
        setResult(Const.CHANGE_IN_DATA,intent);
    }

    private void createInvoiceDialog(){

        double jobAmmountInt = Double.parseDouble(activeJob.getQuotation());
        double adminAmmountInt = Double.parseDouble(activeJob.getAdminPrice());
        AppLog.Log("tag","Admin price - "+activeJob.getAdminPrice());
        String providerRecived =  Andyutils.formatDigitWithPointData(String.valueOf(jobAmmountInt - adminAmmountInt))+
                                    String.valueOf(Html.fromHtml(activeJob.getCurrency()));
        AppLog.Log("tag","job amm - Admin ammo"+jobAmmountInt +"//"+adminAmmountInt);

        String jobAmmount = Andyutils.currencySigns(activeJob.getCurrency())+Andyutils.formatDigitWithPointData(String.valueOf(jobAmmountInt)) ;
        String adminAmmount = Andyutils.currencySigns(activeJob.getCurrency())+Andyutils.formatDigitWithPointData(String.valueOf(adminAmmountInt)) ;
         InvoiceDialog invoiceDialog = new InvoiceDialog(this,jobAmmount,adminAmmount,providerRecived) {
            @Override
            public void onClickButton(View view) {
                dismiss();
            }
        };

        invoiceDialog.show();
    }
}
