package com.example.ma;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;

public class Category extends AppCompatActivity {
    public static final int RES = 101;
    public static final int FUN = 102;
    public static final int PUB = 103;
    public static final int ETC = 104;

    TextView textView;
    LinkedList<Button> buttons = new LinkedList<Button>();
    ArrayList<Places> mItems = new ArrayList<Places>();
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView recyclerView;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        recyclerView = findViewById(R.id.recyclerView);
        textView = findViewById(R.id.categoryName);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new MyAdapter(mItems);
        recyclerView.setAdapter(adapter);

        Intent intent = getIntent();
        selectCategory(intent);
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

    public void selectCategory(Intent intent){
        if(intent != null){
            Bundle bundle = intent.getExtras();
            String url;
            NetworkTask networkTask;
            int category = bundle.getInt("data");


            switch (category)
            {
                case RES:
                    textView.setText("식당");

                    url = "http://pudingles1114.iptime.org:23000/places/get_restau_info";
                    networkTask = new NetworkTask(url, null);
                    networkTask.execute();

                    for(int i=0; i< mItems.size(); i++) {
                        buttons.add(new Button(this));
                        buttons.get(i).setText(mItems.get(i).get_placename());
                    }
                    break;
                case FUN:
                    textView.setText("꿀잼장소");

                    url = "http://pudingles1114.iptime.org:23000/places/get_fun_info";
                    networkTask = new NetworkTask(url, null);
                    networkTask.execute();

                    for(int i=0; i< mItems.size(); i++) {
                        buttons.add(new Button(this));
                        buttons.get(i).setText(mItems.get(i).get_placename());
                    }
                    break;
                case PUB:
                    textView.setText("술집");

                    url = "http://pudingles1114.iptime.org:23000/places/get_pub_info";
                    networkTask = new NetworkTask(url, null);
                    networkTask.execute();

                    for(int i=0; i< mItems.size(); i++) {
                        buttons.add(new Button(this));
                        buttons.get(i).setText(mItems.get(i).get_placename());
                    }
                    break;
                case ETC:
                    textView.setText("기타");

                    url = "http://pudingles1114.iptime.org:23000/places/get_etc_info";
                    networkTask = new NetworkTask(url, null);
                    networkTask.execute();

                    for(int i=0; i< mItems.size(); i++) {
                        buttons.add(new Button(this));
                        buttons.get(i).setText(mItems.get(i).get_placename());
                    }
                    break;


            }
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
        }
    }

    private void receiveArray(String dataObject){
        mItems.clear();
        try {
            // String 으로 들어온 값 JSONObject 로 1차 파싱
            JSONObject wrapObject = new JSONObject(dataObject);

            // JSONObject 의 키 "list" 의 값들을 JSONArray 형태로 변환
            JSONArray jsonArray = new JSONArray(wrapObject.getString("places"));
            for(int i = 0; i < jsonArray.length(); i++){
                // Array 에서 하나의 JSONObject 를 추출
                JSONObject dataJsonObject = jsonArray.getJSONObject(i);
                // 추출한 Object 에서 필요한 데이터를 표시할 방법을 정해서 화면에 표시
                // 필자는 RecyclerView 로 데이터를 표시 함
                mItems.add(new Places(dataJsonObject.getString("id"),dataJsonObject.getString("placeid"),
                        dataJsonObject.getString("placename"),dataJsonObject.getString("category"),
                        dataJsonObject.getString("like"),dataJsonObject.getString("dislike"),
                        dataJsonObject.getString("recomrate"),dataJsonObject.getString("address"),
                        dataJsonObject.getString("phoneNum")));
            }
            // Recycler Adapter 에서 데이터 변경 사항을 체크하라는 함수 호출
            adapter.notifyDataSetChanged();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ItemViewHolder> {
        private ArrayList<Places> mList;
        private int position;
        public MyAdapter(ArrayList<Places> list){
            this.mList = list;
        }

        public class ItemViewHolder extends RecyclerView.ViewHolder {
            // each data item is just a string in this case
            public Button button;
            public ItemViewHolder(View v) {
                super(v);
                button = v.findViewById(R.id.button);
            }
        }

        // Create new views (invoked by the layout manager)
        @NonNull
        @Override
        public MyAdapter.ItemViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {
            // create a new view
            Context context = parent.getContext();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View v = inflater.inflate(R.layout.recyclerview_item, parent, false);
            ItemViewHolder vh = new ItemViewHolder(v);
            return vh;
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(ItemViewHolder holder, int position) {
            // - get element from your dataset at this position
            // - replace the contents of the view with that element
            final int POS = position;
            holder.button.setText(mList.get(POS).get_placename());
            holder.button.setOnClickListener(new View.OnClickListener(){
                @Override
                public  void  onClick(View v)
                {
                    Intent intent = new Intent(getApplicationContext(), Place.class);
                    intent.putExtra("data", mList.get(POS).get_id());
                    intent.putExtra("name", 1);
                    startActivityForResult(intent, mList.get(POS).get_id());

                }
            });

        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return (null != mList ? mList.size() : 0 );
        }

        public ArrayList<Places> getListData(){
            return mList;
        }

        public void setListData(ArrayList<Places> listData){
            this.mList = listData;
        }
    }
}
