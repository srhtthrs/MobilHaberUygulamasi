package com.proje2dersi.mobilenews;

public class ModelEditorNews {

    public String date;
    public String newsText;
    public String newsTitle;
    public String newsType;
    public String user;
    public String downloadURL;
    public String url;
    public String name;
    public String description;

    public ModelEditorNews(String date, String newsText, String newsTitle, String newsType, String user, String downloadURL,String url, String name, String description) {
        this.date = date;
        this.newsText = newsText;
        this.newsTitle = newsTitle;
        this.newsType = newsType;
        this.user = user;
        this.downloadURL = downloadURL;
        this.url=url;
        this.name=name;
        this.description=description;
    }
}
