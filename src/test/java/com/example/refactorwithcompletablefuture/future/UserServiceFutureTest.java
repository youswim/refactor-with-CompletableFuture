package com.example.refactorwithcompletablefuture.future;

import com.example.refactorwithcompletablefuture.common.Image;
import com.example.refactorwithcompletablefuture.common.User;
import com.example.refactorwithcompletablefuture.future.repository.ArticleFutureRepository;
import com.example.refactorwithcompletablefuture.future.repository.FollowFutureRepository;
import com.example.refactorwithcompletablefuture.future.repository.ImageFutureRepository;
import com.example.refactorwithcompletablefuture.future.repository.UserFutureRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

public class UserServiceFutureTest {

    UserFutureService userFutureService;
    ArticleFutureRepository articleRepository;
    FollowFutureRepository followRepository;
    ImageFutureRepository imageRepository;
    UserFutureRepository userRepository;

    @BeforeEach
    void setup() {
        articleRepository = new ArticleFutureRepository();
        followRepository = new FollowFutureRepository();
        imageRepository = new ImageFutureRepository();
        userRepository = new UserFutureRepository();

        userFutureService = new UserFutureService(
                userRepository, articleRepository, imageRepository, followRepository
        );
    }

    @Test
    @DisplayName("존재하지 않는 user id 조회. 테스트 코드 실행이 금방 끝남")
    void getUserEmptyIfInvalidUserIdIsGiven() {
        // given
        String userId = "invalid_user_id";

        // when
        Optional<User> user = userFutureService.getUserById(userId);

        // then
        Assertions.assertTrue(user.isEmpty());
    }

    @Test
    void testGetUser() {
        // given
        String userId = "1234";

        // when
        Optional<User> optionalUser = userFutureService.getUserById(userId);

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
