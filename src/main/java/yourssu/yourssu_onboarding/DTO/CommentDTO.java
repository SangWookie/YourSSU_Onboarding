package yourssu.yourssu_onboarding.DTO;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentDTO {
    private String email;
    private String password;
    private String content;

    public void validateData(){
        if(content == null || content.isEmpty()){
            throw new IllegalArgumentException("Incorrect request");
        }
    }
}
