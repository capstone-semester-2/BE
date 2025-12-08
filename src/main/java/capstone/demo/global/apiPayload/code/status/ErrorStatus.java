package capstone.demo.global.apiPayload.code.status;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import capstone.demo.global.apiPayload.code.BaseErrorCode;
import capstone.demo.global.apiPayload.code.ErrorReasonDTO;

@Getter
@AllArgsConstructor
public enum ErrorStatus implements BaseErrorCode {

    // 일반적인 응답
    _INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "서버 에러, 관리자에게 문의 바랍니다."),
    _BAD_REQUEST(HttpStatus.BAD_REQUEST,"COMMON400","잘못된 요청입니다."),
    _UNAUTHORIZED(HttpStatus.UNAUTHORIZED,"COMMON401","인증이 필요합니다."),
    _FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON403", "금지된 요청입니다."),
    REDIS_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "REDIS_ERROR", "Redis 설정에 오류가 발생했습니다."),

    NOT_FOUND_USER(HttpStatus.NOT_FOUND, "USER404", "해당 유저를 찾을 수 없습니다."),
    NOT_FOUND_TOKEN(HttpStatus.NOT_FOUND,"TOKEN404","토큰을 찾을 수 없습니다."),
    NOT_FOUND_PROFILE(HttpStatus.NOT_FOUND,"PROFILE404","해당 프로필을 찾을 수 없습니다."),
    NOT_FOUND_DICTIONARY(HttpStatus.NOT_FOUND,"DICTIONARY404","해당 사전 내용을 찾을 수 없습니다."),
    NOT_FOUND_VOICE(HttpStatus.NOT_FOUND,"VOICE404","해당 사용자 음성 내용을 찾을 수 없습니다."),

    //500
    NOT_FOUND_ADAPTER(HttpStatus.NOT_FOUND,"ADAPTER501","adapter가 필요합니다."),


    // 로그인 관련 응답
    TOKEN_INVALID(HttpStatus.UNAUTHORIZED, "LOGIN4001", "토큰이 유효하지 않습니다."),
    LOGIN_TYPE_INVALID(HttpStatus.BAD_REQUEST,"LOGIN4002","로그인 타입이 존재하지 않습니다."),


    ;


    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ErrorReasonDTO getReason() {
        return ErrorReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .build();
    }

    @Override
    public ErrorReasonDTO getReasonHttpStatus() {
        return ErrorReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .httpStatus(httpStatus)
                .build();
    }
}
