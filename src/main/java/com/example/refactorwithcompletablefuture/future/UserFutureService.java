package com.example.refactorwithcompletablefuture.future;


import com.example.refactorwithcompletablefuture.common.repository.UserEntity;
import com.example.refactorwithcompletablefuture.future.repository.ArticleFutureRepository;
import com.example.refactorwithcompletablefuture.future.repository.FollowFutureRepository;
import com.example.refactorwithcompletablefuture.future.repository.ImageFutureRepository;
import com.example.refactorwithcompletablefuture.future.repository.UserFutureRepository;
import com.example.refactorwithcompletablefuture.common.Article;
import com.example.refactorwithcompletablefuture.common.Image;
import com.example.refactorwithcompletablefuture.common.User;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class UserFutureService {

    private final UserFutureRepository userFutureRepository;
    private final ArticleFutureRepository articleFutureRepository;
    private final ImageFutureRepository imageFutureRepository;
    private final FollowFutureRepository followFutureRepository;

    @SneakyThrows
    public Optional<User> getUserById(String id) {
        return userFutureRepository.findById(id).get()
                .map(this::getUser);
    }

    @SneakyThrows
    private User getUser(UserEntity user) {
        var image = imageFutureRepository.findById(user.getProfileImageId()).get()
                .map(imageEntity -> new Image(imageEntity.getId(), imageEntity.getName(), imageEntity.getUrl()));

        var articles = articleFutureRepository.findAllByUserId(user.getId()).get()
                .stream().map(articleEntity ->
                        new Article(articleEntity.getId(), articleEntity.getTitle(), articleEntity.getContent(), articleEntity.getUserId()))
                .collect(Collectors.toList());

        var follorCount = followFutureRepository.countByUserId(user.getId()).get();


        return new User(
                user.getId(),
                user.getName(),
                user.getAge(),
                image,
                articles,
                follorCount
        );
    }
}
