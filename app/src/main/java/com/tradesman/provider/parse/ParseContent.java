package com.tradesman.provider.parse;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;

import com.tradesman.provider.AvailableJobActivity;
import com.jimmiejobscreative.tradesman.provider.R;;
import com.tradesman.provider.SpleshScreenActivity;
import com.tradesman.provider.model.ActiveJob;
import com.tradesman.provider.model.AvailableJob;
import com.tradesman.provider.model.CountryCode;
import com.tradesman.provider.model.PreviousJob;
import com.tradesman.provider.model.ProviderType;
import com.tradesman.provider.model.Statistics;
import com.tradesman.provider.model.ViewQuote;
import com.tradesman.provider.utils.Andyutils;
import com.tradesman.provider.utils.AppLog;
import com.tradesman.provider.utils.Const;
import com.tradesman.provider.utils.PreferenceHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Elluminati Mohit on 1/13/2017.
 */

public class ParseContent {

    private static final String KEY_SUCESS = "success";
    private static final  String KEY_ERROR_MESSAGES = "error_messages";
    private static final String USER_ID = "id";
    private PreferenceHelper preferenceHelper;
    private static final String IS_POVIDER_APPROVE = "is_approve";
    private static final String EXCEPTION = "Exception";
    private static final String ISSUE_IMAGE = "issue_image";
    private static final String START_TIME = "start_time";
    private static final String JOB_TITLE = "job_title";
    private static final String JOB_DESCRIPTION = "description";
    private static final String DISTANCE = "distance";
    private static final String DISTANCE_UNIT = "distance_unit";
    private static final String REQUEST_TYPE = "request_type";
    private static final String ADDRESS = "address";
    private static final String REQUEST_STATUS = "request_status";
    private static final String TOTAL = "total";
    private static final String USER = "user";
    private static final String NAME = "name";
    private static final String PICTURE = "picture";
    private static final String RATE = "rate";
    private static final String TYPE = "type";
    private static final String JOBS = "jobs";
    private static final String CURRENCY = "currency";
    private static final String latitude = "latitude";
    private static final String longitude = "longitude";
    private static final String token_bal = "token_bal";
    private static final String SYMBOL = "symbol";
    private int count = 0;
    private Activity activity;

    public ParseContent(Activity activity) {
        this.activity = activity;
        preferenceHelper = PreferenceHelper.getInstance(activity);
    }


    public boolean isstatusSucess(String response) {
        if (TextUtils.isEmpty(response)) {
            return false;
        }

        try {
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.getBoolean(KEY_SUCESS)) {
                count++;
                int getStaus = jsonObject.getInt(IS_POVIDER_APPROVE);
                storeProviderApproveStatus(getStaus);
                return true;
            }
        } catch (JSONException e) {
            AppLog.Log(Const.TAG, EXCEPTION + "EXCEPTION in parse" + e);
        }

