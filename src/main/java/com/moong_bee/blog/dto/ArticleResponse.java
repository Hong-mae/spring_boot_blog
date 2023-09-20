package com.moong_bee.blog.dto;

import java.time.LocalDateTime;

import com.moong_bee.blog.domain.Article;

import lombok.Getter;

@Getter
public class ArticleResponse {
    private final String title;
    private final String content;
    private final LocalDateTime date;

    public ArticleResponse(Article article) {
        this.title = article.getTitle();
        this.content = article.getContent();
        this.date = article.getDate();
    }
}
