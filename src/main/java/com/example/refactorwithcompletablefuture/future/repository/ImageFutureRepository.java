package com.example.refactorwithcompletablefuture.future.repository;

import com.example.refactorwithcompletablefuture.common.repository.ImageEntity;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Optional;

@Slf4j
public class ImageFutureRepository {

    private final Map<String, ImageEntity> imageMap;

    public ImageFutureRepository() {
        imageMap = Map.of(
                "image#1000", new ImageEntity("image#1000", "profileImage", "https://www.naver.com")
        );
    }

    @SneakyThrows
    public Optional<ImageEntity> findById(String id) {
        log.info("ImageRepository.findById: {}", id);
        Thread.sleep(1000);
        return Optional.ofNullable(imageMap.get(id));
    }
}
