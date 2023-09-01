package com.practice.session2.core;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/news")
public class NewsController {

    final NewsService newsService;

    public NewsController(NewsService newsService) {
        this.newsService = newsService;
    }

    @GetMapping()   //to get all data at once.
    public ResponseEntity<List<News>> findAll()
    {
        return  ResponseEntity.ok(newsService.findAll());
    }

    @GetMapping("/{id}")   //to get one record by id.
    public ResponseEntity<Optional<News>> findById(@PathVariable("id") long id)
    {
        Optional<News> news = newsService.findById(id);
        if (news == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(news);
    }

    @GetMapping("/search") // Get news records by title
    public ResponseEntity<List<News>> findByTitleLike(@RequestParam(name = "title") String title) {
        List<News> newsList = newsService.findByTitleLike(title);

//        if (newsList.isEmpty()) {   //no need to do this.
//            return ResponseEntity.notFound().build();
//        }

        return ResponseEntity.ok(newsList);
    }

    @PostMapping       //insert
    public ResponseEntity<News> create(@RequestBody News news) {
        News created = newsService.create(news);
        if (created != null) {
            return ResponseEntity.ok(created);
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

    // UPDATE - Using PUT method
    @PutMapping("/{id}")   // Update news by ID
    public ResponseEntity<News> update(@PathVariable("id") long id, @RequestBody News updatedNews) {
        News updated = newsService.update(updatedNews,id);   //to save on db as well

        if (updated != null) {
            return ResponseEntity.ok(updated);
        }
        return ResponseEntity.notFound().build();
    }

    // DELETE - Using DELETE method
    @DeleteMapping("/{id}")   // Delete news by ID
    public ResponseEntity<Void> delete(@PathVariable("id") long id) {
        boolean deleted = newsService.delete(id);
        if(deleted)
        {
            return ResponseEntity.ok().build();
        }
        else
        {
            return ResponseEntity.noContent().build();
        }
    }
}
