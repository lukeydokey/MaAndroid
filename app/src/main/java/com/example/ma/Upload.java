package com.example.ma;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Upload extends AppCompatActivity {

    String android_id;
    Button categoryBtn;
    Button uploadBtn;
    Button cancelBtn;

    EditText nameEdit;
    EditText addrEdit;
    EditText pNumEdit;

    NetworkTask networkTask;
    String url;
    String category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        categoryBtn = findViewById(R.id.category_Btn);
        uploadBtn = findViewById(R.id.uploadBtn);
        cancelBtn = findViewById(R.id.cancelBtn);

        nameEdit = findViewById(R.id.nameEdit);
        addrEdit = findViewById(R.id.addrEdit);
        pNumEdit = findViewById(R.id.pNumEdit);

        Intent intent = getIntent();
        setting(intent);
    }

    public void setting(Intent intent){
        if(intent != null) {
            Bundle bundle = intent.getExtras();
            android_id = bundle.getString("uuid");
        }

        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (nameEdit.getText().toString().length()<=0){
                    Toast.makeText(getApplicationContext(), "장소명을 입력해주세요",
                            Toast.LENGTH_LONG).show();
                } else if (category == null) {
                    Toast.makeText(getApplicationContext(), "카테고리를 선택해주세요",
                            Toast.LENGTH_LONG).show();
                } else if (addrEdit.getText().toString().length()<=0){
                    Toast.makeText(getApplicationContext(), "주소를 입력해주세요",
                            Toast.LENGTH_LONG).show();
                } else if (pNumEdit.getText().toString().length()<=0){
                    Toast.makeText(getApplicationContext(), "전화 번호를 입력해주세요",
                            Toast.LENGTH_LONG).show();
                } else {
                    JSONObject jObj = new JSONObject();
                    try {
                        jObj.put("placename", nameEdit.getText());
                        jObj.put("category", category);
                        jObj.put("like", "0");
                        jObj.put("dislike", "0");
                        jObj.put("recomrate", "0");
                        jObj.put("address", addrEdit.getText());
                        jObj.put("phoneNum", pNumEdit.getText());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    url = "http://pudingles1114.iptime.org:23000/places/upload";
                    networkTask = new NetworkTask(url, jObj);
                    networkTask.execute();
                }
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                setResult(RESULT_CANCELED, intent);
                finish();
            }
        });
    }

    public boolean onKeyDown(int keyCode, KeyEvent event){

        if(keyCode == KeyEvent.KEYCODE_BACK){
            Intent intent = new Intent();
            setResult(RESULT_CANCELED, intent);
            finish();

            return true;
        }

        return false;
    }

    public void showDialog(View view) {
        List<String> list = new ArrayList<>();
        list.add("식당");
        list.add("꿀잼장소");
        list.add("술집");
        list.add("기타");
        final CharSequence[] items = list.toArray(new String[list.size()]);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("카테고리");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                category = "";
                switch (i){
                    case 0: category = "res";
                        break;
                    case 1: category = "fun";
                        break;
                    case 2: category = "pub";
                        break;
                    case 3: category = "etc";
                }
                categoryBtn.setText(items[i].toString());
                Toast.makeText(getApplicationContext(), category, Toast.LENGTH_SHORT).show();
            }
        });
        builder.show();
    }



    public class NetworkTask extends AsyncTask<JSONObject, Void, String> {

        private String url;
        private JSONObject values;

        public NetworkTask(String url, JSONObject values) {

            this.url = url;
            this.values = values;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //progress bar를 보여주는 등등의 행위
        }

        @Override
        protected String doInBackground(JSONObject... params) {

            String result; // 요청 결과를 저장할 변수.
            RequestUpload requestHttpURLConnection = new RequestUpload();
            result = requestHttpURLConnection.request(url, values); // 해당 URL로 부터 결과물을 얻어온다.

            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            finish();
        }
    }
}