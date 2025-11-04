package capstone.demo.global.apiPayload.exception.handler;

import capstone.demo.global.apiPayload.code.BaseErrorCode;
import capstone.demo.global.apiPayload.exception.GeneralException;

public class BadRequestHandler extends GeneralException {
    public BadRequestHandler(BaseErrorCode errorCode) {super(errorCode);}
}
