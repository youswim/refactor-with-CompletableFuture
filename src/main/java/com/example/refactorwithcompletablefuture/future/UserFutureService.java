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
                .thenCompose(this::getUser); // 이걸로 왜 바꾼건지..?
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

        var image = imageFutureRepository.findById(userEntity.getProfileImageId()).get()
                .map(imageEntity -> new Image(imageEntity.getId(), imageEntity.getName(), imageEntity.getUrl()));

        var articles = articleFutureRepository.findAllByUserId(userEntity.getId()).get()
                .stream().map(articleEntity ->
                        new Article(articleEntity.getId(), articleEntity.getTitle(), articleEntity.getContent(), articleEntity.getUserId()))
                .collect(Collectors.toList());

        var follorCount = followFutureRepository.countByUserId(userEntity.getId()).get();


        return CompletableFuture.completedFuture(
                Optional.of(
                        new User(
                                userEntity.getId(),
                                userEntity.getName(),
                                userEntity.getAge(),
                                image,
                                articles,
                                follorCount
                        )
                )
        );
    }

}
