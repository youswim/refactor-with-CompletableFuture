package common.repository;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserEntity {
    // DB의 테이블과 같은 역할. profileImageId처럼 외래키를 가지고 있다고 가정

    private final String id;
    private final String name;
    private final Integer age;
    private final String profileImageId;

}
