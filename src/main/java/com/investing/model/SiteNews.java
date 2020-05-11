package com.investing.model;

public class SiteNews {
    private String id;
    private String publishedAt;
    private String title;

    public SiteNews(String id, String publishedAt, String title) {
        this.id = id;
        this.publishedAt = publishedAt;
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
