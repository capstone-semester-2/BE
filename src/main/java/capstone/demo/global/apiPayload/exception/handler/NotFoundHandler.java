package capstone.demo.global.apiPayload.exception.handler;

import capstone.demo.global.apiPayload.code.BaseErrorCode;
import capstone.demo.global.apiPayload.exception.GeneralException;

public class NotFoundHandler extends GeneralException {
    public NotFoundHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
