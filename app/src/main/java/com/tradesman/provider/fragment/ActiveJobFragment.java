package com.tradesman.provider.fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tradesman.provider.ActiveJobInfoActivity;
import com.tradesman.provider.adapter.ActiveJobAdapter;
import com.tradesman.provider.parse.ParseContent;
import com.tradesman.provider.utils.AppLog;
import com.tradesman.provider.utils.Const;
import com.tradesman.provider.utils.RecyclerViewTouchListener;
import com.jimmiejobscreative.tradesman.provider.R;;
import com.tradesman.provider.model.ActiveJob;
import com.tradesman.provider.parse.AsyncTaskCompleteListener;
import com.tradesman.provider.parse.HttpRequester;
import com.tradesman.provider.utils.Andyutils;
import com.tradesman.provider.utils.PreferenceHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Elluminati Mohit on 1/26/2017.
 */

public class ActiveJobFragment extends Fragment implements AsyncTaskCompleteListener, RecyclerViewTouchListener.ClickListener, SwipeRefreshLayout.OnRefreshListener {

    private PreferenceHelper preferenceHelper;
    private ParseContent parseContent;
    private List<ActiveJob> activeJobArrayList;
    private ActiveJobAdapter activeJobAdapter;
    private TextView txtEmpty;
    private SwipeRefreshLayout swipActivejob;


    public static ActiveJobFragment newInstance() {

        return new ActiveJobFragment();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activejobfragmnet, container, false);
        initReuire();
        txtEmpty = (TextView) view.findViewById(R.id.no_iteam_active_job);
        swipActivejob = (SwipeRefreshLayout) view.findViewById(R.id.swipeactivejob);
        swipActivejob.setOnRefreshListener(this);
        RecyclerView  recyclerView = (RecyclerView) view.findViewById(R.id.recycler_active_job);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        activeJobArrayList = new ArrayList<>();
        activeJobAdapter = new ActiveJobAdapter(getActivity(), activeJobArrayList);
        recyclerView.setAdapter(activeJobAdapter);
        recyclerView.addOnItemTouchListener(new RecyclerViewTouchListener(getActivity(), recyclerView, this));
        activeJobService(true);
        return view;
    }


    public void initReuire() {
        preferenceHelper = PreferenceHelper.getInstance(getActivity());
        parseContent = new ParseContent(getActivity());

        IntentFilter intentFilter = new IntentFilter(Const.ProviderPushStatus.PROVIDER_STATUS);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(providerReciver, intentFilter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(providerReciver);
    }

    public void activeJobService(boolean isProgessrShow) {

        HashMap<String, String> map = new HashMap<>();
        map.put(Const.Param.URL, Const.ServiceType.ACTIVE_JOB);
        map.put(Const.Param.USER_ID, preferenceHelper.getUSERID());
        map.put(Const.Param.TOKEN, preferenceHelper.getToken());
        map.put(Const.Param.TIME_ZONE, preferenceHelper.getDeviceTimeZone());
        new HttpRequester(getActivity(), map, Const.ServiceCode.ACTIVE_JOB, Const.HttpMethod.POST, this);

        if(isProgessrShow){
            Andyutils.showCustomProgressDialog(getActivity(),false);
        }


    }

    @Override
    public void onTaskCompleted(String response, int serviceCode) {

        if(serviceCode == Const.ServiceCode.ACTIVE_JOB){
            Andyutils.removeCustomProgressDialog();
            swipActivejob.setRefreshing(false);
            if (parseContent.issucess(response)) {
                activeJobArrayList.clear();
                activeJobArrayList = parseContent.parseActiveJob(response, activeJobArrayList);
                activeJobAdapter.notifyDataSetChanged();

                Andyutils.showEmaptyData(activeJobArrayList.size(),txtEmpty);
            }
        }
    }


    @Override
    public void onClickRecyclerListIteam(View view, int position) {
        ActiveJob activeJob = activeJobArrayList.get(position);
        Intent intent = new Intent(getActivity(), ActiveJobInfoActivity.class);
        intent.putExtra(Const.Param.POSITION, position);
        intent.putExtra("activejob", activeJob);

        startActivityForResult(intent, Const.PROVIDER_STATE_CHANGE);
    }

    private BroadcastReceiver providerReciver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String response = intent.getStringExtra(Const.ProviderPushStatus.PROVIDER_APPROVE_STATUS);
            int pushId = parseContent.getPushId(response);
            if(pushId == Const.ProviderPushStatus.ACCEPT_COST_BY_USER || pushId == Const.ProviderPushStatus.USER_CANCELED_JOB ){
                activeJobService(false);
            }
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (resultCode) {

            case Const.PROVIDER_STATE_CHANGE:
                activeJobArrayList.get(data.getIntExtra(Const.Param.POSITION, 0)).setRequestStatus(String.valueOf(data.getIntExtra("request_status", 0)));
                break;
            case Const.CHANGE_IN_DATA:
               onActResultChangeInData(data);
                break;
            case Const.REFRESH_LIST:
                activeJobService(true);
                break;
            default:
                AppLog.Log(Const.TAG,"Default call on Result set ");
                break;
        }
    }
    public void onActResultChangeInData(Intent data){
        activeJobArrayList.remove(data.getIntExtra(Const.Param.POSITION,-1));
        activeJobAdapter.notifyDataSetChanged();
        Andyutils.showEmaptyData(activeJobArrayList.size(),txtEmpty);
        preferenceHelper.putIsLoadPriviousJob(false);
    }


    @Override
    public void onRefresh() {
        activeJobService(false);
    }
}
