package com.example.refactorwithcompletablefuture.future.repository;

import com.example.refactorwithcompletablefuture.common.repository.ArticleEntity;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class ArticleFutureRepository {

    private static List<ArticleEntity> articleEntities;

    public ArticleFutureRepository() {
        articleEntities = List.of(
                new ArticleEntity("1", "subject1", "content1", "1234"),
                new ArticleEntity("2", "소식2", "내용2", "1234"),
                new ArticleEntity("3", "소식3", "내용3", "10000")
        );
    }

    @SneakyThrows
    public List<ArticleEntity> findAllByUserId(String userId) {
        log.info("ArticleRepository.findAllByUserId: {}", userId);
        Thread.sleep(1000);
        return articleEntities.stream()
                .filter(articleEntity -> articleEntity.getUserId().equals(userId))
                .collect(Collectors.toList());
    }
}
