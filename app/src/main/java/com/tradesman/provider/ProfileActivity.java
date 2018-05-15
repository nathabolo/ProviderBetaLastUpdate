package com.tradesman.provider;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.share.Sharer;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.tradesman.provider.dialog.CountryCodeDialog;
import com.tradesman.provider.dialog.CustomDialog;
import com.tradesman.provider.model.Statistics;
import com.tradesman.provider.parse.HttpRequester;
import com.tradesman.provider.parse.MultiPartRequester;
import com.tradesman.provider.utils.AppLog;
import com.tradesman.provider.utils.Const;
import com.tradesman.provider.utils.Validation;
import com.jimmiejobscreative.tradesman.provider.R;;
import com.tradesman.provider.parse.AsyncTaskCompleteListener;
import com.tradesman.provider.utils.Andyutils;
import com.soundcloud.android.crop.Crop;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.HashMap;

public class ProfileActivity extends ActionBarBaseActivity implements AsyncTaskCompleteListener, View.OnClickListener {

    private TextView txtUserName;
    private TextView userService;
    private TextView txtmemberSince;
    private TextView txtupappointments;
    private TextView txtweekappointments;
    private TextView txtyearappointments;
    private TextView txtweekearning;
    private TextView txtyearEarning;
    private TextView txttotalAppointments;
    private TextView txtTotalEarning;
    private Button btnstastistics;
    private Button btnprofile;
    private ScrollView statisticsLayout;
    private LinearLayout profileEditstatistics;
    private ImageView imgUser;
    private static final int CHOOSE_PHOTO = 1121;
    private static final int TAKE_PHOTO = 1122;

