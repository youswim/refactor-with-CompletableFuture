package com.example.refactorwithcompletablefuture.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Article {
    private final String id;
    private final String title;
    private final String content;
    private final String userId;
}
