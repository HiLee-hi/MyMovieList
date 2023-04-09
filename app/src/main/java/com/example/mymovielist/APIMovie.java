package com.example.mymovielist;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class APIMovie extends Thread {
    static String key = "06b4de74d3e2d8c75a3eda682561e5dd";
    static String apiURL = "https://www.kobis.or.kr/kobisopenapi/webservice/rest/boxoffice/searchDailyBoxOfficeList.json";

    public static String today;
    APIMovie(String today) {
        this.today = today;
    }
    public void run() {
        main();
    }
    public static void main() {
        String url = apiURL + "?key=" + key + "&targetDt=" + today;
        String responseBody = get(url);
        parseData(responseBody);
    }
    private static String get(String apiURL) {
        String responseBody = null;
        try {
            URL url = new URL(apiURL);
            InputStream in = url.openStream();
            responseBody = readBody(in);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return responseBody;
    }
    private static String readBody(InputStream body) {
        InputStreamReader streamReader = new InputStreamReader(body);
        try(BufferedReader lineReader = new BufferedReader(streamReader)) {
            StringBuilder responseBody = new StringBuilder();

            String line;
            while((line = lineReader.readLine()) != null) {
                responseBody.append(line);
            }
            return responseBody.toString();
        } catch (IOException e) {
            throw new RuntimeException("API response read fail", e);
        }
    }
    private static void parseData(String responseBody) {
        String rank, movieNm, openDt;
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(responseBody.toString());
            JSONObject obj = (JSONObject) jsonObject.get("boxOfficeResult");
            JSONArray jsonArray = obj.getJSONArray("dailyBoxOfficeList");

            for(int i = 0; i < jsonArray.length(); i++) {
                JSONObject item = jsonArray.getJSONObject(i);
                rank = item.getString("rank");
                movieNm = item.getString("movieNm");
                openDt = item.getString("openDt");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
