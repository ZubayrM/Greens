package object.dto.response;

import lombok.*;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListPostResponseDto<T> {

    private Integer count;

    @Singular
    private List<T> posts;

}
