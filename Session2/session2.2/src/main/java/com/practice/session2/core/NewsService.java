package com.practice.session2.core;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NewsService {
    final NewsRepository newsRepository;

    public NewsService(NewsRepository newsRepository) {
        this.newsRepository = newsRepository;
        this.createTestData();
    }

    public void createTestData()
    {
        for(int i=1;i<=3;i++)
        {
            News news = new News();
            news.setId(Long.valueOf(i));
            news.setTitle(String.valueOf(i));
            news.setDetails(String.valueOf(i));
            news.setTags(String.valueOf(i));
            news.setReportedTime(LocalDateTime.now());
            newsRepository.save(news);
        }
    }

    public List<News> findAll()
    {
        return newsRepository.findAll();
    }
}
