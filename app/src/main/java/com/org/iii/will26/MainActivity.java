package com.org.iii.will26;

import android.content.Context;
import android.graphics.Color;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewDebug;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.logging.Handler;

public class MainActivity extends AppCompatActivity {
    private GridView gridView;
    private MyAdapter myAdapter;
    private MyAdapter2 myAdapter2;
    private int[] imgs = {
            R.drawable.a1, R.drawable.a2, R.drawable.a3,
            R.drawable.a4, R.drawable.a5, R.drawable.a6,
            R.drawable.a7, R.drawable.a8, R.drawable.a9,
            R.drawable.a10 };
    private LinkedList<String> foodNo;
    private UIHandler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        handler = new UIHandler();
        gridView = (GridView) findViewById(R.id.gridView);
        getJSON();
    }

    private void initGridView() {
        myAdapter = new MyAdapter(this);
        myAdapter2 = new MyAdapter2(this);
        gridView.setAdapter(myAdapter2);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.v("will", "i = " + i);
            }
        });

    }

    private void getJSON() {
        new Thread() {
            @Override
            public void run() {
                try {
                    URL url = new URL("http://data.fda.gov.tw/cacheData/19_3.json");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.connect();
                    BufferedReader reader =
                            new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String line = reader.readLine();
                    reader.close();

                    parseJSON(line);
                } catch (Exception e) {
                    Log.v("will", e.toString());
                }
            }
        }.start();
    }

    private void parseJSON(String json) {
        foodNo = new LinkedList<>();
        try {
            JSONArray root = new JSONArray(json);
            for (int i=0; i < root.length(); i++) {
                JSONArray sub = root.getJSONArray(i);
                JSONObject noObj = sub.getJSONObject(0);
                String StringNo = noObj.getString("許可證字號");
                Log.v("will", StringNo);

                foodNo.add(StringNo);
            }
            handler.sendEmptyMessage(0);
        } catch (Exception e) {
            Log.v("will", e.toString());
        }

    }

    public void change(View v) {
        gridView.setColumnWidth(3);
    }

    private class MyAdapter extends BaseAdapter {
        private Context context;
        MyAdapter(Context context) { this.context = context; }

        @Override
        public int getCount() {
            return imgs.length;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ImageView img;

            if (view == null) {
                img = new ImageView(context);
                img.setLayoutParams(new GridView.LayoutParams(185, 185));
                img.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            } else {
                img = (ImageView) view;
            }
            img.setImageResource(imgs[i]);

            return img;
        }
    }

    private class MyAdapter2 extends BaseAdapter {
        private Context context;
        MyAdapter2(Context context) { this.context = context; }

        @Override
        public int getCount() {
            return 30;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if (view == null) {
                view = LayoutInflater.from(context).inflate(R.layout.itemlist,null);
            }
            ImageView img = (ImageView) view.findViewById(R.id.item_img);
            TextView title = (TextView) view.findViewById(R.id.item_title);

            if (i == 7) {
                img.setImageResource(R.drawable.b0);
            }

            img.setImageResource(imgs[(int)(Math.random()*10)]);
            title.setText(foodNo.get(i));

            if (i % 2 == 0) {
                view.setBackgroundColor(Color.YELLOW);
            } else {
                view.setBackgroundColor(Color.WHITE);
            }
            return view;
        }
    }

    private class UIHandler extends android.os.Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            initGridView();
        }
    }
}

