package com.practice.session2.News;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NewsRepository extends JpaRepository<News,Long> {

    //    @Query("SELECT n FROM News n WHERE n.title LIKE %:s%")
    List<News> findByTitleLike(String s);


    Page<News> findByOrderByIdDesc(Pageable pageable);

    Page<News> findByTitleLikeOrderByIdDesc(Pageable pageable, String title);
}