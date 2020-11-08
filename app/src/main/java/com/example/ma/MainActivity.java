package com.example.ma;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity  {
    public static final int REQUEST_CODE_RES = 101;
    public static final int REQUEST_CODE_FUN = 102;
    public static final int REQUEST_CODE_PUB = 103;
    public static final int REQUEST_CODE_ETC = 104;
    //TelephonyManager tm = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
    String url;
    NetworkTask networkTask;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){
                //String name = data.getStringExtra("name");
                Toast.makeText(getApplicationContext(), "홈으로 돌아왔습니다.",
                        Toast.LENGTH_LONG).show();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnClick();


    }


    public void btnClick(){
        Button btn = findViewById(R.id.rest_Button);
        final String android_id = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);

        url = "http://pudingles1114.iptime.org:23000/user/regist";
        networkTask = new NetworkTask(url, android_id);
        networkTask.execute();

        btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public  void  onClick(View v)
            {
                Intent intent = new Intent(getApplicationContext(), Category.class);
                intent.putExtra("data", REQUEST_CODE_RES);
                intent.putExtra("uuid", android_id);
                startActivityForResult(intent, REQUEST_CODE_RES);
            }
        });

        Button btn2 = findViewById(R.id.fun_Button);
        btn2.setOnClickListener(new View.OnClickListener(){
            @Override
            public  void  onClick(View v)
            {
                Intent intent = new Intent(getApplicationContext(), Category.class);
                intent.putExtra("data", REQUEST_CODE_FUN);
                intent.putExtra("uuid", android_id);
                startActivityForResult(intent, REQUEST_CODE_FUN);
            }
        });

        Button btn3 = findViewById(R.id.pub_Button);
        btn3.setOnClickListener(new View.OnClickListener(){
            @Override
            public  void  onClick(View v)
            {
                Intent intent = new Intent(getApplicationContext(), Category.class);
                intent.putExtra("data", REQUEST_CODE_PUB);
                intent.putExtra("uuid", android_id);
                startActivityForResult(intent, REQUEST_CODE_PUB);
            }
        });

        Button btn4 = findViewById(R.id.etc_Button);
        btn4.setOnClickListener(new View.OnClickListener(){
            @Override
            public  void  onClick(View v)
            {
                Intent intent = new Intent(getApplicationContext(), Category.class);
                intent.putExtra("data", REQUEST_CODE_ETC);
                intent.putExtra("uuid", android_id);
                startActivityForResult(intent, REQUEST_CODE_ETC);
            }
        });
    }

    public class NetworkTask extends AsyncTask<String, Void, String> {

        private String url;
        private String values;

        public NetworkTask(String url, String values) {

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
            RequestUUIDCreate requestHttpURLConnection = new RequestUUIDCreate();
            result = requestHttpURLConnection.request(url, values); // 해당 URL로 부터 결과물을 얻어온다.

            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Toast.makeText(getApplicationContext(), s,
                    Toast.LENGTH_LONG).show();
        }
    }

}
