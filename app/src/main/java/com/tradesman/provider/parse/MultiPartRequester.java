package com.tradesman.provider.parse;

import android.app.Activity;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.tradesman.provider.utils.AppLog;
import com.jimmiejobscreative.tradesman.provider.R;;
import com.tradesman.provider.utils.Andyutils;
import com.tradesman.provider.utils.Const;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

/**
 * Created by Elluminati Mohit on 1/18/2017.
 */

public class MultiPartRequester {

    private Map<String, String> map;
    public  String URL = "http://jimmiejob.com/index.php/";
    private AsyncTaskCompleteListener mAsynclistener;
    private int serviceCode;
    private HttpURLConnection httpURLConnection;
    private String boundary = "-------------" + System.currentTimeMillis();
    private static final String LINE_FEED = "\r\n";
    private static final String TWO_HYPHENS = "--";
    private DataOutputStream dos;


    public MultiPartRequester(Activity activity , Map<String , String> map , int serviceCode , AsyncTaskCompleteListener asyncTaskCompleteListener){
        try {
            this.map = map;
            this.serviceCode = serviceCode;
            this.mAsynclistener = asyncTaskCompleteListener;

            if (Andyutils.isNetworkAvailable(activity)) {
                new AsyncHttpRequest().execute(map.get(URL));
            } else {
                Andyutils.showToast(activity, activity.getString(R.string.error_no_internet));
            }
        }catch (Exception e){

        }
    }


    private class AsyncHttpRequest extends AsyncTask<String , Void , String>{

        @Override
        protected String doInBackground(String... urls) {
            map.remove("url");
            try{
                Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
                URL url = new URL(urls[0]);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setConnectTimeout(60000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setRequestProperty("Accept", "application/json");
                httpURLConnection.setRequestProperty("Content-type", "multipart/form-data; boundary=" + boundary);
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);
                dos = new DataOutputStream(httpURLConnection.getOutputStream());

                for (Map.Entry<String, String> entry : map.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();
                    if (key.equalsIgnoreCase(Const.Param.PICTURE) && !TextUtils.isEmpty(value)) {
                        File f = new File(value);
                        addFilePart(key , f);
                    }else {
                        addParameterPart(key , value);
                    }
                }
                //Read the response...
                BufferedReader reader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(), "UTF-8"));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
                reader.close();
                return sb.toString();

            }
            catch (Exception e){
                AppLog.Log(Const.TAG,Const.EXCEPTION+e);
            }
            finally {
                httpURLConnection.disconnect();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String response) {
            if (mAsynclistener != null) {
                mAsynclistener.onTaskCompleted(response, serviceCode);
            }
        }

        private void addParameterPart(String fieldName, String value) {
            try {
                dos.writeBytes(TWO_HYPHENS + boundary + LINE_FEED);
                dos.writeBytes("Content-Disposition: form-data; name=\"" + fieldName + "\"" + LINE_FEED + LINE_FEED);
                dos.writeBytes(value + LINE_FEED);
            } catch (IOException e) {
                AppLog.Log(Const.TAG,Const.EXCEPTION+e);
            }
        }

        private void addFilePart(String fieldName, File uploadFile) throws IOException {
            try{
            FileInputStream fStream = null;
            int length;
            try {
                dos.writeBytes(TWO_HYPHENS + boundary + LINE_FEED);
                dos.writeBytes("Content-Disposition: form-data; name=\"" + fieldName + "\";filename=\"" + uploadFile.getName() + "\"" + LINE_FEED);
                dos.writeBytes(LINE_FEED);

               fStream =  new FileInputStream(uploadFile);
                int bufferSize = 1024;
                byte[] buffer = new byte[bufferSize];


                while ((length = fStream.read(buffer)) != -1) {
                    dos.write(buffer, 0, length);
                }
                dos.writeBytes(LINE_FEED);
                dos.writeBytes(TWO_HYPHENS + boundary + TWO_HYPHENS + LINE_FEED);

            } catch (IOException e) {
                AppLog.Log(Const.TAG,Const.EXCEPTION+e);
            }finally {
                if(fStream != null){
                        fStream.close();

                }
            }

        }catch (Exception e) {
            }
            }

    }
}
