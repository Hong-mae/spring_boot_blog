package com.moong_bee.blog.dto;

import java.time.LocalDateTime;

import com.moong_bee.blog.domain.Article;

import lombok.Getter;

@Getter
public class ArticleResponse {
    private final String title;
    private final String content;
    private final LocalDateTime updatedAt;

    public ArticleResponse(Article article) {
        this.title = article.getTitle();
        this.content = article.getContent();
        this.updatedAt = article.getUpdatedAt();
    }
}
