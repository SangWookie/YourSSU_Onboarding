package yourssu.yourssu_onboarding.DTO;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentReturnDTO {
    private Long commentId;
    private String email;
    private String content;
}
