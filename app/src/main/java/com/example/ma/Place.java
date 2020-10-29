package com.example.ma;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Place extends AppCompatActivity {
    public static final int RES = 101;
    public static final int FUN = 102;
    public static final int PUB = 103;
    public static final int ETC = 104;

    TextView textView;
    TextView textView2;
    TextView textView3;
    TextView textView4;
    ArrayList<Places> mItems = new ArrayList<Places>();
    ImageButton btn;
    String address;
    String url;
    NetworkTask networkTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place);

        textView = findViewById(R.id.categoryName);
        textView2 = findViewById(R.id.like_count);
        textView3 = findViewById(R.id.recomReason);
        textView4 = findViewById(R.id.phoneCall);


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

            int id = bundle.getInt("data");
            int num = bundle.getInt("name");

            url = "http://pudingles1114.iptime.org:23000/places/get_place/"+ Integer.toString(id);
            networkTask = new NetworkTask(url, null);
            networkTask.execute();

        }
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
            receiveArray(s);

            if(mItems.isEmpty() != true) {
                textView.setText(mItems.get(0).get_placename());
                textView2.setText("좋아요 : " + mItems.get(0).get_like() + " 싫어요 : " + mItems.get(0).get_dislike());
                textView3.setText("선호 비율 :" + mItems.get(0).get_recomrate()+"%");
                textView4.setText(mItems.get(0).get_phoneNum());
                address = mItems.get(0).get_address();
            }
        }
    }

    private void receiveArray(String dataObject){
        mItems.clear();
        try {
            // String 으로 들어온 값 JSONObject 로 1차 파싱
            JSONObject wrapObject = new JSONObject(dataObject);
            JSONObject jsonObject = new JSONObject(wrapObject.getString("places"));

            mItems.add(new Places(jsonObject.getString("id"),jsonObject.getString("placeid"),
                    jsonObject.getString("placename"),jsonObject.getString("category"),
                    jsonObject.getString("like"),jsonObject.getString("dislike"),
                    jsonObject.getString("recomrate"),jsonObject.getString("address"),
                    jsonObject.getString("phoneNum")));


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event){

        if(keyCode == KeyEvent.KEYCODE_BACK){
            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            finish();

            return true;
        }

        return false;
    }

}
