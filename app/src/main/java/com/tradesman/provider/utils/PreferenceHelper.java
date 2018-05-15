package com.tradesman.provider.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Elluminati Mohit on 1/5/2017.
 */

public class PreferenceHelper {


    private static SharedPreferences appPrefs;
    private final  String providerSelected = "isproviderselected";
    private final  String deviceToken ="device_token";
    private final  String token ="token";
    private final  String userName ="name";
    private final  String email ="email";
    private final  String userId ="id";
    private final  String phoneNumber = "phone";
    private final  String isproviderApprove = "is_approve";
    private final  String latitude ="latitude";
    private final  String longitude ="longitude";
    private final  String deviceTimeZone = "time_zone";
    private final  String address = "address";
    private final  String countryCode ="country_code";
    private final  String selectedTypes = "selected_types";
    private final String selectedTypeId ="selected_type_id";
    private  static PreferenceHelper preferenceHelper;
    private final String TOTAL_PAGE ="total_page";
    private final  String userImage = "userImagePath";
    private final String IS_LOAD = "is_load";
    private final String SYMBOL = "symbol";
    private final  String userToken ="token_bal";
    private final String C_NO = "c_no";
    private final String CVV = "cvv";
    private final String CTYP = "c_typ";
    private final String EXP_M="exp_m";
    private final String tokenPurchase="token_purchase";
    private final  String EXP_YR ="exp_yr";

    public PreferenceHelper(){

    }

    public static PreferenceHelper getInstance(Context context){
        appPrefs = context.getSharedPreferences("tread_pref",
                Context.MODE_PRIVATE);
        if(preferenceHelper == null){
            preferenceHelper = new PreferenceHelper();
        }
        return preferenceHelper;
    }

    public void putDeviceToken(String deviceToken){
        putStringData(this.deviceToken,deviceToken);
    }

    public String getDeviceToken(){

        return appPrefs.getString(deviceToken,null);
    }

    public void putisProviderTypeSelected(boolean value){
        putBoolenData(providerSelected,value);
    }

    public boolean getisProviderTypeSelected(){

        return appPrefs.getBoolean(providerSelected,false);
    }
    public void puttoken(String token){
        putStringData(this.token,token);
    }

    public String getToken(){
        return appPrefs.getString(token,null);
    }
    public void putUserName(String name){
        putStringData(userName,name);
    }
    public String getUserName(){
        return  appPrefs.getString(userName,null);
    }


    public void putTokenPurchase(String token_purchase){
        putStringData(tokenPurchase,token_purchase);
    }
    public String getTokenPurchase(){
        return  appPrefs.getString(tokenPurchase,null);
    }

    public void putUserTokens(String token){
        putStringData(userToken,token);
    }
    public String getUserToken(){
        return  appPrefs.getString(userToken,null);
    }

    public void putUserImagePath(String filepath){
        putStringData(userImage,filepath);
    }
    public String getUserImagePath(){

        return appPrefs.getString(userImage,null);
    }

    public void putUSERID(String id){
        putStringData(userId,id);

    }
    public void putCVV(String cvv){
        SharedPreferences.Editor edit = appPrefs.edit();
        edit.putString(CVV, cvv);
        edit.commit();
    }

    public String getCVV(){
        return appPrefs.getString(CVV, null);
    }

    public void putCTYP(String ctyp){
        SharedPreferences.Editor edit = appPrefs.edit();
        edit.putString(CTYP, ctyp);
        edit.commit();
    }

    public String getCTYP(){
        return appPrefs.getString(CTYP, null);
    }


    public void putC_NO(String c_no){
        SharedPreferences.Editor edit = appPrefs.edit();
        edit.putString(C_NO, c_no);
        edit.commit();
    }

    public String getC_NO(){
        return appPrefs.getString(C_NO, null);
    }


    public void putEXP_YR(String exp_yr){
        SharedPreferences.Editor edit = appPrefs.edit();
        edit.putString(EXP_YR, exp_yr);
        edit.commit();
    }

    public String getEXP_YR(){
        return appPrefs.getString(EXP_YR, null);
    }


    public void putEXP_M(String exp_m){
        SharedPreferences.Editor edit = appPrefs.edit();
        edit.putString(EXP_M, exp_m);
        edit.commit();
    }

    public String getEXP_M(){
        return appPrefs.getString(EXP_M, null);
    }

    public String getUSERID(){

         return appPrefs.getString(userId,null);
    }
    public void putEmail(String email){
        putStringData(this.email,email);
    }

    public String getEmail(){
        return  appPrefs.getString(email,null);
    }

    public void putPhoneNumber(String number){
        putStringData(phoneNumber,number);
    }

    public String getPhoneNumber(){
        return  appPrefs.getString(phoneNumber,null);
    }

    public void logout() {
            putUSERID(null);
            puttoken(null);
        appPrefs.edit().clear();
    }

    public  void putProvidestatus(boolean value){
        putBoolenData(isproviderApprove,value);

    }

    public boolean getProviderStatus(){

        return appPrefs.getBoolean(isproviderApprove,false);
    }

    public  void putLatitude(String latitude){
        putStringData(this.latitude,latitude);
    }

    public String  getLatitude(){

        return appPrefs.getString(latitude,null);
    }

    public  void putLongitude(String longitude){
        putStringData(this.longitude,longitude);

    }


    public String  getLongitude(){

        return appPrefs.getString(longitude,null);
    }



    public void putTimeZone(String timeZone){
        putStringData(deviceTimeZone,timeZone);

    }


    public String getDeviceTimeZone(){
        return  appPrefs.getString(deviceTimeZone,null);
    }

    public void putAddress(String address){
        putStringData(this.address,address);
    }

    public String getAddress(){
        return appPrefs.getString(address,null);
    }
    public void putCountryCode(String code){
        putStringData(countryCode,code);
    }

    public String getCountryCode(){
        return appPrefs.getString(countryCode,null);
    }

    public void putTotalPage(int page){
        SharedPreferences.Editor editor = appPrefs.edit();
        editor.putInt(TOTAL_PAGE,page);
        editor.apply();
    }

    public int getTotalPage(){
        return appPrefs.getInt(TOTAL_PAGE,0);
    }

    public void putIsLoadPriviousJob(boolean value){
        putBoolenData(IS_LOAD,value);
    }
    public boolean getIsLoadPriviousJob(){
        return appPrefs.getBoolean(IS_LOAD,false);
    }


    public void putSelectedType(String type){
        String typeData=type.trim();
        if(typeData.length() >0 && typeData.charAt(typeData.length()-1) == '|'){
            typeData = typeData.substring(0,typeData.length()-1);
        }
        AppLog.Log("tagg","Selcted Type in pref - parse = "+typeData);
        putStringData(selectedTypes,typeData);
    }
    public String getSelectedType(){
        return appPrefs.getString(selectedTypes,null);
    }

    public String getSelectedTypeId(){
        return appPrefs.getString(selectedTypeId,null);
    }

    public void putSelectedTypeId(String typeId){

        String typeData=typeId.trim();
        if(typeData.length() >0 && typeData.charAt(typeData.length()-1) == ','){
            typeData = typeData.substring(0,typeData.length()-1);
        }
        AppLog.Log("tagg","Selcted Type in pref - parse = "+typeData);
        putStringData(selectedTypeId,typeData);
    }

    private void putStringData(String key, String value){

        SharedPreferences.Editor editor = appPrefs.edit();
        editor.putString(key,value);
        editor.apply();
    }
    private void putBoolenData(String key, boolean value){
        SharedPreferences.Editor editor = appPrefs.edit();
        editor.putBoolean(key,value);
        editor.apply();
    }

}
