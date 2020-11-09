package com.example.ma;

import android.content.ContentValues;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class RequestHttpURLConnection {
    public String request(String _url, ContentValues _params){

        // HttpURLConnection 참조 변수.
        HttpURLConnection urlConn = null;


        try{
            URL url = new URL(_url);
            urlConn = (HttpURLConnection) url.openConnection();


            urlConn.setRequestMethod("GET"); // URL 요청에 대한 메소드 설정 : GET.
            urlConn.setReadTimeout(10000);
            urlConn.setConnectTimeout(15000);
            urlConn.setDoInput(true);
            urlConn.setRequestProperty("Accept", "application/json");


            // 실패 시 null을 리턴하고 메서드를 종료.
            System.out.println("연결 결과 : " + urlConn.getResponseCode());
            if (urlConn.getResponseCode() != HttpURLConnection.HTTP_OK)
                return null;


            // 요청한 URL의 출력물을 BufferedReader로 받는다.
            BufferedReader reader = new BufferedReader(new InputStreamReader(urlConn.getInputStream(), "UTF-8"));

            // 출력물의 라인과 그 합에 대한 변수.
            String line;
            String page = "";

            // 라인을 받아와 합친다.
            while ((line = reader.readLine()) != null){
                page += line;
            }

            return page;

        } catch (MalformedURLException e) { // for URL.
            e.printStackTrace();
        } catch (IOException e) { // for openConnection().
            e.printStackTrace();
        } finally {
            if (urlConn != null)
                urlConn.disconnect();
        }

        return null;

    }
}
