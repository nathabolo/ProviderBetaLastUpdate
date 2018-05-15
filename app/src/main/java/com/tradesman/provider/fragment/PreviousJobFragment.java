package com.tradesman.provider.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tradesman.provider.adapter.PreviousJobAdapter;
import com.tradesman.provider.parse.ParseContent;
import com.tradesman.provider.utils.AppLog;
import com.tradesman.provider.PreviosJobinfoActivity;
import com.jimmiejobscreative.tradesman.provider.R;;
import com.tradesman.provider.model.PreviousJob;
import com.tradesman.provider.parse.AsyncTaskCompleteListener;
import com.tradesman.provider.parse.HttpRequester;
import com.tradesman.provider.utils.Andyutils;
import com.tradesman.provider.utils.Const;
import com.tradesman.provider.utils.PreferenceHelper;
import com.tradesman.provider.utils.RecyclerViewTouchListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Elluminati Mohit on 1/26/2017.
 */

public class PreviousJobFragment   extends Fragment implements AsyncTaskCompleteListener, RecyclerViewTouchListener.ClickListener {


    private PreferenceHelper preferenceHelper;
    private ParseContent parseContent;
    private PreviousJobAdapter adapter;
    private List<PreviousJob> previousJobArrayList;
    private static int pageNo;
    private boolean isclearList = false;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    boolean isFirstLoad = true;
    int totalPage;
    TextView txtEmpty;

    public static PreviousJobFragment newInstanse(){

            return new PreviousJobFragment();
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.previousjobfragmnet, container, false);
        txtEmpty = (TextView) view.findViewById(R.id.no_iteam_previos_job);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_previous_job);
        final RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        previousJobArrayList = new ArrayList<>();
        adapter = new PreviousJobAdapter(previousJobArrayList,getActivity());
        recyclerView.setAdapter(adapter);
        recyclerView.addOnItemTouchListener(new RecyclerViewTouchListener(getActivity(),recyclerView,this));


        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int scrollState) {
                super.onScrollStateChanged(recyclerView, scrollState);

                visibleItemCount = layoutManager.getChildCount();
                totalItemCount = layoutManager.getItemCount();
                pastVisiblesItems =  ((LinearLayoutManager)recyclerView.getLayoutManager()).findFirstVisibleItemPosition();

            if(scrollState == RecyclerView.SCROLL_STATE_IDLE && (visibleItemCount + totalItemCount)>adapter.getItemCount()){
                if(totalItemCount != 0 && visibleItemCount + pastVisiblesItems >=totalItemCount ){
                    if(totalPage >= pageNo ){

                    previousJobService(pageNo,false,false);
                        pageNo +=1;
                    }
            }
            }

            }
        });


        initRequire();
        return view;
    }


    public void initRequire(){
        preferenceHelper = PreferenceHelper.getInstance(getActivity());
        parseContent  = new ParseContent(getActivity());
        preferenceHelper.putIsLoadPriviousJob(false);
        pageNo = 1;
    }


    public void previousJobService(int pageNumber, boolean isclearList, boolean isShowLoading){

        this.isclearList = isclearList;
        HashMap<String ,String> map = new HashMap<>();
        map.put(Const.Param.URL,Const.ServiceType.PREVIOUS_JOB);
        map.put(Const.Param.USER_ID,preferenceHelper.getUSERID());
        map.put(Const.Param.TOKEN,preferenceHelper.getToken());
        map.put(Const.Param.TIME_ZONE,preferenceHelper.getDeviceTimeZone());
        map.put(Const.Param.PAGE,String.valueOf(pageNumber));

        new HttpRequester(getActivity(),map,Const.ServiceCode.PREVIOUS_JOB, Const.HttpMethod.POST,this);
        if(isShowLoading){
            Andyutils.showCustomProgressDialog(getActivity(),false);
        }

    }

    @Override
    public void onTaskCompleted(String response, int serviceCode) {

        switch (serviceCode){

            case Const.ServiceCode.PREVIOUS_JOB:
                Andyutils.removeCustomProgressDialog();
                AppLog.Log("pre","response = "+response);
                if(parseContent.issucess(response)){
                    showPreviousJobData(response);
                }
                break;
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser && !preferenceHelper.getIsLoadPriviousJob()){
            AppLog.Log("pre","into previou sjob ");
            isFirstLoad= true;
            pageNo = 1;
            previousJobService(pageNo,true,true);
            preferenceHelper.putIsLoadPriviousJob(true);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        AppLog.Log("pre","Destroy previous job ");
    }

    public void showPreviousJobData(String response){

        if(isclearList){
            previousJobArrayList.clear();
            isclearList = false;
        }
        previousJobArrayList = parseContent.parsePreviousjob(response,previousJobArrayList);
        totalPage = preferenceHelper.getTotalPage();
        adapter.notifyDataSetChanged();
        Andyutils.showEmaptyData(previousJobArrayList.size(),txtEmpty);
        Andyutils.removeSimpleProgressDialog();
        AppLog.Log("pre","on task complete - total - pagno"+totalPage+"//"+pageNo+"//"+previousJobArrayList.size());

        if(isFirstLoad){
            pageNo+=1;
            isFirstLoad = false;
        }
        if(totalPage >= pageNo){

            adapter.isShowLoadingPanel(true);

        }else {
            adapter.isShowLoadingPanel(false);
        }
    }


    @Override
    public void onClickRecyclerListIteam(View view, int position) {


           Intent intent = new Intent(getActivity(), PreviosJobinfoActivity.class);
           intent.putExtra("position",position);
           intent.putExtra("previous_job",previousJobArrayList.get(position));
           startActivity(intent);

    }
}
