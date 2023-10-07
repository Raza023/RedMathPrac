package com.practice.session2.News;

import com.practice.session2.basic.ApiResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
@RestController
@RequestMapping("/api/v1/news")
public class NewsController {

    final NewsService newsService;

    public NewsController(NewsService newsService) {
        this.newsService = newsService;
    }

    @GetMapping("/all")   //to get all data at once.
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

    @GetMapping // Get news records by title
    public ResponseEntity<ApiResponse<List<News>>> findByTitleLike(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "100") int size,
            @RequestParam(name = "search", defaultValue = "") String title) {
        List<News> newsList = newsService.findByTitleLike(page, size, title);

//        if (newsList.isEmpty()) {   //no need to do this.
//            return ResponseEntity.notFound().build();
//        }

        HttpHeaders headers = new HttpHeaders();
        headers.set("Referrer-Policy", "strict-origin-when-cross-origin");

        // Your response logic here
        return ResponseEntity.ok()
                .headers(headers)
                .body(ApiResponse.of(newsList));  //we don't have to do any mapping for it.

//        return ResponseEntity.ok(Map.of("content",newsList));
    }

    @PostMapping       //insert
//    @PreAuthorize("hasAuthority('REPORTER') or hasAuthority('EDITOR')")
    public ResponseEntity<Map<String, News>> create(@RequestBody News news) {
        News created = newsService.create(news);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Referrer-Policy", "strict-origin-when-cross-origin");

        if (created != null) {
            // Your response logic here
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(Map.of("content", created));
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }


    // UPDATE - Using PUT method
    @PutMapping("/{id}")   // Update news by ID
//    @PreAuthorize("hasAuthority('REPORTER')")
    public ResponseEntity<Map<String, News>> update(@PathVariable("id") long id, @RequestBody News updatedNews) {
        News updated = newsService.update(updatedNews,id);   //to save on db as well

        HttpHeaders headers = new HttpHeaders();
        headers.set("Referrer-Policy", "strict-origin-when-cross-origin");

        if (updated != null) {
            // Your response logic here
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(Map.of("content", updated));
        }
        return ResponseEntity.notFound().build();
    }


    // DELETE - Using DELETE method
    @DeleteMapping("/{id}")   // Delete news by ID
//    @PreAuthorize("hasAuthority('REPORTER')")
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