package capstone.demo.global.apiPayload.code;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ReasonDTO {

    private Boolean isSuccess;
    private String code;
    private String message;

    private HttpStatus httpStatus;

    @Builder
    public ReasonDTO(Boolean isSuccess, String code, String message, HttpStatus httpStatus) {
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }
}
