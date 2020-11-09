package com.example.ma;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Place extends AppCompatActivity {

    int placeid;
    String uuid;

    TextView textView;
    TextView textView2;
    TextView textView3;
    TextView textView4;
    ArrayList<Places> mItems = new ArrayList<Places>();
    ArrayList<Preference> mPrefer = new ArrayList<Preference>();
    Button like_btn;
    Button dislike_btn;
    ImageButton btn;
    String address;
    String url;
    NetworkTask networkTask;
    NetworkTaskPrefer networkTaskPrefer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place);

        textView = findViewById(R.id.categoryName);
        textView2 = findViewById(R.id.like_count);
        textView3 = findViewById(R.id.recomReason);
        textView4 = findViewById(R.id.phoneCall);

        like_btn = findViewById(R.id.like_btn);
        like_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public  void  onClick(View v)
            {
                url = "http://pudingles1114.iptime.org:23000/places/like/"+ placeid +"/"+ mItems.get(0).get_like() + "/" + mItems.get(0).get_dislike();
                networkTask = new NetworkTask(url, null);
                networkTask.execute();

                url = "http://pudingles1114.iptime.org:23000/prefer/update/"+ uuid +"/"+ placeid +"/"+ 1 + "/" + 0;
                networkTask = new NetworkTask(url, null);
                networkTask.execute();
                like_btn.setEnabled(false);
                dislike_btn.setEnabled(false);
                callNetworkTask();
            }
        });
        dislike_btn = findViewById(R.id.dislike_btn);
        dislike_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public  void  onClick(View v)
            {
                url = "http://pudingles1114.iptime.org:23000/places/dislike/"+ placeid+"/"+ mItems.get(0).get_like() + "/" + mItems.get(0).get_dislike();
                networkTask = new NetworkTask(url, null);
                networkTask.execute();

                url = "http://pudingles1114.iptime.org:23000/prefer/update/"+ uuid +"/"+ placeid +"/"+ 0 + "/" + 1;
                networkTask = new NetworkTask(url, null);
                networkTask.execute();
                like_btn.setEnabled(false);
                dislike_btn.setEnabled(false);
                callNetworkTask();
            }
        });

        btn = findViewById(R.id.imageButton);
        btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public  void  onClick(View v)
            {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?z=12&q="+address));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addCategory(Intent.CATEGORY_LAUNCHER );
                intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
                startActivity(intent);
            }
        });



        Intent intent = getIntent();
        loadPlaceInfo(intent);


    }

    public void loadPlaceInfo(Intent intent){
        if(intent != null){
            Bundle bundle = intent.getExtras();

            placeid = bundle.getInt("data");
            uuid = bundle.getString("uuid");


            url = "http://pudingles1114.iptime.org:23000/prefer/get_prefer_info"+"/" + uuid+"/"+ placeid;
            networkTaskPrefer = new NetworkTaskPrefer(url, null);
            networkTaskPrefer.execute();
            callNetworkTask();


        }
    }

    public void callNetworkTask(){
        url = "http://pudingles1114.iptime.org:23000/places/get_place/"+ placeid;
        networkTask = new NetworkTask(url, null);
        networkTask.execute();
    }


    public class NetworkTask extends AsyncTask<String, Void, String> {

        private String url;
        private ContentValues values;

        public NetworkTask(String url, ContentValues values) {

            this.url = url;
            this.values = values;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //progress bar를 보여주는 등등의 행위
        }

        @Override
        protected String doInBackground(String... params) {

            String result; // 요청 결과를 저장할 변수.
            RequestHttpURLConnection requestHttpURLConnection = new RequestHttpURLConnection();
            result = requestHttpURLConnection.request(url, values); // 해당 URL로 부터 결과물을 얻어온다.

            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            //doInBackground()로 부터 리턴된 값이 onPostExecute()의 매개변수로 넘어오므로 s를 출력한다.
            loadUI(s);
        }
    }

    public void loadUI(String s){
        receiveArray(s);

        if(mItems.isEmpty() != true) {
            textView.setText(mItems.get(0).get_placename());
            textView2.setText("좋아요 : " + mItems.get(0).get_like() + " 싫어요 : " + mItems.get(0).get_dislike());
            textView3.setText("선호 비율 :" + mItems.get(0).get_recomrate()+"%");
            textView4.setText(mItems.get(0).get_phoneNum());
            address = mItems.get(0).get_address();
        }
        if(mPrefer.isEmpty() != true){
            if(mPrefer.get(0).get_liked()!=false || mPrefer.get(0).get_disliked()!=false){
                like_btn.setEnabled(false);
                dislike_btn.setEnabled(false);
            }
        }
    }

    private void receiveArray(String dataObject){
        mItems.clear();
        try {
            // String 으로 들어온 값 JSONObject 로 1차 파싱
            JSONObject wrapObject = new JSONObject(dataObject);
            JSONObject jsonObject = new JSONObject(wrapObject.getString("places"));

            mItems.add(new Places(jsonObject.getString("placeid"),
                    jsonObject.getString("placename"),jsonObject.getString("category"),
                    jsonObject.getString("like"),jsonObject.getString("dislike"),
                    jsonObject.getString("recomrate"),jsonObject.getString("address"),
                    jsonObject.getString("phoneNum")));


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void receivePreference(String dataObject){
        mPrefer.clear();
        try {
            // String 으로 들어온 값 JSONObject 로 1차 파싱
            JSONObject wrapObject = new JSONObject(dataObject);
            JSONObject jsonObject = new JSONObject(wrapObject.getString("prefer"));

            mPrefer.add(new Preference(jsonObject.getString("userid"),jsonObject.getString("placeid"),
                    jsonObject.getString("liked"),jsonObject.getString("disliked"),
                    jsonObject.getString("sex"),jsonObject.getString("age")));


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public class NetworkTaskPrefer extends AsyncTask<String, Void, String> {

        private String url;
        private ContentValues values;

        public NetworkTaskPrefer(String url, ContentValues values) {

            this.url = url;
            this.values = values;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //progress bar를 보여주는 등등의 행위
        }

        @Override
        protected String doInBackground(String... params) {

            String result; // 요청 결과를 저장할 변수.
            RequestHttpURLConnection requestHttpURLConnection = new RequestHttpURLConnection();
            result = requestHttpURLConnection.request(url, values); // 해당 URL로 부터 결과물을 얻어온다.

            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            //doInBackground()로 부터 리턴된 값이 onPostExecute()의 매개변수로 넘어오므로 s를 출력한다.
            receivePreference(s);
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event){

        if(keyCode == KeyEvent.KEYCODE_BACK){
            Intent intent = new Intent();
            setResult(99, intent);
            finish();

            return true;
        }

        return false;
    }

}
