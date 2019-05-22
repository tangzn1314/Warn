package com.tianque.warn.common.network;

import com.tianque.warn.bean.UserInfo;

/**
 * @author ctrun on 2018/12/25.
 */
public class LoginResponse extends BaseResponse {
    public UserInfo user;
    public String token = "";
}
