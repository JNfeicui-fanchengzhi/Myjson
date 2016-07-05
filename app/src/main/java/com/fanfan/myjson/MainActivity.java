package com.fanfan.myjson;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "dhjdh";
    private String str = "Http://www.kuaidi100.com/query?type=shunfeng&postid=304312442386";
    private ListView mlv;
    private JSONArray dataArray;
    private Handler mHandler = new Handler() {
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mlv = (ListView) findViewById(R.id.lv_list);
        new Thread() {
            @Override
            public void run() {
                dataArray = getData();
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mlv.setAdapter(ba);
                    }
                });
            }
        }.start();
    }

    private JSONArray getData() {
        HttpURLConnection mConnection = null;
        JSONArray list = null;
        try {
            URL url = new URL(str);
            mConnection = (HttpURLConnection) url.openConnection();
            mConnection.setConnectTimeout(5000);//设置超时时间
            mConnection.setRequestMethod("POST");//设置请求方式
            mConnection.connect();
            //判断是否连接成功
            if (mConnection.getResponseCode() == 200) {
                //获取到读取流
                InputStream is = mConnection.getInputStream();
                Log.d(TAG, "is: " + is);
                String str1 = readfrom(is);
                Log.d(TAG, "str1: " + str1);
                JSONObject object = new JSONObject(str1);
                Log.d(TAG, "object: " + object);
                list = object.getJSONArray("data");
                Log.d(TAG, "list: " + list);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    BaseAdapter ba = new BaseAdapter() {
        @Override
        public int getCount() {
            return dataArray.length();
        }

        @Override
        public JSONObject getItem(int position) {
            JSONObject obj = null;
            try {
                obj = dataArray.getJSONObject(position);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return obj;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            JSONObject object = getItem(position);
            View view = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.list_item_main, null);
            TextView time = (TextView) view.findViewById(R.id.tv_time);
            TextView ftime = (TextView) view.findViewById(R.id.tv_ftime);
            TextView context = (TextView) view.findViewById(R.id.tv_context);
            try {
                time.setText(object.getString("time"));
                ftime.setText(object.getString("ftime"));
                context.setText(object.getString("context"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return view;
        }
    };

    public String readfrom(InputStream is) throws IOException {

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] bytes = new byte[1024];
        int len = 0;
        //不断的读写
        while ((len = is.read(bytes)) != -1) {
            bos.write(bytes, 0, len);
        }
        is.close();
        return new String(bos.toByteArray(), "UTF-8");
    }
}
