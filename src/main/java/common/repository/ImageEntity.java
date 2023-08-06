package common.repository;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ImageEntity {

    private final String id;
    private final String name;
    private final String url;
}
