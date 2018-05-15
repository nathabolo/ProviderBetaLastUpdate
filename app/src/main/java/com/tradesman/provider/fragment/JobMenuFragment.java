package com.tradesman.provider.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.jimmiejobscreative.tradesman.provider.R;
import com.tradesman.provider.ProfileActivity;
import com.tradesman.provider.ProviderTypeActivity;
import com.tradesman.provider.ViewQuoteActivity;
import com.tradesman.provider.dialog.CustomDialogNoEdt;


/**
 * Created by Elluminati Mohit on 1/26/2017.
 */

public class JobMenuFragment extends Fragment {
    private CustomDialogNoEdt logoutDialog;

    public static JobMenuFragment newInstanse(){

            return new JobMenuFragment();
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.jobmenufragmnet, container, false);
        Button profile = (Button) view.findViewById(R.id.profile);
        Button btntypes = (Button) view.findViewById(R.id.btntypes);

        Button viewQuotes = (Button) view.findViewById(R.id.viewQoats);
        viewQuotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ViewQuoteActivity.class);
                startActivity(intent);
            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ProfileActivity.class);
                startActivity(intent);
            }
        });

        btntypes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ProviderTypeActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }


}
