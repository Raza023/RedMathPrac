package com.practice.session2.News;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
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

    public NewsService(NewsRepository newsRepository) {
        this.newsRepository = newsRepository;
//        this.createTestData();
    }

//    public void createTestData()
//    {
//        for(int i=1;i<=3;i++)
//        {
//            News news = new News();
//            news.setId(Long.valueOf(i));
//            news.setTitle(String.valueOf(i));
//            news.setDetails(String.valueOf(i));
//            news.setTags(String.valueOf(i));
//            news.setReportedTime(LocalDateTime.now());
//            newsRepository.save(news);
//        }
//    }

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

    public List<News> findByTitleLike(String title) {
        logger.info("Our operator is: "+sqlLike);
        logger.info("Title: "+title.replaceAll("[\r\n]",""));    //log forgery.
        List<News> newsList = newsRepository.findByTitleLike(sqlLike + title + sqlLike);
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
