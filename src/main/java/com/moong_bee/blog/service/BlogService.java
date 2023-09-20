package com.moong_bee.blog.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.moong_bee.blog.domain.Article;
import com.moong_bee.blog.dto.AddArticleRequest;
import com.moong_bee.blog.repository.BlogRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class BlogService {
    private final BlogRepository blogRepository;

    public Article save(AddArticleRequest request) {
        return blogRepository.save(request.toEntity());
    }

    public List<Article> findAll() {
        return blogRepository.findAll();
    }

    public Article findById(long id) {
        return blogRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found: " + id));
    }
}
