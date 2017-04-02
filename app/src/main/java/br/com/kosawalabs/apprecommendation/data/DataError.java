package br.com.kosawalabs.apprecommendation.data;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class DataError {
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({CONNECTION_ERROR, FORBIDDEN, NOT_FOUND})
    public @interface ErrorCode {
    }

    public static final int CONNECTION_ERROR = 0;
    public static final int FORBIDDEN = 403;
    public static final int NOT_FOUND = 404;

    private String cause;
    @ErrorCode
    private int errorCode = -1;

    public DataError(String cause) {
        this.cause = cause;
    }

    public DataError(String cause, @ErrorCode int errorCode) {
        this.cause = cause;
        this.errorCode = errorCode;
    }

    public String getCause() {
        return cause;
    }

    public int getErrorCode() {
        return errorCode;
    }
}
