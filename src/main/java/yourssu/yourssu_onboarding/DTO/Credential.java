package yourssu.yourssu_onboarding.DTO;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Credential {
    private String email;
    private String password;
}
