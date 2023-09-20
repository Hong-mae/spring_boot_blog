package com.moong_bee.blog.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.moong_bee.blog.domain.Article;

public interface BlogRepository extends JpaRepository<Article, Long>{
}
