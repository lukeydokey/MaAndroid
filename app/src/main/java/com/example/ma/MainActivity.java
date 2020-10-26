package com.example.ma;

import android.content.Intent;
import android.os.Bundle;
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
        btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public  void  onClick(View v)
            {
                Intent intent = new Intent(getApplicationContext(), Category.class);
                intent.putExtra("data", REQUEST_CODE_RES);
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
                startActivityForResult(intent, REQUEST_CODE_ETC);
            }
        });
    }
}
