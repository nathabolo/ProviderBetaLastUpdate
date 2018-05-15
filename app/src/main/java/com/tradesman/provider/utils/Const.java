package com.tradesman.provider.utils;

import android.location.LocationManager;
import android.net.ConnectivityManager;

/**
 * Created by Elluminati Mohit on 1/13/2017.
 */

public class Const {

    public static final String AVAILABLE_JOB = "availablejob";
    public static final String ACTIVE_JOB = "activejob";
    public static final String TAG ="tag";
    public final static String EXCEPTION = "exception";

    /*Timer Task Time*/
    public static final int TIMER_SCHEDULE = 15 * 1000;

    public class ServiceType{

        private static final String HOST_URL = "http://jimmiejob.com/index.php/";

        private static final String BASE_URL = HOST_URL + "provider/";
        public static final String LOGIN =BASE_URL+"login";
        public static final String REGISTER =BASE_URL+"register";
        public static final String UPDATE_PROFILE=BASE_URL+"updateprofile";
        public static final String GET_PROVIDER= BASE_URL +"getproviders";
        public static final String GET_PROVIDER_TYPE = HOST_URL+"application/types";
        public static final String GET_AVAILABLE_JOB = BASE_URL+"getavailablejobs";
        public static final String SELECTED_TYPE=BASE_URL+"selecttypes";
        public static final String ADD_QUOTE = BASE_URL+"addquote";
        public static final String VIEW_QUOTE= BASE_URL+"viewquotes";
        public static final String CANCEL_QUOTE = BASE_URL+"cancelquote";
        public static final String UPDATE_LOCATION = BASE_URL+"updatelocation";
        public static final String ACTIVE_JOB = BASE_URL +"activerequest";
        public static final String ON_THE_WAY = BASE_URL +"ontheway";
        public static final String HAS_ARRIVED = BASE_URL + "hasarrived";
        public static final String JOB_START = BASE_URL + "started";
        public static final String JOB_FINISH = BASE_URL+ "jobdone";
        public static final String FEEDBACK = BASE_URL +"setrate";
        public static final String STATISTICS = BASE_URL+"statistics";
        public static final String PREVIOUS_JOB = BASE_URL + "history";
        public static final String LOGOUT = BASE_URL+ "logout";
        public static final String CANCEL_JOB =BASE_URL+ "cancelconfirmedjob";
        public static final String FORGET_PASSWORD = HOST_URL + "application/forgotpassword";
    }

    public class ServiceCode{
        public static final int REGISTER = 1;
        public static final int LOGIN = 2;
        public static  final int UPDATE_PROFILE =3;
        public static final int GET_PROVIDER_TYPE =5;
        public static final int GET_AVAILABLE_JOB=6;
        public static final int SELECTED_TYPE=7;
        public static final int ADD_QUOTE =8;
        public static final int VIEW_QUOTE=9;
        public static final int CANCEL_QUOTE=10;
        public static final int UPDATE_LOCATION =11;
        public static final int ACTIVE_JOB =12;
        public static final int ON_THE_WAY = 13;
        public static final int HAS_ARRIVED =14;
        public static final int JOB_START = 15;
        public static final int JOB_FINISH = 16;
        public static final int STATISTICS = 17;
        public static final int PREVIOUS_JOB = 18;
        public static final int FEEDBACK = 19;
        public static final int LOGOUT = 20;
        public static final int CANCEL_JOB = 21;
        public static final int FORGET_PASSWORD = 22;
    }

    public class HttpMethod{
        public static final String POST= "POST";
        public static final String GET = "GET";
        public static final String PUT =  "PUT";
        public static final String DELETE= "DELETE";
    }

    public class Param{

