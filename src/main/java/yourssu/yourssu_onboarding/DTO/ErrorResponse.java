package yourssu.yourssu_onboarding.DTO;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
public class ErrorResponse {
    private String time;
    private String status;
    private String message;
    private String requestURI;

    public ErrorResponse(String status, String message, String requestURI) {
        this.time = LocalDateTime.now().toString();
        this.status = status;
        this.message = message;
        this.requestURI = requestURI;
    }
}
