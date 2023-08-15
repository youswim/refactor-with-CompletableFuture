package com.example.refactorwithcompletablefuture.future.repository;

import com.example.refactorwithcompletablefuture.common.repository.UserEntity;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Optional;

@Slf4j
public class UserFutureRepository {

    private final Map<String, UserEntity> userMap;

    public UserFutureRepository() {
        var user = new UserEntity("1234", "taewoo", 32, "image#1000");
        this.userMap = Map.of("1234", user);
    }

    @SneakyThrows
    public Optional<UserEntity> findById(String userId) {
        log.info("userRepository.findById: {}", userId);
        Thread.sleep(1000);
        UserEntity user = userMap.get(userId);
        return Optional.ofNullable(user);
    }

}
