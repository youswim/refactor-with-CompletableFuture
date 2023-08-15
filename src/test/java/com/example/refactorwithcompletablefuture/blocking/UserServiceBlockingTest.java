package com.example.refactorwithcompletablefuture.blocking;

import com.example.refactorwithcompletablefuture.blocking.repository.ArticleRepository;
import com.example.refactorwithcompletablefuture.blocking.repository.FollowRepository;
import com.example.refactorwithcompletablefuture.blocking.repository.ImageRepository;
import com.example.refactorwithcompletablefuture.blocking.repository.UserRepository;
import com.example.refactorwithcompletablefuture.blocking.service.UserBlockingService;
import com.example.refactorwithcompletablefuture.common.Image;
import com.example.refactorwithcompletablefuture.common.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

public class UserServiceBlockingTest {

    UserBlockingService userBlockingService;
    ArticleRepository articleRepository;
    FollowRepository followRepository;
    ImageRepository imageRepository;
    UserRepository userRepository;

    @BeforeEach
    void setup() {
        articleRepository = new ArticleRepository();
        followRepository = new FollowRepository();
        imageRepository = new ImageRepository();
        userRepository = new UserRepository();

        userBlockingService = new UserBlockingService(
                userRepository, articleRepository, imageRepository, followRepository
        );
    }

    @Test
    @DisplayName("존재하지 않는 user id 조회. 테스트 코드 실행이 금방 끝남")
    void getUserEmptyIfInvalidUserIdIsGiven() {
        // given
        String userId = "invalid_user_id";

        // when
        Optional<User> user = userBlockingService.getUserById(userId);

        // then
        Assertions.assertTrue(user.isEmpty());
    }

    @Test
    void testGetUser() {
        // given
        String userId = "1234";

        // when
        Optional<User> optionalUser = userBlockingService.getUserById(userId);

        // then
        Assertions.assertFalse(optionalUser.isEmpty());
        User user = optionalUser.get();
        Assertions.assertEquals(user.getName(), "taewoo");
        Assertions.assertEquals(user.getAge(), 32);

        Assertions.assertFalse(user.getProfileImage().isEmpty());
        Image image = user.getProfileImage().get();
        Assertions.assertEquals(image.getId(), "image#1000");
        Assertions.assertEquals(image.getName(), "profileImage");
        Assertions.assertEquals(image.getUrl(), "https://www.naver.com");

        Assertions.assertEquals(2, user.getArticles().size());

        Assertions.assertEquals(1000, user.getFollowCount());
    }


}
