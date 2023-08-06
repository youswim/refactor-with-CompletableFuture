package blocking.service;


import blocking.repository.ArticleRepository;
import blocking.repository.FollowRepository;
import blocking.repository.ImageRepository;
import blocking.repository.UserRepository;
import common.Article;
import common.Image;
import common.User;
import lombok.RequiredArgsConstructor;

import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class UserBlockingService {

    private final UserRepository userRepository;
    private final ArticleRepository articleRepository;
    private final ImageRepository imageRepository;
    private final FollowRepository followRepository;

    public Optional<User> getUserById(String id) {
        return userRepository.findById(id)
                .map(user -> {
                    var image = imageRepository.findById(user.getProfileImageId())
                            .map(imageEntity -> new Image(imageEntity.getId(), imageEntity.getName(), imageEntity.getUrl()));

                    var articles = articleRepository.findAllByUserId(user.getId())
                            .stream().map(articleEntity ->
                                    new Article(articleEntity.getId(), articleEntity.getTitle(), articleEntity.getContent(), articleEntity.getUserId()))
                            .collect(Collectors.toList());

                    var follorCount = followRepository.countByUserId(user.getId());


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
