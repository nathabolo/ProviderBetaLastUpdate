package com.tradesman.provider;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.tradesman.provider.utils.AppLog;
import com.jimmiejobscreative.tradesman.provider.R;;
import com.tradesman.provider.adapter.ViewQuoteAdapter;
import com.tradesman.provider.model.ViewQuote;
import com.tradesman.provider.parse.AsyncTaskCompleteListener;
import com.tradesman.provider.parse.HttpRequester;
import com.tradesman.provider.utils.Andyutils;
import com.tradesman.provider.utils.Const;
import com.tradesman.provider.utils.RecyclerViewTouchListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ViewQuoteActivity extends ActionBarBaseActivity implements AsyncTaskCompleteListener,
        RecyclerViewTouchListener.ClickListener, SwipeRefreshLayout.OnRefreshListener {

    private List<ViewQuote> viewQuoteArrayList;
    private ViewQuoteAdapter viewQuoteAdapter;
    private TextView noDataViewQuote;
    private SwipeRefreshLayout swipViewQuote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_quote_activity);
        initToolBar();
        //tvActionBarTitle.setText(getString(R.string.view_quote));
        btnDrawerToggle.setVisibility(View.INVISIBLE);
        AppLog.Log(Const.TAG,"OnCreate call");
        initRequire();

        AppLog.Log("push","Latitute longitutde - "+preferenceHelper.getLatitude()+"///"+preferenceHelper.getLongitude());

        final Handler handler=new Handler();

        final Runnable updateTask=new Runnable() {
            @Override
            public void run() {
                viewQuoteService(false);
                handler.postDelayed(this,6000);
            }
        };
        handler.postDelayed(updateTask,6000);
    }

    public void initRequire(){

        IntentFilter intentFilter = new IntentFilter(Const.ProviderPushStatus.PROVIDER_STATUS);
        LocalBroadcastManager.getInstance(this).registerReceiver(providerReciver,intentFilter);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.view_quote_recycler);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        viewQuoteArrayList = new ArrayList<>();
        viewQuoteAdapter = new ViewQuoteAdapter(this,viewQuoteArrayList);
        recyclerView.setAdapter(viewQuoteAdapter);
        recyclerView.addOnItemTouchListener(new RecyclerViewTouchListener(this, recyclerView, this));
        noDataViewQuote = (TextView) findViewById(R.id.no_iteam_view_quote);
        swipViewQuote = (SwipeRefreshLayout) findViewById(R.id.swipviewQuote);
        swipViewQuote.setOnRefreshListener(this);
        swipViewQuote.setColorSchemeColors(getResources().getColor(R.color.color_0),
                getResources().getColor(R.color.color_1),getResources().getColor(R.color.color_2),
                getResources().getColor(R.color.color_3),getResources().getColor(R.color.color_4));

        viewQuoteService(true);
    }

    private BroadcastReceiver providerReciver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String response = intent.getStringExtra(Const.ProviderPushStatus.PROVIDER_APPROVE_STATUS);

            switch (parseContent.getPushId(response)){
                case Const.ProviderPushStatus.ACCEPT_COST_BY_USER:
                case Const.ProviderPushStatus.DELETED_PROVIDED_QUOTE:
                    viewQuoteService(false);
                    break;
                default:
                    AppLog.Log(Const.TAG,"Default case");
                    break;
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(providerReciver);
    }

    public  void viewQuoteService(boolean isshowProgress){

        HashMap<String,String> map = new HashMap<>();
        map.put(Const.Param.URL,Const.ServiceType.VIEW_QUOTE);
        map.put(Const.Param.USER_ID,preferenceHelper.getUSERID());
        map.put(Const.Param.TOKEN,preferenceHelper.getToken());
        map.put(Const.Param.TIME_ZONE,preferenceHelper.getDeviceTimeZone());
        new HttpRequester(this,map,Const.ServiceCode.VIEW_QUOTE,Const.HttpMethod.POST,this);
        if(isshowProgress){
            Andyutils.showCustomProgressDialog(this,false);
        }

    }



    @Override
    public void onTaskCompleted(String response, int serviceCode) {

        if(serviceCode == Const.ServiceCode.VIEW_QUOTE){
            Andyutils.removeCustomProgressDialog();
            swipViewQuote.setRefreshing(false);
            onTaskViewQuote(response);
        }
    }

    private void onTaskViewQuote(String response){
        if(parseContent.issucess(response)) {
            viewQuoteArrayList.clear();
            viewQuoteArrayList = parseContent.parseViewQuote(response,viewQuoteArrayList);
            viewQuoteAdapter.notifyDataSetChanged();
            if(viewQuoteArrayList.isEmpty()){
                noDataViewQuote.setVisibility(View.VISIBLE);
            }else {
                noDataViewQuote.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onClickRecyclerListIteam(View view, int position) {

        Intent intent = new Intent(this,ViewQuoteInfoActivity.class);
        ViewQuote viewQuote = viewQuoteArrayList.get(position);
        intent.putExtra("viewQuote",viewQuote);
        intent.putExtra("position",position);
        startActivityForResult(intent,Const.VIEW_QUOTE_CHNAGE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == Const.VIEW_QUOTE_CHNAGE){
            viewQuoteService(true);
        }
    }

    @Override
    public void onRefresh() {
        viewQuoteService(false);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        AppLog.Log("tag","On back Called View Quote");
    }
}
