package com.tianque.warn.common.network;

/**
 * @author ctrun on 2018/9/14.
 */
@SuppressWarnings("WeakerAccess")
public class BaseResponse<T> {
    private static final int CODE_SUCCESS = 0;
    private static final int DEFAULT_CODE = -1;
    /**
     * 雪亮、智安账号过期，返回状态码
     */
    private static final int SESSION_EXPIRED_1 = 40101;
    /**
     * 平安通账号过期，返回状态码
     */
    private static final int SESSION_EXPIRED_2 = 302;


    public int code = DEFAULT_CODE;
    public String msg;
    public T data;

    public int errorCode = DEFAULT_CODE;
    public String message;

    public boolean isSuccess() {
        return code == CODE_SUCCESS;
    }

    public String getErrorMessage() {
        if (errorCode != DEFAULT_CODE) {
            return message;
        }

        if (code != DEFAULT_CODE && code != CODE_SUCCESS) {
            return msg;
        }

        return "";
    }

    /**
     * 判断是否是session失效或在别处登录
     */
    @SuppressWarnings("RedundantIfStatement")
    public boolean isSessionExpired() {
        if (code == SESSION_EXPIRED_1 || errorCode == SESSION_EXPIRED_2) {
            return true;
        }

        return false;
    }
}
