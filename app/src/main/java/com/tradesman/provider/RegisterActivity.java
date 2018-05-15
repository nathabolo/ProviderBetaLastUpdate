package com.tradesman.provider;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tradesman.provider.dialog.CountryCodeDialog;
import com.tradesman.provider.model.CountryCode;
import com.tradesman.provider.parse.MultiPartRequester;
import com.tradesman.provider.utils.AppLog;
import com.tradesman.provider.utils.Const;
import com.jimmiejobscreative.tradesman.provider.R;;
import com.tradesman.provider.parse.AsyncTaskCompleteListener;
import com.tradesman.provider.utils.Andyutils;
import com.tradesman.provider.utils.Validation;
import com.soundcloud.android.crop.Crop;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import javax.mail.AuthenticationFailedException;
import javax.mail.MessagingException;


public class RegisterActivity extends ActionBarBaseActivity implements View.OnClickListener,AsyncTaskCompleteListener {

    private EditText edtName;
    private EditText edtPassword;
    private EditText edtEmail;
    private EditText edtConfirmPass;
    private EditText edtPhoneNumber;
    private String getName,description;
    private String getPassword;
    private String getEmail;
    private String getPhoneNum;
    private String getCountryCode;
    private String getAddress;
    private static final int CHOOSE_PHOTO = 1121;
    private static final int TAKE_PHOTO = 1122;
    private Uri uri = null;
    private EditText edtAddress;
    private EditText edtRegisterUDescription;
    private ImageView imgUserPhoto;
    private TextView txtSigninText;
    private String filePath = null;
    private TextView txtCountryCode;
    private CountryCodeDialog countryCodeDialog;
    private List<CountryCode> countryCodeArrayList;
    private static String defaultCase = "Default case in Register";
    private int verification_key;
    private int i =0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        checkLocationPermission();
        initRequire();
    }

    public void initRequire() {
        initToolBar();
     //   setToolBarTitle(getString(R.string.toolbar_register));
        btnDrawerToggle.setVisibility(View.INVISIBLE);
        txtSigninText = (TextView) findViewById(R.id.txtSigninText);
        txtSigninText.setOnClickListener(this);
        edtAddress = (EditText) findViewById(R.id.edtAddressRegister);
        Button btnDoRegister = (Button) findViewById(R.id.btnRegister);
        edtName = (EditText) findViewById(R.id.edtRegisterUseName);
        edtRegisterUDescription = (EditText) findViewById(R.id.edtRegisterUDescription);
        edtEmail = (EditText) findViewById(R.id.edtRegisterEmail);
        edtConfirmPass = (EditText) findViewById(R.id.edtRegisterConformPassword);
        edtPassword = (EditText) findViewById(R.id.edtRegisterPassword);
        edtPhoneNumber = (EditText) findViewById(R.id.edtRegisterConatctNo);
        imgUserPhoto = (ImageView) findViewById(R.id.imgUserPhoto);
        btnDoRegister.setOnClickListener(this);
        imgUserPhoto.setOnClickListener(this);
        txtCountryCode = (TextView) findViewById(R.id.txtCountryCodeRegister);
        txtCountryCode.setOnClickListener(this);
        countryCodeArrayList = parseContent.parseCountryCode();
        txtCountryCode.setText(getString(R.string.country_code));

    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.btnRegister:
                if(filePath == null) {

                }else {
                    if (isValidate()) {

                        Andyutils.showCustomProgressDialog(this, false);
                        Random r = new Random();
                        verification_key = r.nextInt(2000 - 1000) * r.nextInt(2000 - 1000);
                        i = 1;
                        sendRegisterRequestWithKey(verification_key + "", "");

                    }
                    break;
                }
            case R.id.imgUserPhoto:
                showPictureChooseDialog();
                break;
            case R.id.txtCountryCodeRegister:
                openCountryCodeDialog();
                break;
            case R.id.txtSigninText:
                Intent intent = new Intent(this, SigninActivity.class);
                startActivity(intent);
                finishAffinity();
                break;
            default:
                AppLog.Log(Const.TAG, defaultCase);
                break;
        }
    }

    public void VerifyDetails(String msg){
        try{
        ImageView imgClose;
        Button btnSubmit,btnCancel;
        final Dialog buyTokensDialog = new Dialog(RegisterActivity.this);
        buyTokensDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        buyTokensDialog.setContentView(R.layout.verification_dialog);
        buyTokensDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.MATCH_PARENT);
        buyTokensDialog.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.color_white_lessOpacity)));
        btnSubmit = (Button) buyTokensDialog.findViewById(R.id.btnBuyTokens);
        btnCancel = (Button) buyTokensDialog.findViewById(R.id.btnCancel);
        final EditText key = (EditText) buyTokensDialog.findViewById(R.id.edtVerification_key);


        imgClose = (ImageView) buyTokensDialog.findViewById(R.id.imgCLosePopup);
        TextView txtAdminPrice = (TextView) buyTokensDialog.findViewById(R.id.txtadminPriceDialog);
        txtAdminPrice.setText(msg);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Andyutils.removeCustomProgressDialog();
                buyTokensDialog.dismiss();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
        if(key.getText().toString().equals(verification_key+"")) {
            Andyutils.showCustomProgressDialog(RegisterActivity.this , false);
            if(i == 1){
                buyTokensDialog.dismiss();
                i = 2;
                Andyutils.showCustomProgressDialog(RegisterActivity.this, false);
                Random r = new Random();
                verification_key = r.nextInt(2000 - 1000) * r.nextInt(2000 - 1000);
                sendRegisterRequestWithKey(verification_key+"","sms");
            }
            else if(i == 2){
                sendRegisterRequest();
                buyTokensDialog.dismiss();
            }
            //VerifyContact();
        }else{
            Toast.makeText(RegisterActivity.this, "Invalid Verification Key\n Please Try Again", Toast.LENGTH_SHORT).show();
        }
            }
        });
        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Andyutils.removeCustomProgressDialog();
              buyTokensDialog.dismiss();
            }
        });
        buyTokensDialog.show();
    }catch (Exception e) {
        }
        }

    public void openCountryCodeDialog() {
        countryCodeDialog = new CountryCodeDialog(this, countryCodeArrayList) {
            @Override
            public void select(View view, int position) {
                TextView txtCode = (TextView) view.findViewById(R.id.txtcountryPhoneCode);
                String getCode = txtCode.getText().toString();
                txtCountryCode.setText(getCode);
                countryCodeDialog.dismiss();
            }
        };
        countryCodeDialog.show();
    }


    public boolean isValidate() {

        String msg = null;
        getName = edtName.getText().toString();
        description = edtRegisterUDescription.getText().toString();
        getEmail = edtEmail.getText().toString();
        getPassword = edtPassword.getText().toString();
        String getConfirmPassword = edtConfirmPass.getText().toString();
        getAddress = edtAddress.getText().toString();
        getPhoneNum = edtPhoneNumber.getText().toString();
        getCountryCode = txtCountryCode.getText().toString();
        if (TextUtils.isEmpty(getName)) {
            msg = getString(R.string.error_name_require);
            edtName.requestFocus();
        } else if (TextUtils.isEmpty(getEmail)) {
            msg = getString(R.string.error_email_require);
            edtEmail.requestFocus();
        } else if (!Validation.eMailValidation(getEmail)) {
            msg = getString(R.string.error_emal_valid_require);
            edtEmail.requestFocus();
        } else if (TextUtils.isEmpty(getPassword)) {
            msg = getString(R.string.error_password_require);
            edtPassword.requestFocus();
        } else if (getPassword.length() < 6) {
            msg = getString(R.string.error_password_min_require);
            edtPassword.requestFocus();
        } else if (TextUtils.isEmpty(getConfirmPassword)) {
            msg = getString(R.string.error_confirm_password_require);
            edtPassword.requestFocus();
        } else if (!TextUtils.equals(getConfirmPassword, getPassword)) {
            msg = getString(R.string.error_password_not_matched);
            edtConfirmPass.setText("");
            edtPassword.requestFocus();
        } else if (TextUtils.isEmpty(getPhoneNum)) {
            msg = getString(R.string.error_phon_number_reauire);
            edtPhoneNumber.requestFocus();
        } else if (TextUtils.isEmpty(getAddress)) {
            msg = getString(R.string.error_address_require);
            edtAddress.requestFocus();
        }

        if (msg != null) {
            Andyutils.showToast(this, msg);
            return false;
        } else {
            return true;
        }


    }

    public void sendRegisterRequestWithKey(String key,String sms) {


        HashMap<String, String> map = new HashMap<>();

        map.put(Const.Param.URL, Const.ServiceType.REGISTER);
        map.put(Const.Param.NAME, getName);
        map.put("description", "");
        map.put(Const.Param.EMAIL, getEmail);
        map.put(Const.Param.PASS, getPassword);
        map.put(Const.Param.PHONE, getPhoneNum);
        map.put(Const.Param.ADDRESS, getAddress);
        map.put(Const.Param.DEVICE_TYPE, Const.Param.ANDROID_DEVICE);
        map.put(Const.Param.APP_VERSION, Const.Param.AVILABLE_APP_VERSION);
        map.put(Const.Param.DEVICE_TOKEN, preferenceHelper.getDeviceToken());
        map.put(Const.Param.PICTURE, filePath);
        map.put(Const.Param.DEVICE_TOKEN, preferenceHelper.getDeviceToken());
        map.put(Const.Param.COUNTRY_CODE, getCountryCode);
        map.put("verification_key", key);
        map.put("sms", sms);
        new MultiPartRequester(this, map, Const.ServiceCode.REGISTER, this);
    }



    public void sendRegisterRequest() {

        HashMap<String, String> map = new HashMap<>();

        map.put(Const.Param.URL, Const.ServiceType.REGISTER);
        map.put(Const.Param.NAME, getName);
        map.put("description", description);
        map.put(Const.Param.EMAIL, getEmail);
        map.put(Const.Param.PASS, getPassword);
        map.put(Const.Param.PHONE, getPhoneNum);
        map.put(Const.Param.ADDRESS, getAddress);
        map.put(Const.Param.DEVICE_TYPE, Const.Param.ANDROID_DEVICE);
        map.put(Const.Param.APP_VERSION, Const.Param.AVILABLE_APP_VERSION);
        map.put(Const.Param.DEVICE_TOKEN, preferenceHelper.getDeviceToken());
        map.put(Const.Param.PICTURE, filePath);
        map.put(Const.Param.DEVICE_TOKEN, preferenceHelper.getDeviceToken());
        map.put(Const.Param.COUNTRY_CODE, getCountryCode);
        map.put("verification_key", "");


        new MultiPartRequester(this, map, Const.ServiceCode.REGISTER, this);
    }

    @Override
    public void onTaskCompleted(String response, int serviceCode) {
//        finish();
//        Intent intent = new Intent(this, SpleshScreenActivity.class);
//        startActivity(intent);

        try {
            if (response.equals("") && i == 1) {

                VerifyDetails("Thank you for creating a JimmieJobs Service Provider Account, to complete the process please enter the verification key that has been sent to "+edtEmail.getText().toString());
                i = 2;
            }

            else if (response.equals("") && i == 2) {

                VerifyDetails("Please enter the final verification key that has been sent to "+edtPhoneNumber.getText().toString());
            }

            else if(response.length()>4){

                finish();
                Intent i = new Intent(RegisterActivity.this, SigninActivity.class);
                startActivity(i);
            }
        }catch (Exception e){

            VerifyDetails("Thank you for creating a JimmieJobs Service Provider Account, to complete the process please enter the verification key that has been sent to "+edtEmail.getText().toString());
        }

    }

    public void goToProviderTypeActivity() {
        Intent intent = new Intent(this, ProviderTypeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


    //It shows the dialog to select photo from gallery or camera....
    private void showPictureChooseDialog() {

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
                                AppLog.Log(Const.TAG, defaultCase);
                                break;
                        }
                    }
                });
        pictureDialog.show();

    }

    //It choose photo from gallery...
    private void choosePhotoFromGallery() {

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, Const.PERMISSION_STORAGE_REQUEST_CODE);
        } else {
            Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
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
            if (file.exists()) {
                boolean isDelete = file.delete();
                AppLog.Log(Const.TAG, "Deleted " + isDelete);
            } else {
                try {
                    boolean isCreate = file.createNewFile();
                    AppLog.Log(Const.TAG, "Created" + isCreate);
                } catch (IOException e) {
                    AppLog.Log(Const.TAG, Const.EXCEPTION + e);
                }
            }

            uri = Uri.fromFile(file);
            Intent i = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            i.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            startActivityForResult(i, TAKE_PHOTO);

        }
    }

    private void beginCrop(Uri source) {
        Uri outputUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(),
                Calendar.getInstance().getTimeInMillis() + ".jpg"));
        Crop.of(source, outputUri).asSquare().start(this);
    }

    private void handleCrop(int resultCode, Intent result) {
        if (resultCode == RESULT_OK) {

            filePath = Andyutils.getRealPathFromURI(Crop.getOutput(result), this);
            imgUserPhoto.setImageURI(Crop.getOutput(result));
        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(this, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CHOOSE_PHOTO) {
            onRequestCodeChosePhoto(data);
        } else if (requestCode == TAKE_PHOTO) {
            try {
                onRequestTakePhoto();
            } catch (IOException e) {
                AppLog.Log(Const.TAG, Const.EXCEPTION + e);
            }
        } else if (requestCode == Crop.REQUEST_CROP) {
            handleCrop(resultCode, data);
        }

    }

    public void onRequestCodeChosePhoto(Intent data) {
        Bitmap photoBitmap;
        int rotationAngle;
        Uri getUri;
        if (data != null) {
            getUri = data.getData();
            filePath = Andyutils.getRealPathFromURI(getUri, this);

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
            intoCrop(photoBitmap, null, rotationAngle, false);


        } else {
            Toast.makeText(
                    this,
                    getResources().getString(
                            R.string.error_select_image),
                    Toast.LENGTH_LONG).show();
        }

    }

    public int getRatio(int ratio) {
        if (ratio == 0) {
            return 1;
        }
        return 0;
    }

    public void onRequestTakePhoto() throws IOException {

        int rotationAngle;
        if (uri != null) {
            String imageFilePath = uri.getPath();
            if (imageFilePath != null && imageFilePath.length() > 0) {

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

                outStream.close();
            }

        } else {
            Toast.makeText(
                    this,
                    getResources().getString(
                            R.string.error_select_image),
                    Toast.LENGTH_LONG).show();
        }

    }


    public void intoCrop(Bitmap bitmap, OutputStream outStream, int rotationAngle, boolean isCompressed) {

        if (isCompressed && bitmap != null) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100,
                    outStream);

        }
        if (bitmap != null) {
            beginCrop(Uri.parse(Andyutils.getPathForBitmap(bitmap, this, rotationAngle)));
        }

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, SpleshScreenActivity.class);
        startActivity(intent);
        finishAffinity();
    }

    class SendEmailAsyncTask extends AsyncTask<Void, Void, Boolean> {

        Mail m;
        RegisterActivity activity;

        public SendEmailAsyncTask() {
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                if (m.send()) {
                    //Toast.makeText(activity, "Sent", Toast.LENGTH_SHORT).show();
                } else {
                    //Toast.makeText(activity, "Failed to send", Toast.LENGTH_SHORT).show();
                }

                return true;
            } catch (AuthenticationFailedException e) {
                Log.e(SendEmailAsyncTask.class.getName(), "Bad account details");
                e.printStackTrace();
//                Toast.makeText(activity, "Authentication failed", Toast.LENGTH_SHORT).show();
                return false;
            } catch (MessagingException e) {
                Log.e(SendEmailAsyncTask.class.getName(), "Email failed");
                e.printStackTrace();
                Toast.makeText(activity, "Failed to send", Toast.LENGTH_SHORT).show();
                return false;
            } catch (Exception e) {
//                e.printStackTrace();
//                Toast.makeText(activity, "Unexpected error", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
    }
}
