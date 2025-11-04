package capstone.demo.global.util;

import capstone.demo.global.apiPayload.code.status.ErrorStatus;
import capstone.demo.global.apiPayload.exception.GeneralException;
import capstone.demo.global.security.AuthDetails;

public class GlobalAuthUtil {
    private GlobalAuthUtil() {}

    public static Long extractUserId(AuthDetails authDetails) {
        if (authDetails == null) {
            throw new GeneralException(ErrorStatus._UNAUTHORIZED);
        }
        return authDetails.user().getId();
    }
}
