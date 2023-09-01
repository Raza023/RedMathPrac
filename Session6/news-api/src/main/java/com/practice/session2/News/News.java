package com.practice.session2.News;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class News {


//    @GeneratedValue(strategy = GenerationType.IDENTITY)    //using index
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "news_sequence")
    @SequenceGenerator(name = "news_sequence", sequenceName = "news_sequence", allocationSize = 1)
    @Id
    private long id;
    private String title;
    private String details;
    private String tags;
    private LocalDateTime reportedTime;

    public News(){
        this.id = 0;
        this.title = "s";
        this.details = "s1";
        this.tags = "s2";
        this.reportedTime = LocalDateTime.now();
    }

    public News(long l, String s, String s1, String s2, LocalDateTime now) {
        this.id = l;
        this.title = s;
        this.details = s1;
        this.tags = s2;
        this.reportedTime = now;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public LocalDateTime getReportedTime() {
        return reportedTime;
    }

    public void setReportedTime(LocalDateTime reportedTime) {
        this.reportedTime = reportedTime;
    }
}
