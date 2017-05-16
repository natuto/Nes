package com.liuwensong.nes_demo.utils;

import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static com.liuwensong.nes_demo.R.drawable.i;

/**
 * Created by song on 2017/5/15.
 */

public class HttpUtils {
    public static  void  getJsonString(String url ,HttpCallbackListener listener){
        try {
            URL httpUrl =new URL(url);
            HttpURLConnection httpURLConnection = (HttpURLConnection)httpUrl.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setConnectTimeout(8000);
            httpURLConnection.setReadTimeout(8000);
            if (httpURLConnection.getResponseCode() ==  200) {
                InputStream inputStream = httpURLConnection.getInputStream();
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                int len =0;
                byte[] bytes =new byte[1024];
                while ((len =inputStream.read(bytes)) != -1) {
                           out.write(bytes,0,len);
                    out.flush();
                }
                inputStream.close();
                String string = out.toString();
                Log.i("33333","33333333333");
                out.close();
                if (listener!= null) {
                    listener.onFinish(string);
                }
            }

        } catch (Exception e) {
          listener.onError(e); ;
        }
    }
    public  interface HttpCallbackListener{
        void onFinish(String response);
        void onError(Exception e);

    }
}
