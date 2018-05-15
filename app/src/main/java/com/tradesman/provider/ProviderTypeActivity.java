package com.tradesman.provider;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.tradesman.provider.adapter.ProviderTypeAdapter;
import com.tradesman.provider.dialog.CustomDialogNoEdt;
import com.tradesman.provider.model.ProviderType;
import com.tradesman.provider.parse.HttpRequester;
import com.tradesman.provider.utils.Andyutils;
import com.tradesman.provider.utils.AppLog;
import com.tradesman.provider.utils.Const;
import com.tradesman.provider.utils.RecyclerViewTouchListener;
import com.jimmiejobscreative.tradesman.provider.R;;
import com.tradesman.provider.parse.AsyncTaskCompleteListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ProviderTypeActivity extends ActionBarBaseActivity implements AsyncTaskCompleteListener,
        View.OnClickListener, RecyclerViewTouchListener.ClickListener {

    private List<ProviderType> providerTypeArrayList;

    private List<String> selectedType;
    CustomDialogNoEdt exitDialog ;

    private ProviderTypeAdapter providerTypeAdapter;
    private String getSelectedTypeId;
    private String[] splitSelctedtypedId;
    private boolean isFromMain;
    private Button btnSubmit;
    private Button btnUpdate;
    private RecyclerView recyclerView;
    private boolean isAllowClick = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.provider_type_activity);
        Intent intent = getIntent();
        isFromMain = intent.getBooleanExtra("is_from_main",false);
        initRequire();
    }

    public void initRequire() {
        initToolBar();
        btnDrawerToggle.setVisibility(View.INVISIBLE);
        setToolBarTitle(getString(R.string.provider_type));
        selectedType = new ArrayList<>();
        if(TextUtils.isEmpty(preferenceHelper.getSelectedType())){
            preferenceHelper.putisProviderTypeSelected(false);
        }
        btnSubmit = (Button) findViewById(R.id.btn_submit_provider_type);
        btnSubmit.setOnClickListener(this);
        btnUpdate = (Button) findViewById(R.id.btn_update_provider_type);
        btnUpdate.setOnClickListener(this);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager gridlayout = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(gridlayout);
        providerTypeArrayList = new ArrayList<>();
        providerTypeAdapter = new ProviderTypeAdapter(providerTypeArrayList, this);
        recyclerView.setAdapter(providerTypeAdapter);
        recyclerView.addOnItemTouchListener(new RecyclerViewTouchListener(this, recyclerView, this));
        if(isFromMain){
            btnSubmit.setVisibility(View.GONE);
            btnUpdate.setVisibility(View.VISIBLE);
            isAllowClick = false;

        }
        getSelectedTypeId();
        providerTypeService();

    }

    private void getSelectedTypeId(){
        if(!TextUtils.isEmpty(preferenceHelper.getSelectedType())){
            getSelectedTypeId= preferenceHelper.getSelectedTypeId();
            splitSelctedtypedId = getSelectedTypeId.split(",");
        }
    }

    public void providerTypeService() {

        HashMap<String, String> map = new HashMap<>();
        map.put(Const.Param.URL, Const.ServiceType.GET_PROVIDER_TYPE);
        new HttpRequester(this, map, Const.ServiceCode.GET_PROVIDER_TYPE, Const.HttpMethod.POST, this);
        Andyutils.showCustomProgressDialog(this,false);
    }

    @Override
    public void onTaskCompleted(String response, int serviceCode) {

        switch (serviceCode) {

            case Const.ServiceCode.GET_PROVIDER_TYPE:
                AppLog.Log("type","Service - "+response);
                Andyutils.removeCustomProgressDialog();
                onTaskGetProviderType(response);
                break;

            case Const.ServiceCode.SELECTED_TYPE:
                AppLog.Log("tag","Provider type Resonse"+response);
                Andyutils.removeCustomProgressDialog();
                goToAvailableActivity(response);
                break;
            default:
                AppLog.Log("tag", "Default case");
                break;
        }
    }

    private void onTaskGetProviderType(String response){
        try{
        if (parseContent.issucess(response)) {
            providerTypeArrayList = parseContent.parseTypesOfProviders(response, providerTypeArrayList);
            if(!TextUtils.isEmpty(preferenceHelper.getSelectedType())){
                for (int i =0;i<splitSelctedtypedId.length; i++){
                    //providerTypeArrayList.get(Integer.parseInt(splitSelctedtypedId[i])).setSelected(true);
                    int splitid = Integer.parseInt(splitSelctedtypedId[i]);
                    AppLog.Log("tag","Selected type "+i +"== "+splitid);
                    providerTypeArrayList.get(splitid-1).setSelected(true);
                }
            }

            providerTypeAdapter.notifyDataSetChanged();
        }
    }catch (Exception e) {
        }
        }
    private void goToAvailableActivity(String response){
        if (parseContent.isSucessWithProviderType(response)) {
            preferenceHelper.putisProviderTypeSelected(true);
            Intent intent = new Intent(this, AvailableJobActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }


    @Override
    public void onClick(View view) {

        if(view.getId() == R.id.btn_submit_provider_type){
            Andyutils.removeCustomProgressDialog();
            submitSelctedtype();
        }else if(view.getId() == R.id.btn_update_provider_type){
            btnUpdate.setVisibility(View.GONE);
            btnSubmit.setVisibility(View.VISIBLE);
            isAllowClick = true;
        }
    }

    private void submitSelctedtype(){
        try {
            selectedType.clear();
            for (int i = 0; i < providerTypeArrayList.size(); i++) {
                if (providerTypeArrayList.get(i).getSelected()) {
                    selectedType.add(providerTypeArrayList.get(i).getId());
                }
            }
            if (selectedType.isEmpty()) {
                Andyutils.showToast(this, getString(R.string.please_selece_provioder_type));
            } else {

                exitDialog = new CustomDialogNoEdt(ProviderTypeActivity.this, "Update Profile", "Are you sure you want Update your Category List?",
                        "YES", "NO", true) {
                    @Override
                    public void btnOkClickListner() {
                        dismiss();
                        String data = parseContent.getSelectedTypeJson(selectedType);
                        if (!TextUtils.isEmpty(data)) {
                            sendSelectedJob(data);
                            Andyutils.showCustomProgressDialog(ProviderTypeActivity.this, false);
                        }
                    }

                    @Override
                    public void btnCancelClickListner() {
                        dismiss();
                    }
                };

                exitDialog.show();
            }
        }catch (Exception e){

        }
    }

    public void sendSelectedJob(String data) {
        try {
            HashMap<String, String> map = new HashMap<>();

            map.put(Const.Param.URL, Const.ServiceType.SELECTED_TYPE);
            map.put(Const.Param.USER_ID, preferenceHelper.getUSERID());
            map.put(Const.Param.TOKEN, preferenceHelper.getToken());
            map.put(Const.Param.TYPES, data);

            new HttpRequester(this, map, Const.ServiceCode.SELECTED_TYPE, Const.HttpMethod.POST, this);

            Andyutils.showCustomProgressDialog(this, false);
        }catch (Exception e){
            Toast.makeText(ProviderTypeActivity.this, "Network Error!!", Toast.LENGTH_SHORT).show();
        }
    }

    public void getSelectedType(String response){
        String getType = preferenceHelper.getSelectedTypeId();
        String[] splitId = getType.split(",");

        for(int i =0 ; i<splitId.length;i++ ){
            if(TextUtils.equals(splitId[i],providerTypeArrayList.get(i).getId())){


            }
        }

    }
    @Override
    public void onClickRecyclerListIteam(View view, int position) {

        if (isAllowClick) {
            LinearLayout relativeLayout = (LinearLayout) view.findViewById(R.id.layoutRelativeType);
            ImageView imageView = (ImageView) view.findViewById(R.id.imgCorrectPType);
            ColorDrawable layoutBackground = (ColorDrawable) relativeLayout.getBackground();

            if (layoutBackground.getColor() == getResources().getColor(R.color.color_selected)) {
                relativeLayout.setBackgroundColor(Color.TRANSPARENT);
                imageView.setVisibility(View.GONE);
                providerTypeArrayList.get(position).setSelected(false);
            } else {
                relativeLayout.setBackgroundColor(getResources().getColor(R.color.color_selected));
                imageView.setVisibility(View.VISIBLE);
                providerTypeArrayList.get(position).setSelected(true);
            }

            selectedType.add(providerTypeArrayList.get(position).getId());
        }
    }
}
