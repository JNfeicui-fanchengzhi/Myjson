package com.fanfan.myjson;

import android.test.AndroidTestCase;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Administrator on 2016/7/5.
 */
public class text1 extends AndroidTestCase {
    public static final String TAG = "jaxn";
    String str = "Http://www.kuaidi100.com/query?type=shunfeng&postid=304312442386";

    public void testConnection() {
        HttpURLConnection mConnection = null;

        try {
            mConnection = (HttpURLConnection) new URL(str).openConnection();
            mConnection.setConnectTimeout(5000);//设置超时时间
            mConnection.setRequestMethod("POST");//设置请求方式
            //判断是否连接成功
            if (mConnection.getResponseCode() == 200) {
                //获取到读取流
                InputStream is = mConnection.getInputStream();
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                byte[] bytes = new byte[1024];
                int len = 0;
                //不断的读写
                while ((len = is.read(bytes)) != -1) {
                    bos.write(bytes, 0, len);
                }
                is.close();
                Log.d(TAG, new String(bos.toByteArray(), "UTF-8"));
//                return new String(bos.toByteArray(),"UTF-8");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}