        public static final String URL="http://jimmiejob.com/index.php/";
        public static final String NAME="name";
        public static final String EMAIL ="email";
        public static final String PASS ="password";
        public static final String PHONE ="phone";
        public static final String DEVICE_TOKEN="device_token";
        public static final String DEVICE_TYPE="device_type";
        public static final String ANDROID_DEVICE="android";
        public static final String TOKEN ="token";
        public static final String USER_ID="id";
        public static final String LATITUDE="latitude";
        public static final String LONGITUDE="longitude";
        public static final String APP_VERSION = "app_version";
        public static final String AVILABLE_APP_VERSION="1";
        public static final String PICTURE ="picture";
        public static final String TIME_ZONE="time_zone";
        public static final String SELECTED_TYPE="selected_types";
        public static final String SELCTED="selected";
        public static final String TYPES ="types";
        public static final String REQUEST_ID="request_id";
        public static final String QUOTE="quote";
        public static final String USER_TYPE = "user_type";
        public static final String PROVIDER_DEFAULT_TYPE = "0";
        public static final String COUNTRY_CODE = "phone_code";
        public static final String JOB_TITLE="job_title";
        public static final String JOB_DESCRIPTION = "description";
        public static final String DISTANCE = "distance";
        public static final String DISTANCE_UNIT="distance_unit";
        public static final String ADDRESS = "address";
        public static final String PAGE = "page";
        public static final String RATING = "rating";
        public static final String COMMENT = "comment";
        public static final String NEW_PASS = "new_password";
        public static final String OLD_PASS = "old_password";
        public static final String POSITION =  "position";

    }

    public class PushNotificationCount{

        public static final int APPROVE_OR_DECLINE =1;
        public static final int NEW_REQUEST = 2;
        public static final int ACCEPT_COST = 3;
        public static final int DELETE_QUOTE = 4;
        public static final int CANCEL_JOB = 5;
    }

    /*   Error Code */

    public static final String ERROR_CODE_PREFIX = "error_";
    public static final int ERROR_SESSION_EXPIRE_CODE = 9;
    public static final int PROVIDER_DECLINE = 31;

    /* No Reuqest*/

    public static final int NO_REQUEST = -1;

    /*Request permission Code*/

    public static final int PERMISSION_STORAGE_REQUEST_CODE = 200;
    public static final int PERMISSION_CAMERA_REQUEST_CODE=201;
    public static final int PERMISSION_LOCATION_REQUEST_CODE = 202;

   public class ProviderPushStatus {
       public static final String  PROVIDER_STATUS="provider_status";
       public static final String PROVIDER_APPROVE_STATUS="provider_approve_status";
       public static final String PUSH_ID = "push_id";
       public static final String REQUEST_ID ="request_id";
       public static final int PROVIDER_APPOVE = 1;
       public static final int PROVIDER_DECLINE = 2;
       public static final int NEW_REQUEST = 3;
       public static final int ACCEPT_COST_BY_USER = 4;
       public static final int DELETED_PROVIDED_QUOTE =  5;
       public static final int USER_CANCELED_JOB = 6;

   }


    /*Activity Result*/

    public static final int ACTIVITY_AVAILABLE_JOB = 222;
    public static final int ACTIVITY_QUOTE_PROVIDED = 223;
    public static final int PROVIDER_STATE_CHANGE = 224;
    public static final int CHANGE_IN_DATA = 225;
    public static final int REFRESH_LIST = 226;
    public static final int VIEW_QUOTE_CHNAGE = 227;
    public static final int ACTION_SETTING = 228;
    public static final int ACTION_LOCATION_SETTINGS = 229;

    public class ProviderTripStatus{

        public static final int PROVIDER_TRIP_START = 2;
        public static final int PROVIDER_HAS_ARRIVED = 3;
        public static final int PROVIDER_JOB_START = 4;
        public static final int PROVIDER_JOB_DONE = 5;

    }

    /*Brod cast reciver*/

    public static final String  NETWORK_CHANGE = ConnectivityManager.CONNECTIVITY_ACTION;
    public static final String GPS_CHANGE = LocationManager.PROVIDERS_CHANGED_ACTION;
}