        return false;

    }

    private void storeProviderApproveStatus(int status){
        if (count == 3 && status == 1) {
            preferenceHelper.putProvidestatus(true);
            count =0;

        } else if (count == 3 && status == 0) {
            preferenceHelper.putProvidestatus(false);
            count =0;
        }
    }

    public boolean issucess(String response) {

        if (TextUtils.isEmpty(response)) {
            Andyutils.showToast(activity, activity.getString(R.string.error_response_null));
            return false;
        }
        try {
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.getBoolean(KEY_SUCESS)) {

                return true;

            } else {

                if (jsonObject.getJSONArray(KEY_ERROR_MESSAGES).getInt(0) == Const.ERROR_SESSION_EXPIRE_CODE) {
                    preferenceHelper.logout();
                    Intent i = new Intent(activity, SpleshScreenActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    activity.startActivity(i);
                } else if (jsonObject.getJSONArray(KEY_ERROR_MESSAGES).getInt(0) == Const.PROVIDER_DECLINE) {

                    preferenceHelper.putProvidestatus(false);

                    Intent i = new Intent(activity, AvailableJobActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    activity.startActivity(i);

                } else {
                    Andyutils.showErrorToast(
                            jsonObject.getJSONArray(KEY_ERROR_MESSAGES).getInt(0),
                            activity);
                }
                return false;
            }

        } catch (JSONException e) {
            AppLog.Log(Const.TAG, EXCEPTION + e);
        }

        return false;
    }

    public boolean isSuccessWithStoreId(String response) {
        final String countryCode = "phone_code";
        final String email = "email";
        final String userName = "name";
        final String phoneNumber = "phone";
        final String token = "token";
        if (TextUtils.isEmpty(response)) {
            Andyutils.showToast(activity, activity.getString(R.string.error_response_null));
            return false;
        }

        try {
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.getBoolean(KEY_SUCESS)) {
                AppLog.Log("tag","Reg Response - "+response);
                preferenceHelper.puttoken(jsonObject.getString(token));
                preferenceHelper.putUSERID(jsonObject.getString(USER_ID));
                preferenceHelper.putEmail(jsonObject.getString(email));
                preferenceHelper.putPhoneNumber(jsonObject.getString(phoneNumber));
                preferenceHelper.putUserName(jsonObject.getString(userName));
                preferenceHelper.putTimeZone(Andyutils.getTimeZone());
                preferenceHelper.putAddress(jsonObject.getString(ADDRESS));
                preferenceHelper.putUserTokens(jsonObject.getString("token_bal"));
                preferenceHelper.putCountryCode(jsonObject.getString(countryCode));
                preferenceHelper.putC_NO(jsonObject.getString("c_no"));
                preferenceHelper.putCVV(jsonObject.getString("cvv"));
                preferenceHelper.putCTYP(jsonObject.getString("c_typ"));
                preferenceHelper.putTokenPurchase(jsonObject.getString("token_purchase"));
                preferenceHelper.putEXP_YR(jsonObject.getString("exp_yr"));
                preferenceHelper.putEXP_M(jsonObject.getString("exp_m"));
                preferenceHelper.putUserTokens(jsonObject.getString("token_bal"));
                preferenceHelper.putUserImagePath(jsonObject.getString(PICTURE));
                getSelectedType(response);
                switch (jsonObject.getInt(IS_POVIDER_APPROVE)) {

                    case 0:
                        preferenceHelper.putProvidestatus(false);
                        break;
                    case 1:
                        preferenceHelper.putProvidestatus(true);
                        break;
                    default:
                        AppLog.Log(Const.TAG, "Default case");
                        break;

                }
                return true;
            } else {
                Andyutils.showErrorToast(
                        jsonObject.getJSONArray(KEY_ERROR_MESSAGES).getInt(0),
                        activity);

                return false;
            }

        } catch (JSONException e1) {
            AppLog.Log(Const.TAG, EXCEPTION + e1);
            return false;
        }

    }

    public boolean isSucessWithProviderType(String response){
        if (TextUtils.isEmpty(response)) {
            Andyutils.showToast(activity, activity.getString(R.string.error_response_null));
            return false;
        }
        try {
            JSONObject jsonObject = new JSONObject(response);
        if (jsonObject.getBoolean(KEY_SUCESS)) {}{
                getSelectedType(response);
                return true;
            }
        } catch (JSONException e1) {
            AppLog.Log(Const.TAG, EXCEPTION + e1);
            return false;
        }
    }

    private void getSelectedType(String response) {
        try {
            JSONArray selectedTypeArray = new JSONObject(response).getJSONArray("selected_types");
            StringBuilder getType = new StringBuilder();
            StringBuilder getTypeId = new StringBuilder();
            for (int i = 0; i < selectedTypeArray.length(); i++) {
                JSONObject typeObject = selectedTypeArray.getJSONObject(i);
                if (TextUtils.equals(typeObject.getString("selected"), "1")) {
                    getType.append(typeObject.getString("name") + " | ");
                    getTypeId.append(typeObject.getString("id") + ",");
                }
            }
            preferenceHelper.putSelectedType(getType.toString());
            preferenceHelper.putSelectedTypeId(getTypeId.toString());
            AppLog.Log("tagg","Selected Type pref = "+ preferenceHelper.getSelectedType());

        } catch (JSONException e) {
            AppLog.Log(Const.TAG, EXCEPTION + e);
        }
    }

    public int getPushId(String response) {

        try {
            JSONObject jsonObject = new JSONObject(response);
            return jsonObject.getInt(Const.ProviderPushStatus.PUSH_ID);
        } catch (JSONException e) {
            AppLog.Log(Const.TAG, EXCEPTION + e);
        }

        return -1;
    }


    public List<ProviderType> parseTypesOfProviders(String response, List<ProviderType> listProviderType) {
        final String providerTypeData = "types";

        if (TextUtils.isEmpty(response))
            return listProviderType;

        try {
            JSONArray jsonArray = new JSONObject(response).getJSONArray(providerTypeData);

            for (int i = 0; i < jsonArray.length(); i++) {
                ProviderType providerType = new ProviderType();
                providerType.setId(jsonArray.getJSONObject(i).getString(Const.Param.USER_ID));
                providerType.setName(jsonArray.getJSONObject(i).getString(Const.Param.NAME));
                providerType.setPicture(jsonArray.getJSONObject(i).getString(Const.Param.PICTURE));
                listProviderType.add(providerType);
            }
        } catch (Exception e) {
            AppLog.Log(Const.TAG, EXCEPTION + e);
        }

        return listProviderType;
    }

    public boolean isProviderselected(String response) {

        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray(Const.Param.SELECTED_TYPE);

            for (int i = 0; i < jsonArray.length(); i++) {
                String selected = jsonArray.getJSONObject(i).getString(Const.Param.SELCTED);
                if (TextUtils.equals(selected, "1")) {
                    return true;
                }
            }
        } catch (JSONException e) {
            AppLog.Log(Const.TAG, EXCEPTION + e);
        }
        return false;
    }

    public String getSelectedTypeJson(List<String> arrayList) {

        try {
            JSONObject finalJson = new JSONObject();
            JSONArray jsonArray = new JSONArray();

            for (int i = 0; i < arrayList.size(); i++) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put(USER_ID, Integer.parseInt(arrayList.get(i)));
                jsonArray.put(jsonObject);
            }
            return finalJson.put("types", jsonArray).toString();

        } catch (JSONException e) {
            AppLog.Log(Const.TAG, EXCEPTION + e);
        }
        return null;
    }

    public List<AvailableJob> parseAvailableJob(String response, List<AvailableJob> availableJobArrayList) {


        try {

            JSONArray jobArray = new JSONObject(response).getJSONArray(JOBS);

            for (int i = 0; i < jobArray.length(); i++) {
                AvailableJob availableJob = new AvailableJob();
                availableJob.setRequestId(jobArray.getJSONObject(i).getString(USER_ID));
                availableJob.setJobTitle(jobArray.getJSONObject(i).getString(JOB_TITLE));
                availableJob.setDescription(jobArray.getJSONObject(i).getString(JOB_DESCRIPTION));
                availableJob.setLatitude(jobArray.getJSONObject(i).getString(latitude));
                availableJob.setLongitude(jobArray.getJSONObject(i).getString(longitude));
                availableJob.setDistance(jobArray.getJSONObject(i).getString(DISTANCE));
                availableJob.setDistanceUnit(jobArray.getJSONObject(i).getString(DISTANCE_UNIT));
                availableJob.setIssueImgeUrl(jobArray.getJSONObject(i).getString(ISSUE_IMAGE));
                availableJob.setStartTime(jobArray.getJSONObject(i).getString(START_TIME));
                availableJob.setJobType(jobArray.getJSONObject(i).getString(REQUEST_TYPE));
                availableJob.setAddress(jobArray.getJSONObject(i).getString(ADDRESS));
                availableJob.setQ(jobArray.getJSONObject(i).getString(TOTAL));

                JSONObject userObject = jobArray.getJSONObject(i).getJSONObject(USER);
                availableJob.setUserName(userObject.getString(NAME));
                availableJob.setUserImageUrl(userObject.getString(PICTURE));
                availableJob.setUserRating(userObject.getString(RATE));

                JSONObject typeObject = jobArray.getJSONObject(i).getJSONObject(TYPE);
                availableJob.setServiceName(typeObject.getString(NAME));
                availableJob.setServiceImageUrl(typeObject.getString(PICTURE));
                availableJob.setCurrency(typeObject.getString(SYMBOL));
                availableJob.setAdminPrice(typeObject.getString("admin_charge"));

                availableJobArrayList.add(availableJob);
            }

        } catch (JSONException e) {
            AppLog.Log(Const.TAG, EXCEPTION + e);
        }

        return availableJobArrayList;
    }

    public List<ViewQuote> parseViewQuote(String response, List<ViewQuote> quoteArrayList) {

        final String quotation = "quotation";
        final String quotationDate = "quotation_date";

        try {

            JSONArray jobArray = new JSONObject(response).getJSONArray(quotation);

            for (int i = 0; i < jobArray.length(); i++) {
                ViewQuote viewQuote = new ViewQuote();
                viewQuote.setId(jobArray.getJSONObject(i).getString(USER_ID));
                viewQuote.setJobTitle(jobArray.getJSONObject(i).getString(JOB_TITLE));
                viewQuote.setDescription(jobArray.getJSONObject(i).getString(JOB_DESCRIPTION));
                viewQuote.setDistance(jobArray.getJSONObject(i).getString(DISTANCE));
                viewQuote.setDistanceUnit(jobArray.getJSONObject(i).getString(DISTANCE_UNIT));
                viewQuote.setIssueImageUrl(jobArray.getJSONObject(i).getString(ISSUE_IMAGE));
                viewQuote.setStartTime(jobArray.getJSONObject(i).getString(START_TIME));
                viewQuote.setAddress(jobArray.getJSONObject(i).getString(ADDRESS));
                viewQuote.setRequestType(jobArray.getJSONObject(i).getString(REQUEST_TYPE));
                viewQuote.setQuotation(jobArray.getJSONObject(i).getString(quotation));
                viewQuote.setQuotationDate(jobArray.getJSONObject(i).getString(quotationDate));
                viewQuote.setCurrency(jobArray.getJSONObject(i).getString(SYMBOL));
                viewQuote.setSymbol(jobArray.getJSONObject(i).getString(SYMBOL));
                viewQuote.setLatitude(jobArray.getJSONObject(i).getString(latitude));
                viewQuote.setLongitude(jobArray.getJSONObject(i).getString(longitude));
                JSONObject userObject = jobArray.getJSONObject(i).getJSONObject(USER);
                viewQuote.setUserName(userObject.getString(NAME));
                viewQuote.setUserImageUrl(userObject.getString(PICTURE));
                viewQuote.setUserRating(userObject.getString(RATE));

                JSONObject typeObject = jobArray.getJSONObject(i).getJSONObject(TYPE);
                viewQuote.setServiceName(typeObject.getString(NAME));
                viewQuote.setServiceImageUrl(typeObject.getString(PICTURE));


                quoteArrayList.add(viewQuote);
            }

        } catch (JSONException e) {
            AppLog.Log(Const.TAG, EXCEPTION + e);
        }

        return quoteArrayList;
    }

    public List<ActiveJob> parseActiveJob(String response, List<ActiveJob> activeJobArrayList) {


        try {
            JSONArray jobArray = new JSONObject(response).getJSONArray(JOBS);
            for (int i = 0; i < jobArray.length(); i++) {
                ActiveJob activeJob = new ActiveJob();

                activeJob.setRequestId(jobArray.getJSONObject(i).getString(USER_ID));
                activeJob.setJobTitle(jobArray.getJSONObject(i).getString(JOB_TITLE));
                activeJob.setDescription(jobArray.getJSONObject(i).getString(JOB_DESCRIPTION));
                activeJob.setDistance(jobArray.getJSONObject(i).getString(DISTANCE));
                activeJob.setDistanceUnit(jobArray.getJSONObject(i).getString(DISTANCE_UNIT));
                activeJob.setIssueImgeUrl(jobArray.getJSONObject(i).getString(ISSUE_IMAGE));
                activeJob.setStartTime(jobArray.getJSONObject(i).getString(START_TIME));
                activeJob.setAddress(jobArray.getJSONObject(i).getString(ADDRESS));
                activeJob.setRequestType(jobArray.getJSONObject(i).getString(REQUEST_TYPE));
                activeJob.setQuotation(jobArray.getJSONObject(i).getString(TOTAL));
                activeJob.setRequestStatus(jobArray.getJSONObject(i).getString(REQUEST_STATUS));
                activeJob.setLatitude(jobArray.getJSONObject(i).getString(latitude));
                activeJob.setLongitude(jobArray.getJSONObject(i).getString(longitude));


                activeJob.setCurrency(jobArray.getJSONObject(i).getString(SYMBOL));

                JSONObject userObject = jobArray.getJSONObject(i).getJSONObject(USER);
                activeJob.setUserName(userObject.getString(NAME));
                activeJob.setUserImageUrl(userObject.getString(PICTURE));
                activeJob.setUserRating(userObject.getString(RATE));

                JSONObject typeObject = jobArray.getJSONObject(i).getJSONObject(TYPE);
                activeJob.setServiceName(typeObject.getString(NAME));
                activeJob.setServiceImageUrl(typeObject.getString(PICTURE));
                activeJob.setAdminPrice(typeObject.getString("admin_charge"));

                activeJobArrayList.add(activeJob);

                AppLog.Log(Const.TAG, "List size = " + activeJobArrayList.size());

            }


        } catch (JSONException e) {
            AppLog.Log(Const.TAG, EXCEPTION + e);
        }

        return activeJobArrayList;
    }


    public Statistics parseStatistics(String response) {


        final String memberSince = "member_since";
        final String upcomming = "upcomming";
        final String weekJobs = "week_jobs";
        final String weekEarning = "week_earning";
        final String yearJobs = "year_jobs";
        final String yearEarning = "year_earning";
        final String totalJobs = "total_jobs";
        final String totalEarning = "total_earning";
        Statistics statistics = new Statistics();

        try {
            JSONObject jsonObject = new JSONObject(response);
            statistics.setCurrency(jsonObject.getString(SYMBOL));
            statistics.setMemberSince(jsonObject.getString(memberSince));
            statistics.setUpcomming(jsonObject.getString(upcomming));
            statistics.setWeekJobs(jsonObject.getString(weekJobs));
            statistics.setWeekEarning(jsonObject.getString(weekEarning));
            statistics.setYearJobs(jsonObject.getString(yearJobs));
            statistics.setYearEarning(jsonObject.getString(yearEarning));
            statistics.setTotalJobs(jsonObject.getString(totalJobs));
            statistics.setTotalEarning(jsonObject.getString(totalEarning));
            return statistics;
        } catch (JSONException e) {
            AppLog.Log(Const.TAG, EXCEPTION + e);
        }

        return null;
    }


    public List<PreviousJob> parsePreviousjob(String response, List<PreviousJob> previousJobArrayList) {

        final String totalPageJson = "total_page";
        final String previousJobs = "previous_jobs";
        try {

            JSONObject jsonObject = new JSONObject(response);
            preferenceHelper.putTotalPage(jsonObject.getInt(totalPageJson));
            JSONArray jobArray = jsonObject.getJSONArray(previousJobs);
            for (int i = 0; i < jobArray.length(); i++) {

                PreviousJob previousJob = new PreviousJob();
                previousJob.setRequestId(jobArray.getJSONObject(i).getString(USER_ID));
                previousJob.setJobTitle(jobArray.getJSONObject(i).getString(JOB_TITLE));
                previousJob.setDescription(jobArray.getJSONObject(i).getString(JOB_DESCRIPTION));
                previousJob.setIssueImgeUrl(jobArray.getJSONObject(i).getString(ISSUE_IMAGE));
                previousJob.setStartTime(jobArray.getJSONObject(i).getString(START_TIME));
                previousJob.setAddress(jobArray.getJSONObject(i).getString(ADDRESS));
                previousJob.setRequestType(jobArray.getJSONObject(i).getString(REQUEST_TYPE));
                previousJob.setQuotation(jobArray.getJSONObject(i).getString(TOTAL));
                previousJob.setRequestStatus(jobArray.getJSONObject(i).getString(REQUEST_STATUS));

                previousJob.setCurrency(jobArray.getJSONObject(i).getString(SYMBOL));

                JSONObject userObject = jobArray.getJSONObject(i).getJSONObject(USER);
                previousJob.setUserName(userObject.getString(NAME));
                previousJob.setUserImageUrl(userObject.getString(PICTURE));

                JSONObject typeObject = jobArray.getJSONObject(i).getJSONObject(TYPE);
                previousJob.setServiceName(typeObject.getString(NAME));
                previousJob.setServiceImageUrl(typeObject.getString(PICTURE));
                previousJob.setAdminPrice(typeObject.getString("admin_charge"));

                JSONObject feedbackObject = jobArray.getJSONObject(i).getJSONObject("feedback");
                previousJob.setUserGivenRating(feedbackObject.getDouble("user_given_rate"));
                previousJob.setUserComment(feedbackObject.getString("user_given_comment"));

                previousJobArrayList.add(previousJob);


            }
        } catch (JSONException e) {
            AppLog.Log(Const.TAG, EXCEPTION + e);
        }

        return previousJobArrayList;
    }

    public List<CountryCode> parseCountryCode() {

        ArrayList<CountryCode> listCountry = new ArrayList<>();
        try {
            InputStream inputStream = activity.getResources().openRawResource(
                    R.raw.countrycodes);
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    inputStream));
            StringBuilder result = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
            reader.close();

            JSONArray countryArray = new JSONArray(result.toString());

            for (int i = 0; i < countryArray.length(); i++) {
                CountryCode countryCode = new CountryCode();
                countryCode.setCountryPhoneCode(countryArray.getJSONObject(i).getString("phone-code"));
                countryCode.setCountryName(countryArray.getJSONObject(i).getString("name"));
                listCountry.add(countryCode);
            }
        } catch (Exception e) {
            AppLog.Log(Const.TAG, EXCEPTION + e);
        }

        return listCountry;
    }


}
