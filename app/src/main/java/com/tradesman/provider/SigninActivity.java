package com.tradesman.provider;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.tradesman.provider.dialog.CustomDialog;
import com.tradesman.provider.utils.AppLog;
import com.tradesman.provider.utils.Const;
import com.jimmiejobscreative.tradesman.provider.R;;
import com.tradesman.provider.parse.AsyncTaskCompleteListener;
import com.tradesman.provider.parse.HttpRequester;
import com.tradesman.provider.utils.Andyutils;
import com.tradesman.provider.utils.Validation;

import java.util.HashMap;


public class SigninActivity extends ActionBarBaseActivity implements View.OnClickListener, AsyncTaskCompleteListener {


   private Button btnSignin;
   private EditText edtEmail,edtPassword;
   private String getEmail,getPassword;
    private Button btnRegister;
    private TextView txtForgetpassword;
    private CustomDialog forgetPassDialog;
    private TextView txtRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in);
        initrequire();
    }

    public void initrequire(){
        initToolBar();
       // setToolBarTitle(getString(R.string.toolbar_sigin_in));
        btnDrawerToggle.setVisibility(View.INVISIBLE);
        btnSignin = (Button) findViewById(R.id.btnSignIn);
        edtEmail = (EditText) findViewById(R.id.edtEmail);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        btnSignin.setOnClickListener(this);
        txtRegister = (TextView) findViewById(R.id.txtRegistertext);
        txtRegister.setOnClickListener(this);

        txtForgetpassword = (TextView) findViewById(R.id.tvForgotPassword);
        txtForgetpassword.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.btnSignIn:
                getEmail = edtEmail.getText().toString();
                getPassword = edtPassword.getText().toString();
                if(TextUtils.isEmpty(getEmail)){
                    Andyutils.showToast(this,getString(R.string.error_email_require));
                }else if(TextUtils.isEmpty(getPassword)){
                    Andyutils.showToast(this,getString(R.string.error_password_require));
                }else {
                    Andyutils.showCustomProgressDialog(this,false);
                    sendloginService();
                }

                break;
            case R.id.txtRegistertext:
                Intent intent = new Intent(this,RegisterActivity.class);
                startActivity(intent);
                finishAffinity();
                break;

            case R.id.tvForgotPassword:
                createForgetpassDialog();
                break;
            default:
                AppLog.Log(Const.TAG,"Default tab sign in");
                break;

        }


    }

    public void sendloginService(){

        HashMap<String,String> map = new HashMap<>();

        map.put(Const.Param.URL,Const.ServiceType.LOGIN);
        map.put(Const.Param.EMAIL,getEmail);
        map.put(Const.Param.PASS,getPassword);
        map.put(Const.Param.DEVICE_TYPE,Const.Param.ANDROID_DEVICE);
        map.put(Const.Param.APP_VERSION,Const.Param.AVILABLE_APP_VERSION);
        map.put(Const.Param.DEVICE_TOKEN,preferenceHelper.getDeviceToken());

        new HttpRequester(this,map,Const.ServiceCode.LOGIN,Const.HttpMethod.POST,this);
    }

    @Override
    public void onTaskCompleted(String response, int serviceCode) {
            switch (serviceCode){

                case Const.ServiceCode.LOGIN:
                    Andyutils.removeCustomProgressDialog();
                    if(parseContent.isSuccessWithStoreId(response)) {
                        boolean istypeSelected = parseContent.isProviderselected(response);
                        preferenceHelper.putisProviderTypeSelected(istypeSelected);
                        if (istypeSelected) {
                            Intent intent = new Intent(this, AvailableJobActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        } else {

                            Intent intent = new Intent(this, AvailableJobActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }

                    }
                    break;

                case Const.ServiceCode.FORGET_PASSWORD:
                    Andyutils.removeCustomProgressDialog();
                    if(parseContent.issucess(response)){
                        forgetPassDialog.dismiss();
                        Andyutils.showToast(getApplicationContext(),getString(R.string.forget_password_done));
                    }
                    break;
                default:
                    AppLog.Log(Const.TAG,"Default case");
                    break;
            }
    }


    public void createForgetpassDialog(){
        forgetPassDialog = new CustomDialog(this,getString(R.string.forget_pass),getString(R.string.send),getString(R.string.CANCEL),getString(R.string.email_hint)) {
            @Override
            public void OnpositiveButtonClick(String edtData) {
                if(Validation.eMailValidation(edtData)){
                    forgetpasswordService(edtData);
                } else {
                    Andyutils.showToast(getApplicationContext(),getString(R.string.error_email));
                }
            }

            @Override
            public void OnnegativeButtonClick() {
                dismiss();
            }
        };
        forgetPassDialog.show();

    }


    public void forgetpasswordService(String email){

        HashMap<String ,String> map = new HashMap<>();
        map.put(Const.Param.URL,Const.ServiceType.FORGET_PASSWORD);
        map.put(Const.Param.USER_TYPE,Const.Param.PROVIDER_DEFAULT_TYPE);
        map.put(Const.Param.EMAIL,email);

        new HttpRequester(this,map,Const.ServiceCode.FORGET_PASSWORD, Const.HttpMethod.POST,this);


        Andyutils.showCustomProgressDialog(this,false);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this,SpleshScreenActivity.class);
        startActivity(intent);
        finishAffinity();
    }
}
