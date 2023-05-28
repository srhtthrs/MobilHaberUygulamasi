package com.proje2dersi.mobilenews;

import retrofit2.Call;
import retrofit2.http.GET;

public interface HaberAPI {

    //https://newsapi.org/v2/everything?q=apple&sortBy=publishedAt&apiKey=3a85ed307f05415985a5d19c6ed115f4
    //https://newsapi.org/v2/everything?q=tesla&from=2023-02-19&sortBy=publishedAt&apiKey=3a85ed307f05415985a5d19c6ed115f4
    //https://newsapi.org/v2/top-headlines?country=us&category=business&apiKey=3a85ed307f05415985a5d19c6ed115f4
    //https://newsapi.org/v2/everything?domains=wsj.com&apiKey=3a85ed307f05415985a5d19c6ed115f4
    //https://newsapi.org/v2/top-headlines?sources=techcrunch&apiKey=3a85ed307f05415985a5d19c6ed115f4
    //https://newsapi.org/v2/top-headlines?language=en&apiKey=3a85ed307f05415985a5d19c6ed115f4
    //https://newsapi.org/v2/everything?q=android&language=en&sortBy=publishedAt&apiKey=3a85ed307f05415985a5d19c6ed115f4



    @GET("everything?language=tr&q=teknoloji&sortBy=publishedAt&apiKey=3a85ed307f05415985a5d19c6ed115f4")
    Call<HaberAPIModel> getAPIandroid();

    @GET("everything?language=tr&q=haberler&sortBy=publishedAt&apiKey=3a85ed307f05415985a5d19c6ed115f4")
    Call<HaberAPIModel> getAPIturkey();

    @GET("everything?language=tr&q=futbol&sortBy=publishedAt&apiKey=3a85ed307f05415985a5d19c6ed115f4")
    Call<HaberAPIModel> getAPIsport();

    String arama="magazin";
    @GET("everything?language=tr&q="+arama+"&sortBy=publishedAt&apiKey=3a85ed307f05415985a5d19c6ed115f4")
    Call<HaberAPIModel> getAPIentertainment();
}
