package com.tianque.warn.common.network;

/**
 * @author ctrun on 2018/10/12.
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class ApiException extends RuntimeException {
    private static final long serialVersionUID = 8120239524850900803L;

    public static final int NETWORK_ERROR = -1;
    public static final int NETWORK_ERROR_SERVICE = -2;

    public static final String ERROR_MSG_CONNECT_FAIL = "连接服务器失败，请检查网络或稍后重试";
    public static final String ERROR_MSG_SERVICE_ERROR = "服务器内部错误，请稍后重试";

    private int mErrorCode;

    public ApiException(int errorCode, String errorMessage, Throwable cause) {
        super(errorMessage, cause);
        mErrorCode = errorCode;
    }

    public ApiException(int errorCode, String errorMessage) {
        super(errorMessage);
        mErrorCode = errorCode;
    }
}
