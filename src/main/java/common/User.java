package common;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.Optional;

@Getter
@AllArgsConstructor
public class User {
    // 도메인 객체의 역할. String이 아니라 필드로 클래스 인스턴스를 가진다.

    private final String id;
    private final String name;
    private final Integer age;
    private final Optional<Image> profileImage;
    private final List<Article> articles;
    private final Long followCount;
}
