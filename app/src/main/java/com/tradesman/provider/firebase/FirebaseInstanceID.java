package com.tradesman.provider.firebase;


import com.tradesman.provider.utils.PreferenceHelper;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;


/**
 * Created by Elluminati Mohit on 1/5/2017.
 */

public class FirebaseInstanceID extends FirebaseInstanceIdService {


    PreferenceHelper preferenceHelper;
    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        preferenceHelper = PreferenceHelper.getInstance(this);
        String token = FirebaseInstanceId.getInstance().getToken();
        preferenceHelper.putDeviceToken(token);
    }
}
