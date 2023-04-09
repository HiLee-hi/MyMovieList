package com.example.mymovielist;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {
    ArrayList<MovieData> mData = new ArrayList<>();
    Context mContext;
    Search searchItem;

    public class MovieViewHolder extends RecyclerView.ViewHolder {
        TextView textView_rank, textView_movieNm, textView_openDt;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            textView_rank = itemView.findViewById(R.id.textView_rank);
            textView_movieNm = itemView.findViewById(R.id.textView_movieNm);
            textView_openDt = itemView.findViewById(R.id.textView_openDt);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION) {
                        // 새로 작성한 코드
                        Thread thread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    String keyword = mData.get(position).getMovieNm();
                                    String movieCd = mData.get(position).getMovieCd();
                                    Log.d("movieCd:", movieCd);
                                    String prdtYear = getListSearch(movieCd);
                                    Log.d("prdtYear: ", prdtYear);

                                    String str = getNaverSearch(keyword);
//                                    String str = getNaverSearch(keyword, prdtYear);
//                                    parseData(str);
//                                    parseData(str, prdtYear);
                                    parseData(str, keyword, prdtYear);

                                    Intent intent = new Intent(mContext, MovieContentActivity.class);
                                    intent.putExtra("item", searchItem); // 클릭한 list 객체 전달
                                    mContext.startActivity(intent);
                                } catch(Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }); thread.start();
//                        Intent intent = new Intent(mContext, SearchMovieActivity.class);
//                        intent.putExtra("keyword", mData.get(position).getMovieNm()); // 클릭한 list 객체 전달
//                        mContext.startActivity(intent);
                    }
                }
            });
        }

        public String getListSearch(String movieCd) {
            String key = "06b4de74d3e2d8c75a3eda682561e5dd";
            String url = "https://www.kobis.or.kr/kobisopenapi/webservice/rest/movie/searchMovieInfo.json";
            String listURL = url + "?key=" + key + "&movieCd=" + movieCd;
            String prdtYear = "";

            Log.d("movieCd", movieCd);

            try {
                URL urlAddress = new URL(listURL);
                InputStream in = urlAddress.openStream();

                InputStreamReader streamReader = new InputStreamReader(in);
                BufferedReader reader = new BufferedReader(streamReader);
                StringBuffer buffer = new StringBuffer();
                String line = reader.readLine();

                while (line != null) {
                    buffer.append(line + "\n");
                    line = reader.readLine();
                }
                String jsonData = buffer.toString();
                JSONObject jsonObject = new JSONObject(jsonData);
                JSONObject movieInfoResult = (JSONObject) jsonObject.get("movieInfoResult");
                JSONObject movieInfo = (JSONObject) movieInfoResult.get("movieInfo");

                prdtYear = movieInfo.getString("prdtYear");
                Log.d("method prdtYear: ", movieInfo.getString("prdtYear"));
                return prdtYear;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return prdtYear;
        }

        public String getNaverSearch(String keyword) {
            // 네이버 api
            String clientId = "BRtC876XnJFwlTNFBiFK";
            String clientSecret = "B8q7RgXUFf";

            StringBuffer stringBuffer = new StringBuffer();
            String body;

            try {
                String text = URLEncoder.encode(keyword, "UTF-8");

//            String apiURL = "https://openapi.naver.com/v1/search/movie?query=" + text;
                String apiURL = "https://openapi.naver.com/v1/search/movie?query=" + text + "&display=20";

                URL url  = new URL(apiURL);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();

                con.setRequestMethod("GET");
                con.setRequestProperty("X-Naver-Client-Id", clientId);
                con.setRequestProperty("X-Naver-Client-Secret", clientSecret);

                InputStream inputStream = con.getInputStream();

                InputStreamReader streamReader = new InputStreamReader(inputStream);
                try(BufferedReader lineReader = new BufferedReader(streamReader)) {
                    StringBuilder responseBody = new StringBuilder();
                    String line;
                    while((line = lineReader.readLine()) != null) {
                        responseBody.append(line);
                    }
                    body = responseBody.toString();
                }

            } catch(Exception e) {
                return e.toString();
            }
            return body;
        }

        public void parseData(String body, String keyword, String prdtYear) {
            String title, link, image, subtitle, pubDate, director, actor, userRating;
            JSONObject jsonObject = null;

            try {
                jsonObject = new JSONObject(body);
                JSONArray jsonArray = jsonObject.getJSONArray("items");

                SimpleDateFormat year = new SimpleDateFormat("yyyy");
                Date date = year.parse(prdtYear);

                for(int i = 0; i < jsonArray.length(); i++) {

                    JSONObject item = jsonArray.getJSONObject(i);
                    title = item.getString("title").replaceAll("<(/)?([a-zA-Z]*)(\\s[a-zA-Z]*=[^>]*)?(\\s)*(/)?>", "");

                    if(item.getString("pubDate").equals(prdtYear) && title.equals(keyword)) {
                        title = item.getString("title").replaceAll("<(/)?([a-zA-Z]*)(\\s[a-zA-Z]*=[^>]*)?(\\s)*(/)?>", "");
                        link = item.getString("link");
                        image = item.getString("image");
                        subtitle = item.getString("subtitle");
                        pubDate = item.getString("pubDate");
                        director = item.getString("director").replaceAll("[|]", ", ");
                        actor = item.getString("actor").replaceAll("[|]", ", ");
                        userRating = String.valueOf(item.getDouble("userRating"));

                        searchItem = new Search(title, link, image, subtitle, pubDate, director, actor, userRating);

                        System.out.println("title: " + title);
                        System.out.println("link: " + link);
                        System.out.println("image: " + image);
                        System.out.println("subtitle: " + subtitle);
                        System.out.println("pubDate: " + pubDate);
                        System.out.println("director: " + director);
                        System.out.println("actor: " + actor);
                        System.out.println("userRating: " + userRating);
                    }

                }

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        void onBind(MovieData data) {
            textView_rank.setText(data.getRank());
            textView_movieNm.setText(data.getMovieNm());
            textView_openDt.setText(data.getOpenDt());
        }
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.movie_item, parent, false);
        MovieAdapter.MovieViewHolder viewHolder = new MovieAdapter.MovieViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        holder.onBind(mData.get(position));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    // Data 객체들을 items 에 저장
    public void setMovieList(ArrayList<MovieData> items) {
        this.mData = items;
        notifyDataSetChanged();
    }
}
