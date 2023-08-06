package common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Image {

    private final String id;
    private final String name;
    private final String url;
}
