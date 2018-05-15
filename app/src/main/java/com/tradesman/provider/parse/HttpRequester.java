package com.tradesman.provider.parse;

import android.app.Activity;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.tradesman.provider.utils.AppLog;
import com.tradesman.provider.utils.Const;
import com.jimmiejobscreative.tradesman.provider.R;;
import com.tradesman.provider.utils.Andyutils;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.Executors;

/**
 * Created by Elluminati Mohit on 1/13/2017.
 */

public class HttpRequester {

    private Map<String, String> map;
    public  String URL = "http://jimmiejob.com/index.php/";
    private AsyncTaskCompleteListener mAsynclistener;
    private int serviceCode;
    private String httpMethod;

    public HttpRequester(Activity activity, Map<String, String> map,
                         int serviceCode, String  HttpMethod,
                         AsyncTaskCompleteListener asyncTaskCompleteListener) {
        try {
            this.map = map;
            this.serviceCode = serviceCode;
            this.httpMethod = HttpMethod;
            this.mAsynclistener = asyncTaskCompleteListener;


            if (Andyutils.isNetworkAvailable(activity)) {
                new AsyncHttpRequest()
                        .executeOnExecutor(Executors.newSingleThreadExecutor(),
                                map.get(URL));
            } else {
                Andyutils.removeSimpleProgressDialog();
                Andyutils.showToast(activity, activity.getString(R.string.error_no_internet));
            }
        }catch (Exception e){

        }
    }

    private class AsyncHttpRequest  extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            map.remove(URL);

            try {
                Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
                if (TextUtils.equals(Const.HttpMethod.POST, httpMethod)) {

                    JSONObject parseJson = new JSONObject();
                    for (Map.Entry<String, String> entry : map.entrySet()) {
                        String key = entry.getKey();
                        String value = entry.getValue();
                        parseJson.put(key, value);
                    }
                    URL url = new URL(urls[0]);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    sendDAta(connection, parseJson);
                    return getData(connection);

                } else if (TextUtils.equals(Const.HttpMethod.GET, httpMethod)) {
                    URL url;
                    HttpURLConnection connection;
                    url = new URL(urls[0]);
                    connection = (HttpURLConnection) url.openConnection();
                    return getData(connection);
                }
            } catch (Exception e) {
                AppLog.Log(Const.TAG,Const.EXCEPTION+e);
                return null;

            }

            return null;

        }

        @Override
        protected void onPostExecute(String response) {

            if (mAsynclistener != null) {
                mAsynclistener.onTaskCompleted(response, serviceCode);

            }

        }
    }


    private String getData(HttpURLConnection connection){

        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream(),"UTF-8"));
            StringBuilder sb = new StringBuilder();
            String line;

            while((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
            bufferedReader.close();
            return sb.toString();
        } catch (IOException e) {
            AppLog.Log(Const.TAG,Const.EXCEPTION+e);
            return null;
        }

    }


    private void sendDAta(HttpURLConnection connection, JSONObject jsonObject){

        try {
            connection.setRequestMethod(Const.HttpMethod.POST);
           connection.setReadTimeout(15000);     /* milliseconds */
            connection.setConnectTimeout(15000);     /* milliseconds */
            connection.setRequestProperty("Content-Type","application/json");
            connection.setRequestProperty("Accept","application/json");
            connection.setDoInput(true);
            connection.setDoOutput(true);

            OutputStream os = connection.getOutputStream();

            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(String.valueOf(jsonObject));

            writer.flush();
            writer.close();
            os.close();
        } catch (IOException e) {
            AppLog.Log(Const.TAG,Const.EXCEPTION+e);
        }
    }
}