    private Uri uri = null;
    public String filePath = null;
    public EditText edtName;
    public EditText edtPassword;
    public EditText edtConfirmPass;
    public EditText edtNumber;
    public EditText edtEmail;
    public EditText edtAddress;
    private EditText edtRegisterUDescription;
    private TextView txtCountry;
    private CustomDialog confirmDialog;
    private String getName;
    private String getpassword;
    private String getNumber;
    private String getCountrycode;
    private String getEmail;
    private String getAddress, description;
    private String defaultCase = "Default case in PRofile Activity";
    public int i = 0;
    public int ii = 0;
    public CallbackManager callbackManager;
    public ShareDialog shareDialog;
    public String opt = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_activity);

        initRequire();
        updateUserDataService("", "");
        try {
            Intent iin = getIntent();
            Bundle b = iin.getExtras();
            String extra_str = b.getString("share");

            if (extra_str.equals("share")) {
                shareYourContentOnFacebook();
            } else if (extra_str.equals("buy_tokens")) {
                i = 1;
                int amnt = (int) b.get("amnt");
                String c_no = (String) b.get("c_no");
                String cvv = (String) b.get("cvv");
                String exp_yr = (String) b.get("exp_yr");
                String exp_m = (String) b.get("exp_m");
                String c_typ = (String) b.get("c_typ");
                HashMap<String, String> map = new HashMap<>();
                map.put(Const.Param.URL, Const.ServiceType.UPDATE_PROFILE);
                map.put("description", "");
                map.put(Const.Param.NAME, preferenceHelper.getUserName());
                map.put(Const.Param.ADDRESS, preferenceHelper.getAddress());
                map.put(Const.Param.EMAIL, preferenceHelper.getEmail());
                map.put(Const.Param.USER_ID, preferenceHelper.getUSERID());
                map.put(Const.Param.TOKEN, preferenceHelper.getToken());
                map.put(Const.Param.PHONE, preferenceHelper.getPhoneNumber());
                map.put(Const.Param.COUNTRY_CODE, preferenceHelper.getCountryCode());
                map.put("add_card", "buy_tokens");
                map.put("buy", amnt + "");
                map.put("c_no", c_no);
                map.put("cvv", cvv);
                map.put("exp_yr", exp_yr);
                map.put("exp_m", exp_m);
                map.put("c_typ", c_typ);
                if (!TextUtils.isEmpty("")) {
                    map.put(Const.Param.NEW_PASS, getpassword);
                    map.put(Const.Param.OLD_PASS, "");
                }
                map.put(Const.Param.PICTURE, filePath);

                new MultiPartRequester(this, map, Const.ServiceCode.UPDATE_PROFILE, this);

                Andyutils.showCustomProgressDialog(this, false);
            }

            if (b != null) {

            }

        } catch (Exception e) {

        }
    }


    public void shareYourContentOnFacebook() {
        try {
            AlertDialog.Builder builder;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder = new AlertDialog.Builder(ProfileActivity.this, android.R.style.Theme_Material_Dialog_Alert);
            } else {
                builder = new AlertDialog.Builder(ProfileActivity.this);
            }
            builder.setTitle("Share JimmieJobs On Facebook")
                    .setMessage("By Sharing JimmieJobs with Your Friends You Earn Free Tokens to use")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            shareYourContentOnFacebook2();

                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();


        } catch (Exception e) {
            Toast.makeText(ProfileActivity.this, "" + e, Toast.LENGTH_SHORT).show();
        }
    }

    private void shareYourContentOnFacebook1() {
        try {
            callbackManager = CallbackManager.Factory.create();
            ShareDialog shareDialog = new ShareDialog(this);
            shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
                @Override
                public void onSuccess(Sharer.Result result) {
                    HashMap<String, String> map = new HashMap<>();
                    map.put(Const.Param.URL, Const.ServiceType.UPDATE_PROFILE);
                    map.put("description", "");
                    map.put(Const.Param.NAME, preferenceHelper.getUserName());
                    map.put(Const.Param.ADDRESS, preferenceHelper.getAddress());
                    map.put(Const.Param.EMAIL, preferenceHelper.getEmail());
                    map.put(Const.Param.USER_ID, preferenceHelper.getUSERID());
                    map.put(Const.Param.TOKEN, preferenceHelper.getToken());
                    map.put(Const.Param.PHONE, preferenceHelper.getPhoneNumber());
                    map.put(Const.Param.COUNTRY_CODE, preferenceHelper.getCountryCode());
                    map.put("share", "share");

                    if (!TextUtils.isEmpty("")) {
                        map.put(Const.Param.NEW_PASS, getpassword);
                        map.put(Const.Param.OLD_PASS, "");
                    }
                    map.put(Const.Param.PICTURE, filePath);

                    new MultiPartRequester(ProfileActivity.this, map, Const.ServiceCode.UPDATE_PROFILE, ProfileActivity.this);

                    Andyutils.showCustomProgressDialog(ProfileActivity.this, false);
                }

                @Override
                public void onCancel() {
                    Log.d(this.getClass().getSimpleName(), "sharing cancelled");
                    //add your code to handle cancelled sharing
                    Toast.makeText(ProfileActivity.this, "Cancelled", Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onError(FacebookException error) {
                    Log.d(this.getClass().getSimpleName(), "sharing error");
                    //add your code to handle sharing error
                    Toast.makeText(ProfileActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
            });

            if (ShareDialog.canShow(ShareLinkContent.class)) {

                ShareLinkContent shareLinkContent = new ShareLinkContent.Builder()
                        .setContentTitle("Share JimmieJobs To Earn Tokens")
                        .setContentDescription("JimmieJobs")
                        .setContentUrl(Uri.parse("https://www.facebook.com/JimmieJobs/?ref=br_rs"))
                        .build();

                shareDialog.show(shareLinkContent);

            }

        } catch (Exception e) {
        }
    }

    private void shareYourContentOnFacebook2() {

        callbackManager = CallbackManager.Factory.create();
        ShareDialog shareDialog = new ShareDialog(this);
        shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
            @Override
            public void onSuccess(Sharer.Result result) {
                HashMap<String, String> map = new HashMap<>();
                map.put(Const.Param.URL, Const.ServiceType.UPDATE_PROFILE);
                map.put("description", "");
                map.put(Const.Param.NAME, preferenceHelper.getUserName());
                map.put(Const.Param.ADDRESS, preferenceHelper.getAddress());
                map.put(Const.Param.EMAIL, preferenceHelper.getEmail());
                map.put(Const.Param.USER_ID, preferenceHelper.getUSERID());
                map.put(Const.Param.TOKEN, preferenceHelper.getToken());
                map.put(Const.Param.PHONE, preferenceHelper.getPhoneNumber());
                map.put(Const.Param.COUNTRY_CODE, preferenceHelper.getCountryCode());
                map.put("share", "share");

                if (!TextUtils.isEmpty("")) {
                    map.put(Const.Param.NEW_PASS, getpassword);
                    map.put(Const.Param.OLD_PASS, "");
                }
                map.put(Const.Param.PICTURE, filePath);

                new MultiPartRequester(ProfileActivity.this, map, Const.ServiceCode.UPDATE_PROFILE, ProfileActivity.this);

                Andyutils.showCustomProgressDialog(ProfileActivity.this, false);
            }

            @Override
            public void onCancel() {
                Log.d(this.getClass().getSimpleName(), "sharing cancelled");
                //add your code to handle cancelled sharing
                Toast.makeText(ProfileActivity.this, "Cancelled", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onError(FacebookException error) {
                Log.d(this.getClass().getSimpleName(), "sharing error");
                //add your code to handle sharing error
                Toast.makeText(ProfileActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });

        if (ShareDialog.canShow(ShareLinkContent.class)) {

            ShareLinkContent shareLinkContent = new ShareLinkContent.Builder()
                    .setContentTitle("Share JimmieJobs To Earn Tokens")
                    .setContentDescription("JimmieJobs")
                    .setContentUrl(Uri.parse("https://www.facebook.com/jimmiejobs/"))
                    .build();

            shareDialog.show(shareLinkContent);

        }

    }

    public void initRequire(){
        initToolBar();
        try{
        //setToolBarTitle(getString(R.string.profile));
        btnDrawerToggle.setVisibility(View.INVISIBLE);
        txtUserName = (TextView) findViewById(R.id.txtprofileUser);
        userService = (TextView) findViewById(R.id.txtprofileService);
        txtmemberSince = (TextView) findViewById(R.id.txtprofileDate);
        txtupappointments = (TextView) findViewById(R.id.txtprofileupcoming);
        txtweekappointments = (TextView) findViewById(R.id.txtprofileThisweek);
        txtyearappointments = (TextView) findViewById(R.id.txtprofileThisYear);
        txtweekearning = (TextView) findViewById(R.id.txtprofileweekrate);
        txtyearEarning = (TextView) findViewById(R.id.txtprofileyearrate);
        txtTotalEarning = (TextView) findViewById(R.id.txtProfileTotalEarning);
        txttotalAppointments = (TextView) findViewById(R.id.txtprofiletotalappointments);
        btnstastistics = (Button) findViewById(R.id.btnprofileSta);
        btnprofile = (Button) findViewById(R.id.btnProfileEdit);
        statisticsLayout = (ScrollView) findViewById(R.id.layoutstatistics);
        profileEditstatistics = (LinearLayout) findViewById(R.id.layoutProfile);
        imgUser = (ImageView) findViewById(R.id.imgProfile);
        imgUser.setOnClickListener(this);
        btnstastistics.setOnClickListener(this);
        btnprofile.setOnClickListener(this);
        statisticsService();

        edtName = (EditText) findViewById(R.id.edtUpdateUserName);
        edtRegisterUDescription = (EditText) findViewById(R.id.edtRegisterUDescription);
        edtEmail = (EditText) findViewById(R.id.edtUpdateEmail);
        edtPassword = (EditText) findViewById(R.id.edtnewPass);
        edtNumber = (EditText) findViewById(R.id.edtRegisterConatctNo);
        Button btnUpdate = (Button) findViewById(R.id.btnUpdateProfile);
        txtCountry = (TextView) findViewById(R.id.txtCountryCode);
        txtCountry.setOnClickListener(this);
        edtAddress = (EditText) findViewById(R.id.edtaddressUpdate);
        edtConfirmPass = (EditText) findViewById(R.id.edtUpdatePassword);
        btnUpdate.setOnClickListener(this);
        }catch (Exception e){

        }
    }

    public void statisticsService(){

        HashMap<String,String > map = new HashMap<>();
        map.put(Const.Param.URL,Const.ServiceType.STATISTICS);
        map.put(Const.Param.USER_ID,preferenceHelper.getUSERID());
        map.put(Const.Param.TOKEN,preferenceHelper.getToken());
        map.put(Const.Param.TIME_ZONE,preferenceHelper.getDeviceTimeZone());

        new HttpRequester(this,map,Const.ServiceCode.STATISTICS, Const.HttpMethod.POST,this);

        Andyutils.showCustomProgressDialog(this,false);
    }


    public void setStatisticsData(String response){
        try{
        Statistics statistics = parseContent.parseStatistics(response);
        String getTime = Andyutils.formateDate(statistics.getMemberSince(),false);
        String[] splitDate = getTime.split(",");
        txtmemberSince.setText(splitDate[0]);
        txtupappointments.setText(statistics.getUpcomming());
        txtTotalEarning.setText(String.valueOf(Html.fromHtml(statistics.getCurrency()))+statistics.getTotalEarning() );
        txttotalAppointments.setText(statistics.getTotalJobs());
        txtyearappointments.setText(statistics.getYearJobs());
        txtyearEarning.setText(String.valueOf(Html.fromHtml(statistics.getCurrency()))+statistics.getYearEarning());
        txtweekappointments.setText(statistics.getWeekJobs());
        txtweekearning.setText(String.valueOf(Html.fromHtml(statistics.getCurrency()))+statistics.getWeekEarning() );
        AppLog.Log("tag","User services"+preferenceHelper.getSelectedType());
        userService.setText(preferenceHelper.getUserToken()+" Token(s)");
        txtUserName.setText(preferenceHelper.getUserName());
       setUserImage(preferenceHelper.getUserImagePath());
    }catch (Exception e) {
        }
        }


    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.btnprofileSta:
                onClickStatstics();
                break;

            case R.id.btnProfileEdit:
                onClickProfileEdit();
                break;

            case R.id.btnUpdateProfile:
                onClickUpdateProfile();
                break;

            case R.id.imgProfile:
                showPictureChooseDialog();
                break;

            case R.id.txtCountryCode:
                setCountryCode();
                break;

            default:
                AppLog.Log(Const.TAG, defaultCase);
                break;
        }
    }

    public void onClickStatstics(){
        statisticsLayout.setVisibility(View.VISIBLE);
        Andyutils.hideKeybord(this);
        btnprofile.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        btnstastistics.setBackgroundColor(getResources().getColor(R.color.color_green_dark));
    }

    public void onClickProfileEdit(){
        btnprofile.setBackgroundColor(getResources().getColor(R.color.color_green_dark));
        btnstastistics.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        statisticsLayout.setVisibility(View.GONE);
        profileEditstatistics.setVisibility(View.VISIBLE);
        setuserData();
    }
    public void onClickUpdateProfile(){
        if(getALlData()){
            if(TextUtils.isEmpty(getpassword)){
                description = edtRegisterUDescription.getText().toString();
                updateProfileService("");
            }else {
                createConfirmUpdateDialog();
            }
        }
    }

    public void setuserData(){
        try {
            edtName.setText(preferenceHelper.getUserName());
            edtAddress.setText(preferenceHelper.getAddress());
            edtNumber.setText(preferenceHelper.getPhoneNumber());
            edtEmail.setText(preferenceHelper.getEmail());
            edtAddress.setText(preferenceHelper.getAddress());
            AppLog.Log("tag", "addresss - " + preferenceHelper.getAddress());
            txtCountry.setText(preferenceHelper.getCountryCode());
        }catch (Exception e){

        }

    }

    public boolean getALlData(){

            String msg = null;
            getName = edtName.getText().toString();
            getAddress = edtAddress.getText().toString();
            getEmail = edtEmail.getText().toString();
            getCountrycode = txtCountry.getText().toString();
            getpassword = edtPassword.getText().toString();
            String getCofirmPass = edtConfirmPass.getText().toString();
            getNumber = edtNumber.getText().toString();

            if (TextUtils.isEmpty(getName)) {
                msg = getString(R.string.error_name_require);
                edtName.requestFocus();
            } else if (TextUtils.isEmpty(getEmail)) {
                msg = getString(R.string.error_email_require);
                edtEmail.requestFocus();
            } else if (!Validation.eMailValidation(getEmail)) {
                msg = getString(R.string.error_emal_valid_require);
                edtEmail.requestFocus();
            } else if (!TextUtils.isEmpty(getpassword) && getpassword.length() < 6) {
                msg = getString(R.string.error_password_min_require);
                edtPassword.requestFocus();
            } else if (!TextUtils.equals(getpassword, getCofirmPass)) {
                msg = getString(R.string.error_password_not_matched);
                edtConfirmPass.setText("");
                edtPassword.requestFocus();
            } else if (TextUtils.isEmpty(getAddress)) {
                msg = getString(R.string.error_address_require);
                edtAddress.requestFocus();
            } else if (TextUtils.isEmpty(getNumber)) {
                msg = getString(R.string.error_phon_number_reauire);
                edtNumber.requestFocus();
            }

        if (msg != null){
            Andyutils.showToast(this,msg);
            return false;
        }else {
            return true;
        }

    }

    public void updateProfileService(String oldPassword){
    try{
        HashMap<String,String> map = new HashMap<>();
        map.put(Const.Param.URL,Const.ServiceType.UPDATE_PROFILE);
        map.put("description", description);
        map.put(Const.Param.NAME,getName);
        map.put(Const.Param.ADDRESS,getAddress);
        map.put(Const.Param.EMAIL,getEmail);
        map.put(Const.Param.USER_ID,preferenceHelper.getUSERID());
        map.put(Const.Param.TOKEN,preferenceHelper.getToken());
        map.put(Const.Param.PHONE,getNumber);
        map.put(Const.Param.COUNTRY_CODE,getCountrycode);

        if(!TextUtils.isEmpty(oldPassword)){
            map.put(Const.Param.NEW_PASS,getpassword);
            map.put(Const.Param.OLD_PASS, oldPassword);
        }
        map.put(Const.Param.PICTURE,filePath);

        new MultiPartRequester(this,map, Const.ServiceCode.UPDATE_PROFILE,this);

        Andyutils.showCustomProgressDialog(this,false);
    }catch (Exception e) {
    }
    }

    public void updateUserDataService(String oldPassword, String description){
        try{
        ii =1;
        HashMap<String,String> map = new HashMap<>();
        map.put(Const.Param.URL,Const.ServiceType.UPDATE_PROFILE);
        map.put(Const.Param.NAME,preferenceHelper.getUserName());
        map.put("description", description);
        map.put(Const.Param.ADDRESS,preferenceHelper.getAddress());
        map.put(Const.Param.EMAIL,preferenceHelper.getEmail());
        map.put(Const.Param.USER_ID,preferenceHelper.getUSERID());
        map.put(Const.Param.TOKEN,preferenceHelper.getToken());
        map.put(Const.Param.PHONE,preferenceHelper.getPhoneNumber());
        map.put(Const.Param.COUNTRY_CODE,getCountrycode);

        if(!TextUtils.isEmpty(oldPassword)){
            map.put(Const.Param.NEW_PASS,getpassword);
            map.put(Const.Param.OLD_PASS, oldPassword);
        }
        map.put(Const.Param.PICTURE,filePath);

        new MultiPartRequester(this,map, Const.ServiceCode.UPDATE_PROFILE,this);

        Andyutils.showCustomProgressDialog(this,false);
    }catch (Exception e) {
        }
        }



    @Override
    public void onTaskCompleted(String response, int serviceCode) {

        switch (serviceCode){

            case Const.ServiceCode.STATISTICS:
                Andyutils.removeCustomProgressDialog();
                if(parseContent.issucess(response)){
                    setStatisticsData(response);
                }
                break;
            case Const.ServiceCode.UPDATE_PROFILE:
                Andyutils.removeCustomProgressDialog();
                if(parseContent.isSuccessWithStoreId(response)){
                 onTaskCompleteUpdateProfile();
                    setuserData();
                    Toast.makeText(ProfileActivity.this, "Update Successful", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                AppLog.Log(Const.TAG,defaultCase);
                break;
        }
    }

    public void onTaskCompleteUpdateProfile(){
        if(confirmDialog != null && confirmDialog.isShowing()){
            confirmDialog.dismiss();
        }
        setuserData();
        String xx = preferenceHelper.getTokenPurchase();

        if(xx.equals("")) {
            i = 6;
            if(opt.equals("add_card")){
                i = 2;
            }
            else if(opt.equals("share")){
                i = 3;
            }
        }

        else{
            i = 5;
            Intent show_avail = new Intent(ProfileActivity.this,AvailableJobActivity.class);
            startActivity(show_avail);
        }
        txtUserName.setText(preferenceHelper.getUserName());
        userService.setText(preferenceHelper.getUserToken()+"Token(s)");
        preferenceHelper.putUserImagePath(filePath);
//        if(i == 5) {
//        boolean x = preferenceHelper.getTokenPurchase().contains("Request successfully processed");
//                Andyutils.showToast(this, x ? "Transaction Successful" : "Transaction Failed");
//                Intent show_avail = new Intent(ProfileActivity.this,AvailableJobActivity.class);
//                startActivity(show_avail);
//            i = 0;
//        }else
            if(i == 2){
             Andyutils.showToast(this,"Successful");
            Intent show_avail = new Intent(ProfileActivity.this,AvailableJobActivity.class);
            startActivity(show_avail);
             i = 0;
        }else if(i == 3){
            Andyutils.showToast(this,"1 Token Earned");
            i = 0;
        }
        else if(ii == 1 && i == 6){
            //Andyutils.showToast(this,"Profile Updated");
            ii = 0;
            i = 0;
        }
        //   onBackPressed();

    }
    public void createConfirmUpdateDialog(){

        confirmDialog = new CustomDialog(this,getString(R.string.verify_account),getString(R.string.profile_update_ok),
                getString(R.string.CANCEL),getString(R.string.current_password)) {
            @Override
            public void OnpositiveButtonClick(String edtData) {
                if(Validation.eMailValidation(edtData)){
                    updateProfileService(edtData);
                } else {
                    Andyutils.showToast(getApplicationContext(),getString(R.string.error_email));
                }
            }

            @Override
            public void OnnegativeButtonClick() {
                dismiss();
            }
        };
        confirmDialog.show();
    }

    public void setCountryCode(){
        CountryCodeDialog codeDialog = new CountryCodeDialog(this,parseContent.parseCountryCode()) {
            @Override
            public void select(View view, int position) {
                TextView txtCode = (TextView) view.findViewById(R.id.txtcountryPhoneCode);
                String getCode = txtCode.getText().toString();
                txtCountry.setText(getCode);
                dismiss();
            }
        };

        codeDialog.show();
    }



    private void showPictureChooseDialog(){

        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle(getResources().getString(
                R.string.dialog_chose_picture));
        String[] pictureDialogItems = {
                getResources().getString(R.string.dialog_chose_gallary),
                getResources().getString(R.string.dialog_chose_camera)};

        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int type) {
                        switch (type) {
                            case 0:
                                choosePhotoFromGallery();
                                break;

                            case 1:
                                takePhotoFromCamera();
                                break;
                            default:
                                AppLog.Log(Const.TAG,defaultCase);
                                break;
                        }
                    }
                });
        pictureDialog.show();

    }


    private void choosePhotoFromGallery(){

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, Const.PERMISSION_STORAGE_REQUEST_CODE);
        } else {
            Intent galleryIntent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(galleryIntent, CHOOSE_PHOTO);
        }

    }

    public void takePhotoFromCamera() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, Const.PERMISSION_CAMERA_REQUEST_CODE);
        } else {
            Calendar cal = Calendar.getInstance();
            File file = new File(Environment.getExternalStorageDirectory(),
                    cal.getTimeInMillis() + ".jpg");
            if(file.exists()){
                boolean isDelete = file.delete();
                AppLog.Log(Const.TAG,"Delted"+isDelete);
                try {
                    boolean isCreate = file.createNewFile();
                    AppLog.Log(Const.TAG,"Created"+isCreate);
                } catch (IOException e) {
                    AppLog.Log(Const.TAG,Const.EXCEPTION+e);
                }
            }

            uri = Uri.fromFile(file);
            Intent i = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            i.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            startActivityForResult(i, TAKE_PHOTO);

        }
    }

    private void beginCrop(Uri source) {
        try {
            Uri outputUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), Calendar.getInstance().getTimeInMillis() + ".jpg"));
            Crop.of(source, outputUri).asSquare().start(this);
        }catch (Exception e){
            AppLog.Log("tag","Exxception accures here ");
        }
    }

    private void handleCrop(int resultCode, Intent result) {
        if (resultCode == RESULT_OK) {

            filePath = Andyutils.getRealPathFromURI(Crop.getOutput(result),this);
            setUserImage(filePath);

        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(this, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();

        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CHOOSE_PHOTO) {
            try {
                onRequestCodeChosePhoto(data);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (requestCode == TAKE_PHOTO) {
            try {
                onRequestTakePhoto();
            } catch (IOException e) {
                AppLog.Log(Const.TAG,Const.EXCEPTION+e);
            }
        } else if (requestCode == Crop.REQUEST_CROP) {
            handleCrop(resultCode, data);
        }

    }

    public void setUserImage(String filePath){

        Glide.with(this)
                .load(filePath)
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

    public void onRequestCodeChosePhoto(Intent data) throws IOException {
        Bitmap photoBitmap;
        int rotationAngle;
        Uri getUri;
        if (data != null) {
            getUri = data.getData();
            filePath = Andyutils.getRealPathFromURI(getUri,this);

            int mobileWidth = 480;
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(filePath, options);
            int outWidth = options.outWidth;
            int ratio = (int) ((((float) outWidth) / mobileWidth) + 0.5f);
            ratio = getRatio(ratio);
            rotationAngle = Andyutils.setRotaionValue(filePath);


            options.inJustDecodeBounds = false;
            options.inSampleSize = ratio;
            photoBitmap = BitmapFactory.decodeFile(filePath, options);
            intoCrop(photoBitmap,null,rotationAngle,false);


        } else {
            Toast.makeText(
                    this,
                    getResources().getString(
                            R.string.error_select_image),
                    Toast.LENGTH_LONG).show();
        }

    }

    public void onRequestTakePhoto() throws IOException {

        if (uri != null) {
            String imageFilePath = uri.getPath();
            if (imageFilePath != null && imageFilePath.length() > 0) {

                int rotationAngle;
                int mobileWidth = 480;
                BitmapFactory.Options options = new BitmapFactory.Options();
                int outWidth = options.outWidth;
                int ratio = (int) ((((float) outWidth) / mobileWidth) + 0.5f);

                ratio = getRatio(ratio);
                rotationAngle = Andyutils.setRotaionValue(imageFilePath);

                options.inJustDecodeBounds = false;
                options.inSampleSize = ratio;

                Bitmap bmp = BitmapFactory.decodeFile(imageFilePath,
                        options);
                File myFile = new File(imageFilePath);
                FileOutputStream outStream = new FileOutputStream(
                        myFile);
                intoCrop(bmp, outStream, rotationAngle, true);

            }


        } else {
            Toast.makeText(
                    this,
                    getResources().getString(
                            R.string.error_select_image),
                    Toast.LENGTH_LONG).show();
        }

    }


    public int getRatio(int ratio){
        if(ratio == 0){
            return 1;
        }
        return  0;
    }



    public void intoCrop(Bitmap bitmap, OutputStream outStream, int rotationAngle, boolean isCompressed) throws IOException {

        if(isCompressed && bitmap != null ){
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100,
                    outStream);
            outStream.close();

        }
        if(bitmap != null){
            beginCrop(Uri.parse(Andyutils.getPathForBitmap(bitmap,this,rotationAngle)));
        }


    }

}
