package yourssu.yourssu_onboarding.DTO;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ArticleReturnDTO {
    private Long articleId;
    private String email;
    private String title;
    private String content;
}
