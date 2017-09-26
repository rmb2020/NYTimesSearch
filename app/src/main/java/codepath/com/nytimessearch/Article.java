package codepath.com.nytimessearch;

import android.graphics.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static android.R.attr.format;

/**
 * Created by nb0 on 9/15/2017.
 */

public class Article implements Serializable{
    String webUrl;
    String headLine;
    String thumbNail;
    //need to add pub_date and new_desk
    Date pubDate;
    String newsDesk;

    int hits;

    /*public int getHits() {
        return hits;
    }

    public String getNewsDesk() {
        return newsDesk;
    }

    public Date getPubDate() {
        return pubDate;
    }*/


    public String getWebUrl() {
        return webUrl;
    }

    public String getHeadLine() {
        return headLine;
    }

    public String getThumbNail() {
        return thumbNail;
    }


    public Article(JSONObject jsonObject){
        try {
            this.webUrl = jsonObject.getString("web_url");
            this.headLine = jsonObject.getJSONObject("headline").getString("main");

            JSONArray multimedia = jsonObject.getJSONArray("multimedia");

            if (multimedia.length() > 0){
                JSONObject multimediaJson = multimedia.getJSONObject(0);
                this. thumbNail = "http://www.nytimes.com/" + multimediaJson.getString("url");
            } else {
                this.thumbNail = "";
            }

            //The pub_date, news_desk not needed for current NYTimes app. But can be used for display of category if needed.
            /*String dtString = jsonObject.getString("pub_date");

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZZZZ");
            //SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:sss'Z'");
            //DateFormat dForm = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");


            //Date convertedDate;
                    //= new Date();
            try {
                //this.pubDate = format.parse(dtString);
                this.pubDate = format.parse(dtString);
                System.out.println(pubDate); //Tue Sep 05 08:00:13 PDT 2017
            } catch (ParseException e) {
                e.printStackTrace();
            }
            //this.pubDate = (Date) jsonObject.get("pub_date");
            //this.pubDate = convertedDate;
            this.newsDesk = jsonObject.getString("new_desk");
            this.hits = jsonObject.getJSONObject("meta").getInt("hits");*/


        } catch (JSONException e){

        }

    }

    public static ArrayList<Article> fromJSONArray(JSONArray array){
        ArrayList<Article> results = new ArrayList();

        for (int x = 0; x<array.length(); x++) {
            try {
                results.add(new Article(array.getJSONObject(x)));
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return results;

    }
}
