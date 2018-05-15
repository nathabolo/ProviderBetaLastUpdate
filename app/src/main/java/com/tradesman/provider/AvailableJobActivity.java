package com.tradesman.provider;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.tradesman.provider.adapter.AvailableJobAdapter;
import com.tradesman.provider.dialog.CustomDialog;
import com.tradesman.provider.utils.AppLog;
import com.jimmiejobscreative.tradesman.provider.R;;
import com.tradesman.provider.dialog.CustomDialogNoEdt;
import com.tradesman.provider.model.AvailableJob;
import com.tradesman.provider.parse.AsyncTaskCompleteListener;
import com.tradesman.provider.parse.HttpRequester;
import com.tradesman.provider.utils.Andyutils;
import com.tradesman.provider.utils.Const;
import com.tradesman.provider.utils.RecyclerViewTouchListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Pattern;

import com.stripe.android.model.Card;

public class AvailableJobActivity extends ActionBarBaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, AsyncTaskCompleteListener,
        RecyclerViewTouchListener.ClickListener, SwipeRefreshLayout.OnRefreshListener {

    private DrawerLayout drawer;

    private CustomDialogNoEdt approveDialog;
    private List<AvailableJob> availableJobArrayList;
    private Timer updateLocationTimer;
    private CustomDialogNoEdt logoutDialog;
    private TextView txtnoIteam;
    private AvailableJobAdapter availableJobAdapter;
    private ImageView imgNavUser;
    private TextView txtNavName;
    private SwipeRefreshLayout swipeavailablejob;
    CustomDialogNoEdt exitDialog ;
    private static final String AMERICAN_EXPRESS = "AMERICAN EXPRESS";
    private static final String DISCOVER = "Discover";
    private static final String JCB = "JCB";
    private static final String DINERS_CLUB = "Diners Club";
    private static final String VISA = "VISA";
    private static final String MASTERCARD = "MASTERCARD";
    private static final String UNKNOWN = "Unknown";
    public int counter = 0;
    private EditText edtCreditCardNo , edtCardMonth , edtCardYear , edtCardCvv;
    private Button btnAddCard;
    private Dialog dialogAddCard;
    private String cardType;
    private static final Pattern CODE_PATTERN = Pattern.compile("([0-9]{0,4})|([0-9]{4}-)+|([0-9]{4}-[0-9]{0,4})+");
    public Congrats alertDialoge = new Congrats();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

//        new CountDownTimer(3000, 1000) {
//
//            public void onTick(long millisUntilFinished) {
//
//                alertDialoge.showDialog(AvailableJobActivity.this, "JimmieJob");
//            }
//
//            public void onFinish() {
//                alertDialoge.dialog.dismiss();
//            }
//        }.start();


        preferenceHelper.putIsLoadPriviousJob(false);
        initToolBar();
        // tvActionBarTitle.setText(getString(R.string.available_job));
        initRequire();
        checkLocationPermission();
        startService(new Intent(this, RealTimeService.class));
//        final Handler handler=new Handler();
//
//        final Runnable updateTask=new Runnable() {
//            @Override
//            public void run() {
//                avilableJobService(false);
//                handler.postDelayed(this,6000);
//            }
//        };
//        handler.postDelayed(updateTask,6000);




    }


    public void initRequire() {
        try {
            IntentFilter intentFilter = new IntentFilter(Const.ProviderPushStatus.PROVIDER_STATUS);
            LocalBroadcastManager.getInstance(this).registerReceiver(providerReciver, intentFilter);
            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);
            View viewHeader = navigationView.getHeaderView(0);
            imgNavUser = (ImageView) viewHeader.findViewById(R.id.imgDrawableUser);
            imgNavUser.setOnClickListener(this);
            txtNavName = (TextView) viewHeader.findViewById(R.id.myFontOpenRegTextView);
            txtnoIteam = (TextView) findViewById(R.id.no_iteam_available_job);
            availableJobArrayList = new ArrayList<>();
            drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.avilable_job_recycler);
            recyclerView.setHasFixedSize(true);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);
            availableJobAdapter = new AvailableJobAdapter(this, availableJobArrayList);
            recyclerView.setAdapter(availableJobAdapter);
            btnDrawerToggle.setOnClickListener(this);
            tvActionBarTitle.setOnClickListener(this);
            recyclerView.addOnItemTouchListener(new RecyclerViewTouchListener(this, recyclerView, this));
            swipeavailablejob = (SwipeRefreshLayout) findViewById(R.id.swipeavailablejob);
            swipeavailablejob.setOnRefreshListener(this);
            swipeavailablejob.setColorSchemeColors(getResources().getColor(R.color.color_0),
                    getResources().getColor(R.color.color_1), getResources().getColor(R.color.color_2),
                    getResources().getColor(R.color.color_3), getResources().getColor(R.color.color_4));
            if (preferenceHelper.getProviderStatus()) {
                avilableJobService(true);


            } else {
                createApproveDialog();
            }
            startUpdateProviderLocation();
        }catch (Exception e){
            Toast.makeText(AvailableJobActivity.this, ""+e, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setDrawerData();
    }

    public void setDrawerData() {
        txtNavName.setText(preferenceHelper.getUserName());
        Glide.with(this)
                .load(preferenceHelper.getUserImagePath())
                .asBitmap()
                .placeholder(getResources().getDrawable(R.drawable.default_icon))
                .dontAnimate()
                .into(new BitmapImageViewTarget(imgNavUser) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        super.setResource(resource);
                        RoundedBitmapDrawable cirimage = RoundedBitmapDrawableFactory.create(getResources(), resource);
                        cirimage.setCircular(true);
                        imgNavUser.setImageDrawable(cirimage);
                    }
                });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(providerReciver);
        stopUpdateProviderLocation();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.drawer_profile_update) {
            // Handle the camera action
            Intent intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);

        } else if (id == R.id.drawer_logout) {
            createLogoutDialog();
        } else if (id == R.id.view_quote_activity) {
            Intent intent = new Intent(this, ViewQuoteActivity.class);
            startActivity(intent);
        } else if (id == R.id.my_job) {
            Intent intent = new Intent(this, MyJobActivity.class);
            startActivity(intent);
        }else if (id == R.id.help) {
            help();
        }else if(id == R.id.drawer_select_type_activity){
            Intent intent = new Intent(this, ProviderTypeActivity.class);
            intent.putExtra("is_from_main",true);
            startActivity(intent);
        }

        else if(id == R.id.buy_tokens){
            buyTokens();
        }

        else if (id == R.id.nav_share) {
            Intent intent = new Intent(this, ProfileActivity.class);
            intent.putExtra("share","share");
            startActivity(intent);
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void buyTokens(){
        final Dialog buyTokensDialog = new Dialog(AvailableJobActivity.this);
        buyTokensDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        buyTokensDialog.setContentView(R.layout.buy_tokens);
        buyTokensDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.MATCH_PARENT);
        Button btnSubmit = (Button) buyTokensDialog.findViewById(R.id.btn50);
        Button btnCancel = (Button) buyTokensDialog.findViewById(R.id.btn120);

        //txtAdminPrice.setText("Select the Amount of Tokens you want to buy");

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                exitDialog = new CustomDialogNoEdt(AvailableJobActivity.this, "Buy Tokens", "To Buy Tokens you are required to enter your credit card details and proceed",
                        "PROCEED", "NO", true) {
                    @Override
                    public void btnOkClickListner() {
                        dismiss();
                        openPaymentDialog(50);
                    }

                    @Override
                    public void btnCancelClickListner() {
                        dismiss();
                    }
                };

                exitDialog.show();



            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exitDialog = new CustomDialogNoEdt(AvailableJobActivity.this, "Buy Tokens", "To Buy Tokens you are required to enter your credit card details and proceed",
                        "PROCEED", "NO", true) {
                    @Override
                    public void btnOkClickListner() {
                        dismiss();
                        openPaymentDialog(120);
                    }

                    @Override
                    public void btnCancelClickListner() {
                        dismiss();
                    }
                };

                exitDialog.show();

            }
        });
        buyTokensDialog.show();
    }


    public void help(){
        try{
            LayoutInflater li = LayoutInflater.from(AvailableJobActivity.this);
            View promptsView = li.inflate(R.layout.dialog_help_provider, null);
            final EditText x = (EditText) promptsView.findViewById(R.id.editText) ;
            Button y = (Button) promptsView.findViewById(R.id.emailSupport);

            y.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Toast.makeText(AvailableJobActivity.this, "Send Email to Support", Toast.LENGTH_SHORT).show();
                    x.setText("");
                }

            });

            final android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(AvailableJobActivity.this);
            alertDialogBuilder.setView(promptsView);
            alertDialogBuilder.setPositiveButton("OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    });
            android.app.AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.setCancelable(false);
            alertDialog.show();
        }catch (Exception e) {
        }
    }
    private BroadcastReceiver providerReciver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String response = intent.getStringExtra(Const.ProviderPushStatus.PROVIDER_APPROVE_STATUS);

            switch (parseContent.getPushId(response)) {

                case Const.ProviderPushStatus.PROVIDER_APPOVE:
                    onPushProviderApprove();
                    break;

                case Const.ProviderPushStatus.PROVIDER_DECLINE:
                    onPushProviderDecline();
                    break;
                case Const.ProviderPushStatus.NEW_REQUEST:
                case Const.ProviderPushStatus.DELETED_PROVIDED_QUOTE:
                    avilableJobService(false);
                    break;
                default:
                    AppLog.Log("tag", "Default case");
                    break;
            }
        }
    };

    private void onPushProviderApprove() {
        preferenceHelper.putProvidestatus(true);
        stopUpdateProviderLocation();
        startUpdateProviderLocation();
        removeApproveDialog();
        avilableJobService(true);

    }

    private void onPushProviderDecline() {
        preferenceHelper.putProvidestatus(false);
        createApproveDialog();
        stopUpdateProviderLocation();
    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.btnDrawerToggle || view.getId() == R.id.tvActionBarTitle) {
            drawer.openDrawer(GravityCompat.START);
        }else if(view.getId() == R.id.imgDrawableUser){
            startActivity(new Intent(this,ProfileActivity.class));
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            }
        }
    }

    public void createApproveDialog() {
        if (approveDialog != null && approveDialog.isShowing()) {
            return;
        }

        approveDialog = new CustomDialogNoEdt(this, getString(R.string.dialog_approve_title), getString(R.string.dialog_approve_message),
                getString(R.string.dialog_approve_btn), getString(R.string.dialog_logout_ok), true) {
            @Override
            public void btnOkClickListner() {
                dismiss();
                finish();
            }

            @Override
            public void btnCancelClickListner() {
                logoutService();
            }
        };

        approveDialog.show();


    }

    public void removeApproveDialog() {

        if (approveDialog != null && approveDialog.isShowing()) {
            approveDialog.dismiss();
            approveDialog = null;
        }

    }

    public void avilableJobService(boolean isLoading) {

        try{
        HashMap<String, String> map = new HashMap<>();
        map.put(Const.Param.URL, Const.ServiceType.GET_AVAILABLE_JOB);
        map.put(Const.Param.USER_ID, preferenceHelper.getUSERID());
        map.put(Const.Param.TOKEN, preferenceHelper.getToken());
        map.put(Const.Param.TIME_ZONE, preferenceHelper.getDeviceTimeZone());

        new HttpRequester(this, map, Const.ServiceCode.GET_AVAILABLE_JOB, Const.HttpMethod.POST, this);
        if (isLoading) {
            Andyutils.showCustomProgressDialog(this, false);
        }
        if(counter<availableJobArrayList.size()) {
            counter = availableJobArrayList.size();
            //New Job
            Toast.makeText(AvailableJobActivity.this, "JimmieJobs Has an available Job for You", Toast.LENGTH_SHORT).show();
            NotificationManager notif=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
            Notification notify=new Notification.Builder
                    (getApplicationContext()).setContentTitle("JimmieJobs").setContentText("JimmieJobs Has a Job for You").
                    setContentTitle("JimmieJobs Provider").setSmallIcon(R.drawable.app_icon).build();
            notify.defaults = Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE;

            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
            r.play();

            notify.flags |= Notification.FLAG_AUTO_CANCEL;
            notif.notify(0, notify);
        }
    }catch (Exception e) {
        }
        }


    /*On CLick Recycler view */
    @Override
    public void onClickRecyclerListIteam(View view, int position) {
        Intent intent = new Intent(this, MyJobActivity.class);
        intent.putExtra("position",position);
        startActivity(intent);
//        Intent intent = new Intent(this, ActiveJobInfoActivity.class);
//        AvailableJob availableJobParsceable = availableJobArrayList.get(position);
//        intent.putExtra("availablejob", availableJobParsceable);
//        intent.putExtra("position", position);
//        startActivityForResult(intent, Const.ACTIVITY_AVAILABLE_JOB);
    }


    public void updateLocationService() {
    try {


        HashMap<String, String> map = new HashMap<>();
        map.put(Const.Param.URL, Const.ServiceType.UPDATE_LOCATION);
        map.put(Const.Param.USER_ID, preferenceHelper.getUSERID());
        map.put(Const.Param.TOKEN, preferenceHelper.getToken());
        map.put(Const.Param.LATITUDE, String.valueOf(preferenceHelper.getLatitude()));
        map.put(Const.Param.LONGITUDE, String.valueOf(preferenceHelper.getLongitude()));

        if (Andyutils.isNetworkAvailable(this)) {
            new HttpRequester(this, map, Const.ServiceCode.UPDATE_LOCATION, Const.HttpMethod.POST, this);
        }
    }catch (Exception e){

    }


    }

    @Override
    public void onTaskCompleted(String response, int serviceCode) {
        try {
            switch (serviceCode) {

                case Const.ServiceCode.UPDATE_LOCATION:
                    if (parseContent.isstatusSucess(response)) {
                        AppLog.Log("tag", "callled on task update location");
                    }
                    break;
                case Const.ServiceCode.GET_AVAILABLE_JOB:
                    onTaskGetAvailableJob(response);
                    break;
                case Const.ServiceCode.LOGOUT:
                    if (parseContent.issucess(response)) {
                        onTaskLogout();
                    }
                    break;

                default:
                    AppLog.Log(Const.TAG, "Default");
                    break;

            }
        }catch (Exception e){

        }
    }

    private void onTaskGetAvailableJob(String response) {
        try {
            Andyutils.removeCustomProgressDialog();
            swipeavailablejob.setRefreshing(false);
            if (parseContent.issucess(response)) {
                availableJobArrayList.clear();
                availableJobArrayList = parseContent.parseAvailableJob(response, availableJobArrayList);
                if (availableJobArrayList.isEmpty()) {
                    txtnoIteam.setVisibility(View.VISIBLE);
                } else {
                    txtnoIteam.setVisibility(View.GONE);
                }
                availableJobAdapter.notifyDataSetChanged();
            }
        }catch (Exception e){

        }
    }

    private void onTaskLogout() {
        if (approveDialog != null && approveDialog.isShowing()) {
            approveDialog.dismiss();
            approveDialog = null;
        } else if (logoutDialog != null && logoutDialog.isShowing()) {
            logoutDialog.dismiss();
        }
        doLogout();
    }



    @Override
    public void onRefresh() {
        avilableJobService(false);
    }


    public class TrackTimerLocation extends TimerTask {
        @Override
        public void run() {
            if (!TextUtils.isEmpty(preferenceHelper.getLatitude()) && !TextUtils.isEmpty(preferenceHelper.getLongitude())) {
                updateLocationService();
            }

        }
    }

    private void startUpdateProviderLocation() {
        updateLocationTimer = new Timer();
        updateLocationTimer.scheduleAtFixedRate(new TrackTimerLocation(), 0, Const.TIMER_SCHEDULE);
    }

    private void stopUpdateProviderLocation() {
        if (updateLocationTimer != null) {
            updateLocationTimer.cancel();
            updateLocationTimer = null;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);
            if (resultCode == Const.ACTIVITY_QUOTE_PROVIDED) {
                int getPosition = data.getIntExtra("position", 0);
                availableJobArrayList.remove(getPosition);
                if (availableJobArrayList.isEmpty()) {
                    txtnoIteam.setVisibility(View.VISIBLE);
                } else {
                    txtnoIteam.setVisibility(View.GONE);
                }
                availableJobAdapter.notifyDataSetChanged();
            }
        }catch (Exception e){

        }
    }

    public void createLogoutDialog() {

        logoutDialog = new CustomDialogNoEdt(this, getString(R.string.dialog_logout), getString(R.string.dialog_logout_message), getString(R.string.dialog_logout_ok),
                getString(R.string.dialog_logout_cancel), true) {
            @Override
            public void btnOkClickListner() {
                logoutService();
            }

            @Override
            public void btnCancelClickListner() {
                dismiss();
            }
        };
        logoutDialog.show();

    }

    public void logoutService() {
        try{
        HashMap<String, String> map = new HashMap<>();
        map.put(Const.Param.URL, Const.ServiceType.LOGOUT);
        map.put(Const.Param.USER_ID, preferenceHelper.getUSERID());
        map.put(Const.Param.TOKEN, preferenceHelper.getToken());

        new HttpRequester(this, map, Const.ServiceCode.LOGOUT, Const.HttpMethod.POST, this);
        }catch (Exception e) {
        }
        }

    public void doLogout() {
        stopUpdateProviderLocation();
        preferenceHelper.logout();
        Intent intent = new Intent(getApplicationContext(), SigninActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);

    }


    public void openExitDialog() {


        if(exitDialog != null && exitDialog.isShowing()){
            return;
        }

        exitDialog = new CustomDialogNoEdt(this, getString(R.string.exit_dialog_title), getString(R.string.exit_dialog_message),
                getString(R.string.OK), getString(R.string.CANCEL), true) {
            @Override
            public void btnOkClickListner() {
                dismiss();
                finish();
            }

            @Override
            public void btnCancelClickListner() {
                dismiss();
            }
        };

        exitDialog.show();

    }

    private void openPaymentDialog(final int amnt){
        if(dialogAddCard != null && dialogAddCard.isShowing()){
            return;
        }


        dialogAddCard = new Dialog(this);
        dialogAddCard.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogAddCard.setContentView(R.layout.dialog_add_card);
        WindowManager.LayoutParams params = dialogAddCard.getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        dialogAddCard.getWindow().setAttributes(params);
        dialogAddCard.setCancelable(false);

        edtCreditCardNo = (EditText) dialogAddCard.findViewById(R.id.card_no);
        edtCardMonth = (EditText) dialogAddCard.findViewById(R.id.exp_m);
        edtCardYear = (EditText) dialogAddCard.findViewById(R.id.exp_yr);
        edtCardCvv = (EditText) dialogAddCard.findViewById(R.id.cvv);
        btnAddCard = (Button) dialogAddCard.findViewById(R.id.btnAddCard);
        Button btnCancel = (Button) dialogAddCard.findViewById(R.id.btnCancel);
        setTextWatcher();

        btnAddCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                String x = year+"".replace("20","");
                int y = Integer.parseInt(x);
                if(cardType.equals("Unknown")){
                    Toast.makeText(AvailableJobActivity.this, "Invalid Card", Toast.LENGTH_SHORT).show();
                }
                if(Integer.parseInt(edtCardMonth.getText().toString())>12){
                    Toast.makeText(AvailableJobActivity.this, "Invalid Expiry Month", Toast.LENGTH_SHORT).show();
                }
                if(Integer.parseInt(edtCardYear.getText().toString())+2000<y){

                    Toast.makeText(AvailableJobActivity.this, "Invalid Expiry Year", Toast.LENGTH_SHORT).show();
                }
                else {
                    if(exitDialog != null && exitDialog.isShowing()){
                        return;
                    }

                    exitDialog = new CustomDialogNoEdt(AvailableJobActivity.this, "JimmieJobs", "Are you sure you want to Buy Tokens?",
                            "YES", "NO", true) {
                        @Override
                        public void btnOkClickListner() {
                            dismiss();
                            dialogAddCard.dismiss();
                            Intent intentShare = new Intent(AvailableJobActivity.this, ProfileActivity.class);
                            intentShare.putExtra("share", "buy_tokens");
                            intentShare.putExtra("c_no", edtCreditCardNo.getText().toString().replace("-", ""));
                            intentShare.putExtra("cvv", edtCardCvv.getText().toString());
                            intentShare.putExtra("exp_yr", edtCardYear.getText().toString());
                            intentShare.putExtra("exp_m", edtCardMonth.getText().toString());
                            intentShare.putExtra("amnt", amnt);
                            intentShare.putExtra("c_typ", cardType);
                            startActivity(intentShare);
                        }

                        @Override
                        public void btnCancelClickListner() {
                            dismiss();
                        }
                    };

                    exitDialog.show();


                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogAddCard.dismiss();
            }
        });
        dialogAddCard.show();
    }

    private void setTextWatcher(){
        edtCreditCardNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //Do with text..
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(TextUtils.isEmpty(s.toString())){
                    edtCreditCardNo.setCompoundDrawablesWithIntrinsicBounds(null , null , null , null);
                }

                cardType = getCardType(s.toString());
                setCardTypeDrawable(cardType);

                if(edtCreditCardNo.getText().toString().length() == 19){
                    edtCardMonth.requestFocus();
                }

            }


            @Override
            public void afterTextChanged(Editable s) {

                if(s.length() > 0 && !CODE_PATTERN.matcher(s).matches()){
                    String input = s.toString();
                    String numberOnly = keepNumberOnly(input);
                    String code = formatCardNo(numberOnly);
                    edtCreditCardNo.removeTextChangedListener(this);
                    edtCreditCardNo.setText(code);
                    edtCreditCardNo.setSelection(code.length());
                    edtCreditCardNo.addTextChangedListener(this);
                }

            }


            private String keepNumberOnly(CharSequence s){
                return s.toString().replaceAll("[^0-9]", "");
            }

            private String formatCardNo(CharSequence s){
                int groupDigits = 0;
                String cardNo = "";
                int sSize = s.length();
                for (int i = 0; i < sSize; ++i) {
                    cardNo += s.charAt(i);
                    ++groupDigits;
                    if (groupDigits == 4) {
                        cardNo += "-";
                        groupDigits = 0;
                    }
                }
                return cardNo;
            }
        });


        edtCardYear.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //Do with text
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(edtCardYear.getText().toString().length() == 2){
                    edtCardCvv.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                //Do with text
            }
        });


        edtCardMonth.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //Do with text..
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(edtCardMonth.getText().toString().length() == 2){
                    edtCardYear.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                //Do with text..
            }
        });
    }

    private String getCardType(String prifix){
        if(TextUtils.isEmpty(prifix)){
            return UNKNOWN;
        }
        else {
            if (Andyutils.hasAnyPrifix(prifix, Card.PREFIXES_VISA)) {
                return VISA;
            } else if (Andyutils.hasAnyPrifix(prifix, Card.PREFIXES_AMERICAN_EXPRESS)) {
                return AMERICAN_EXPRESS;
            } else if (Andyutils.hasAnyPrifix(prifix, Card.PREFIXES_DISCOVER)) {
                return DISCOVER;
            } else if (Andyutils.hasAnyPrifix(prifix, Card.PREFIXES_DINERS_CLUB)) {
                return DINERS_CLUB;
            } else if (Andyutils.hasAnyPrifix(prifix, Card.PREFIXES_MASTERCARD)) {
                return MASTERCARD;
            } else if (Andyutils.hasAnyPrifix(prifix, Card.PREFIXES_JCB)){
                return JCB;
            } else {
                return UNKNOWN;
            }
        }
    }


    private void setCardTypeDrawable(String cradTypes){

        if(cradTypes.equals(VISA)){
            edtCreditCardNo.setCompoundDrawablesWithIntrinsicBounds(
                    ResourcesCompat.getDrawable(getResources(), R.drawable
                            .creditcard_visa, null)
                    , null,
                    null, null);
        }
        else if(cradTypes.equals(MASTERCARD)){
            edtCreditCardNo.setCompoundDrawablesWithIntrinsicBounds(
                    ResourcesCompat.getDrawable(getResources(), R.drawable
                            .creditcard_mastercard, null)
                    , null,
                    null, null);
        }
        else if(cradTypes.equals(DISCOVER)){
            edtCreditCardNo.setCompoundDrawablesWithIntrinsicBounds(
                    ResourcesCompat.getDrawable(getResources(), R.drawable
                            .creditcard_discover, null)
                    , null,
                    null, null);
        }
        else if (cradTypes.equals(AMERICAN_EXPRESS)){
            edtCreditCardNo.setCompoundDrawablesWithIntrinsicBounds(
                    ResourcesCompat.getDrawable(getResources(), R.drawable
                            .creditcard_amex, null)
                    , null,
                    null, null);
        }
        else {
            edtCreditCardNo.setCompoundDrawablesWithIntrinsicBounds(
                    null, null, null, null);
        }
    }

    @Override
    public void onBackPressed() {

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            openExitDialog();
        }
    }


}

