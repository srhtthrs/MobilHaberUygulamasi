package com.proje2dersi.mobilenews;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class HaberAPIModel {

    @SerializedName("status")
    @Expose

    public String status;

    @SerializedName("totalResults")
    @Expose

    public String totalResults;

    public String getTotalResults() {
        return totalResults;
    }

    @SerializedName("articles")
    @Expose

    public List<Articles> articles;

    public List<Articles> getArticles() {
        return articles;
    }


    public class Articles {

        @SerializedName("title")
        @Expose
        public String title;

        @SerializedName("description")
        @Expose
        public String description;

        @SerializedName("url")
        @Expose
        public String url;

        @SerializedName("urlToImage")
        @Expose
        public String urlToImage;

        @SerializedName("publishedAt")
        @Expose
        public String publishedAt;

        @SerializedName("content")
        @Expose
        public String content;

        @SerializedName("source")
        @Expose
        Source source;



        public class Source {
            @SerializedName("name")
            @Expose
            String name;

            @SerializedName("id")
            @Expose
            String id;


        }


    }
}
