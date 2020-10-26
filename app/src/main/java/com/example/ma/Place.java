package com.example.ma;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Place extends AppCompatActivity {
    public static final int RES = 101;
    public static final int FUN = 102;
    public static final int PUB = 103;
    public static final int ETC = 104;

    TextView textView;
    TextView textView2;
    TextView textView3;
    TextView textView4;
    ImageButton btn;
    String address;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place);

        textView4 = findViewById(R.id.textLocation);

        textView = findViewById(R.id.placeName);
        textView2 = findViewById(R.id.placeCall);
        textView3 = findViewById(R.id.recomReason);

        btn = findViewById(R.id.imageButton);
        btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public  void  onClick(View v)
            {
                //LatLng sangjucu = new LatLng(36.382954, 128.151578);
                //Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:"+sangjucu.latitude+","
                //        +sangjucu.longitude+"?z=14&q=36.382954, 128.151578(씨유)"));
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

            int category = bundle.getInt("data");
            int num = bundle.getInt("name");
            switch (category)
            {
                case RES:
                    textView.setText("식당" + num);
                    textView2.setText("054-1234-567");
                    textView3.setText("맛있습니다!!");
                    address="선산김치곱창";
                    break;
                case FUN:
                    textView.setText("꿀잼장소" + num);
                    textView2.setText("053-1234-567");
                    textView3.setText("너무 놀기 좋습니다!!");
                    address="상주월드컵볼링장";
                    break;
                case PUB:
                    textView.setText("술집" + num);
                    textView2.setText("052-1234-567");
                    textView3.setText("맥주가 굉장히 시원합니다!!");
                    address="삼백호프";
                    break;
                case ETC:
                    textView.setText("기타" + num);
                    textView2.setText("051-1234-567");
                    textView3.setText("데이트 코스로 추천합니다!!");
                    address="상주공간";
                    break;


            }
        }
    }

}
