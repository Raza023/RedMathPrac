package com.github.workshop.news;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.workshop.basic.ApiResponse;

@RestController
@RequestMapping("/api/v1/news")
public class NewsController {

    @Autowired
    private NewsService service;

    @GetMapping
    public ResponseEntity<ApiResponse<List<News>>> findAll(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "100") int size,
            @RequestParam(name = "title", defaultValue = "") String title) {
        List<News> news = service.findAll(page, size, title);
        if (news.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(ApiResponse.of(news));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<News>> findById(@PathVariable("id") Long id) {
        News news = service.findById(id);
        if (news == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(ApiResponse.of(news));
    }

    @PreAuthorize("hasAuthority('REPORTER')")
    @PostMapping
    public ResponseEntity<ApiResponse<News>> create(@RequestBody News news) {
        News created = service.create(news);
        if (created != null) {
            return ResponseEntity.ok(ApiResponse.of(created));
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

    @PreAuthorize("hasAuthority('REPORTER')")
    @PostMapping("/{id}")
    public ResponseEntity<ApiResponse<News>> update(@PathVariable("id") Long id, @RequestBody News news,
            Authentication auth) {
        News saved = service.update(id, news, auth);
        if (saved == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(ApiResponse.of(saved));
    }

    @PreAuthorize("hasAuthority('EDITOR')")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        service.delete(id);
    }
}