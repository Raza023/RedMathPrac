package com.practice.session2.News;

//import jakarta.transaction.Transactional;
import javax.transaction.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class NewsService {
    final NewsRepository newsRepository;

    @Value("${spring.sql.like.operator: %}")
    private String sqlLike;

    Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${news.page.min:0}")
    public int pageMin = 0;

    @Value("${news.page.size.min:1}")
    public int pageSizeMin = 1;

    @Value("${news.page.size.max:100}")
    public int pageSizeMax = 100;

    public NewsService(NewsRepository newsRepository) {
        this.newsRepository = newsRepository;
    }


    public List<News> findAll()
    {
        logger.debug("All news found");
        return newsRepository.findAll();
    }

    public Optional<News> findById(long id) {
        Optional<News> news = newsRepository.findById(id);
        if(news.isPresent())
        {
            logger.debug("Found a news with id: {}",id);
            return news;
        }
        else {
            return null;
        }
    }

    public List<News> findByTitleLike(int page,int size,String title) {
        logger.info("Our operator is: "+sqlLike);
        logger.info("Title: "+title.replace("[\r\n]",""));    //log forgery.

        if (page < pageMin) {
            page = pageMin;
        }
        if (size > pageSizeMax) {
            size = pageSizeMax;
        } else if (size < pageSizeMin) {
            size = pageSizeMin;
        }

        List<News> newsList = newsRepository.findByTitleLike(sqlLike + title + sqlLike);

        Pageable pageable = PageRequest.of(page, size);
        if (title.isEmpty() || title.isBlank()) {
            newsList = newsRepository.findByOrderByIdDesc(pageable).getContent();
            return newsList;
        }
        newsList = newsRepository.findByTitleLikeOrderByIdDesc(pageable, sqlLike + title + sqlLike).getContent();

        return newsList; // If no matches are found, this will return an empty list
    }


    @Transactional
    public News create(News news) {
        Long wrapperLong = news.getId();
        if (wrapperLong != null && newsRepository.existsById(news.getId())) {
            logger.error("Can't add existing news with the id:  {}", wrapperLong);
            return null;
        }
        news.setId(System.currentTimeMillis());
        news.setReportedTime(LocalDateTime.now());
        return newsRepository.save(news);
    }

    @Transactional
    public News update(News newsToUpdate,long id) {
        // Check if the news item with the given id exists in the repository
        Optional<News> existingNewsOptional = newsRepository.findById(id);

        if (existingNewsOptional.isEmpty()) {
            logger.trace("There is nothing to update with id = {}",id);
            return null; // Return null or throw an exception to handle the case where the news item doesn't exist
        }

        // Get the existing news item from the Optional
        News existingNews = existingNewsOptional.get();

        // Update the existing news item with the new data from newsToUpdate
        existingNews.setTitle(newsToUpdate.getTitle());
        existingNews.setDetails(newsToUpdate.getDetails());
        existingNews.setTags(newsToUpdate.getTags());
        existingNews.setReportedTime(newsToUpdate.getReportedTime());

        // Save the updated news item back to the repository
        newsRepository.save(existingNews);

        return existingNews; // Return the updated news item
    }
    @Transactional
    public boolean delete(long id) {
        Optional<News> news = newsRepository.findById(id);
        if(news.isPresent())
        {
            newsRepository.deleteById(id);
            return true;
        }
        else
        {
            logger.error("No record found to delete with the id {}", id);
            return false;
        }
    }


}