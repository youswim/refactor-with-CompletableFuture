package com.example.refactorwithcompletablefuture.future;


import com.example.refactorwithcompletablefuture.future.repository.ArticleFutureRepository;
import com.example.refactorwithcompletablefuture.future.repository.FollowFutureRepository;
import com.example.refactorwithcompletablefuture.future.repository.ImageFutureRepository;
import com.example.refactorwithcompletablefuture.future.repository.UserFutureRepository;
import com.example.refactorwithcompletablefuture.common.Article;
import com.example.refactorwithcompletablefuture.common.Image;
import com.example.refactorwithcompletablefuture.common.User;
import lombok.RequiredArgsConstructor;

import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class UserBlockingService {

    private final UserFutureRepository userFutureRepository;
    private final ArticleFutureRepository articleFutureRepository;
    private final ImageFutureRepository imageFutureRepository;
    private final FollowFutureRepository followFutureRepository;

    public Optional<User> getUserById(String id) {
        return userFutureRepository.findById(id)
                .map(user -> {
                    var image = imageFutureRepository.findById(user.getProfileImageId())
                            .map(imageEntity -> new Image(imageEntity.getId(), imageEntity.getName(), imageEntity.getUrl()));

                    var articles = articleFutureRepository.findAllByUserId(user.getId())
                            .stream().map(articleEntity ->
                                    new Article(articleEntity.getId(), articleEntity.getTitle(), articleEntity.getContent(), articleEntity.getUserId()))
                            .collect(Collectors.toList());

                    var follorCount = followFutureRepository.countByUserId(user.getId());


                    return new User(
                            user.getId(),
                            user.getName(),
                            user.getAge(),
                            image,
                            articles,
                            follorCount
                    );
                });
    }
}
