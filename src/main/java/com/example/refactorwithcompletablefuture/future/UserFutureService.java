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
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class UserFutureService {

    private final UserFutureRepository userFutureRepository;
    private final ArticleFutureRepository articleFutureRepository;
    private final ImageFutureRepository imageFutureRepository;
    private final FollowFutureRepository followFutureRepository;

    @SneakyThrows
    public CompletableFuture<Optional<User>> getUserById(String id) {
        return userFutureRepository.findById(id)
                .thenComposeAsync(this::getUser); // 이걸로 왜 바꾼건지..?
    }

    @SneakyThrows
    private CompletableFuture<Optional<User>> getUser(Optional<UserEntity> userEntityOptional) { // 여기에 굳이 Optional을 사용할 이유가 있는가?
        if (userEntityOptional.isEmpty()) {
//            var future = new CompletableFuture<Optional<User>>();
//            future.complete(Optional.empty());
//            return future;
//            위 세줄을 아래 한줄로 표현 가능
            return CompletableFuture.completedFuture(Optional.empty());
        }
        var userEntity = userEntityOptional.get(); // 위에서 empty인 경우는 반환했으므로, get을 바로 해도 된다.

        var imageFuture = imageFutureRepository.findById(userEntity.getProfileImageId())
                .thenApplyAsync(imageEntityOptional -> {
                    return imageEntityOptional.map(imageEntity -> {
                        return new Image(imageEntity.getId(), imageEntity.getName(), imageEntity.getUrl());
                    });
                });

        var articlesFuture = articleFutureRepository.findAllByUserId(userEntity.getId())
                .thenApplyAsync(articleEntities -> {
                    return articleEntities.stream()
                            .map(articleEntity ->
                                    new Article(articleEntity.getId(), articleEntity.getTitle(), articleEntity.getContent(), articleEntity.getUserId()))
                            .collect(Collectors.toList());
                });

        var followCountFuture = followFutureRepository.countByUserId(userEntity.getId());

        return CompletableFuture.allOf(imageFuture, articlesFuture, followCountFuture)
                .thenApplyAsync(v -> {
                            try {
                                // allOf의 파라미터로 전달된 Future들이 모두 완료된 경우에만 이 로직이 실행되므로, get으로 결과값을 가져온다고 하더라도 문제되지 않는다.
                                var image = imageFuture.get();
                                var articles = articlesFuture.get();
                                var followCount = followCountFuture.get();

                                return Optional.of(
                                        new User(
                                                userEntity.getId(),
                                                userEntity.getName(),
                                                userEntity.getAge(),
                                                image,
                                                articles,
                                                followCount
                                        )
                                );
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        }
                );
    }
}
