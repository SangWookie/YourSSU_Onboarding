package yourssu.yourssu_onboarding.DTO;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ArticleDTO {
    private String email;
    private String password;
    private String title;
    private String content;

    public void validateData(){
        if(title == null || title.isEmpty() || title.equals(" ")){
            throw new IllegalArgumentException("Incorrect request");
        }
        if(content == null || content.isEmpty() || content.equals(" ")){
            throw new IllegalArgumentException("Incorrect request");
        }
    }
}
