package com.tianque.warn.common.network;

import com.tianque.warn.BuildConfig;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * @author ctrun on 2018/9/17.
 */
@SuppressWarnings("all")
public interface ApiService {
    String JSON_PARAM = "jsonParam";

    String BASE_PATH = "/api/" + BuildConfig.APP_CODE;

    String USER_CENTER_LOGIN        = BASE_PATH + "/user/userCenterLogin.json";
    String LOGIN                    = BASE_PATH + "/user/login.json";
    String ORG_LIST                 = BASE_PATH + "/org/list.json";
    String USER_WARN_COUNT          = BASE_PATH + "/user/warnCount.json";


    //------------------------------------------分割线-----------------------------------------------

    /**
     * 用户中心登录
     */
    @FormUrlEncoded
    @POST(USER_CENTER_LOGIN)
    Observable<Object> userCenterLogin(@Field("userName") String userName,
                                       @Field("password") String password);

    /**
     * 通过用户中心sid换取智安token
     *
     * @param sid 用户中心sid
     */
    @FormUrlEncoded
    @POST(LOGIN)
    Observable<LoginResponse> login(@Field("sid") String sid);

}
