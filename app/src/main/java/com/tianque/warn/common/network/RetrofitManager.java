package com.tianque.warn.common.network;

import android.util.Log;

import com.tianque.warn.BuildConfig;
import com.tianque.warn.MyApp;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author ctrun on 2018/9/17.
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public final class RetrofitManager {
    private static final String TAG = "RetrofitManager";
    /**
     * 网络请求超时时间秒
     */
    private static final int DEFAULT_TIMEOUT = 10;
    /**
     * 上传附件超时时间秒
     */
    private static final int UPLOAD_TIMEOUT = 15;

    private Retrofit mRetrofit;
    private ApiService mApiService;

    private RetrofitManager() {
        BizContentParamInterceptor bizContentParamInterceptor = new BizContentParamInterceptor();
        GlobalParamsInterceptor globalParamsInterceptor = new GlobalParamsInterceptor();
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(bizContentParamInterceptor)
                .addInterceptor(globalParamsInterceptor)
                .addInterceptor(loggingInterceptor)
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .build();

        mRetrofit = new Retrofit.Builder()
                .client(okHttpClient)
                //添加gson转换器
                .addConverterFactory(GsonConverterFactory.create())
                //添加rxjava转换器
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(BuildConfig.SERVER_HOST)
                .build();

        //创建默认apiService
        mApiService = mRetrofit.create(ApiService.class);
    }

    /**
     * 使用内部类的好处是，静态内部类不会在单例加载时就加载，
     * 而是在调用getInstance()方法时才进行加载，达到了类似懒汉模式的效果，而这种方法又是线程安全的
     */
    private static class SingletonHolder {
        private static final RetrofitManager INSTANCE = new RetrofitManager();
    }

    /**
     * 获取单例
     */
    public static RetrofitManager getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public static Retrofit getRetrofit() {
        return SingletonHolder.INSTANCE.mRetrofit;
    }

    public static ApiService getApiService() {
        return SingletonHolder.INSTANCE.mApiService;
    }

    public <T> T createApiService(Class<T> service) {
        return mRetrofit.create(service);
    }

    /**
     * 创建与添加BizContent参数
     * 注意: 须添加在GlobalParamsInterceptor前面
     */
    static class BizContentParamInterceptor implements Interceptor {

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request oldRequest = chain.request();
            Request.Builder requestBuilder = oldRequest.newBuilder();

            String bizContent = buildBizContent(oldRequest);
            Log.d(TAG, "bizContent="+bizContent);

            if (isGetRequest(oldRequest)) {
                //清掉Get请求相关参数
                HttpUrl.Builder urlBuilder = clearAllQueryParams(oldRequest);
                urlBuilder.addQueryParameter("bizContent", bizContent);
                requestBuilder.url(urlBuilder.build());
            } else if (isPostRequest(oldRequest)) {
                FormBody.Builder newFormBodyBuilder = new FormBody.Builder();
                newFormBodyBuilder.add("bizContent", bizContent);
                requestBuilder.post(newFormBodyBuilder.build());
            }

            Request newRequest = requestBuilder.build();

            return chain.proceed(newRequest);
        }

        /**
         * 构建bizContent参数(json字符串类型)
         * 将所有Get或Post参数保存到jsonObject对象中
         */
        private static String buildBizContent(Request request) {
            HashMap<String, Object> map = new HashMap<>(10);
            map.put("tqmobile", "true");

            HttpUrl url = request.url();
            Set<String> queryParameterNames = url.queryParameterNames();
            for (String name : queryParameterNames) {
                map.put(name, url.queryParameter(name));
            }

            if (isPostRequest(request)) {
                RequestBody requestBody = request.body();
                if (requestBody instanceof FormBody) {
                    FormBody formBody = (FormBody) requestBody;
                    for (int i = 0; i < formBody.size(); i++) {
                        /*
                            对jsonParam参数统一进行拦截处理
                            将json字符串转为json对象，否则服务器端无法识别
                         */
                        if (ApiService.JSON_PARAM.equals(formBody.name(i))) {
                            try {
                                JSONObject json = new JSONObject(formBody.value(i));
                                Iterator<String> iterator = json.keys();
                                while (iterator.hasNext()) {
                                    String key = iterator.next();
                                    map.put(key, json.opt(key));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        } else {
                            map.put(formBody.name(i), formBody.value(i));
                        }
                    }
                }
            }

            JSONObject jsonBizContent = new JSONObject(map);

            return jsonBizContent.toString();
        }

        private HttpUrl.Builder clearAllQueryParams(Request request) {
            HttpUrl.Builder urlBuilder = request.url().newBuilder();
            HttpUrl url = request.url();
            Set<String> queryParameterNames = url.queryParameterNames();
            for (String name : queryParameterNames) {
                urlBuilder.removeAllQueryParameters(name);
            }

            return urlBuilder;
        }

        private static boolean isPostRequest(Request request) {
            String method = request.method();
            return "POST".equalsIgnoreCase(method);
        }

        private static boolean isGetRequest(Request request) {
            String method = request.method();
            return "GET".equalsIgnoreCase(method);
        }
    }

    /**
     * 全局请求参数添加
     */
    static class GlobalParamsInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request oldRequest = chain.request();
            Request.Builder requestBuilder = oldRequest.newBuilder();

            //添加公共请求头
            Headers.Builder headerBuilder = oldRequest.headers().newBuilder();
            headerBuilder.add("Cookie", "sid=" + MyApp.sUserInfo.sid);
            headerBuilder.add("Cookie", "sourceCode=APP");
            requestBuilder.headers(headerBuilder.build());

            //添加公共参数
            HttpUrl.Builder urlBuilder = oldRequest.url().newBuilder();
            urlBuilder.addQueryParameter("appKey", BuildConfig.APP_KEY);
            urlBuilder.addQueryParameter("requestId", "zhian_" + System.currentTimeMillis());
            urlBuilder.addQueryParameter("timestamp", String.valueOf(System.currentTimeMillis()));
            requestBuilder.url(urlBuilder.build());

            Request newRequest = requestBuilder.build();

            return chain.proceed(newRequest);
        }
    }


    public static ApiService createUserCenterLoginApiService(UserCenterLoginCookieManager.OnSidSetListener listener) {
        Retrofit retrofit = RetrofitManager.getRetrofit();
        OkHttpClient client = (OkHttpClient) retrofit.callFactory();
        OkHttpClient.Builder newClientBuilder = client.newBuilder();
        final UserCenterLoginCookieManager cookieManager = new UserCenterLoginCookieManager(listener);
        newClientBuilder.cookieJar(cookieManager);

        Retrofit.Builder retrofitBuilder = retrofit.newBuilder();
        retrofitBuilder.client(newClientBuilder.build());

        Retrofit newRetrofit = retrofitBuilder.build();
        return newRetrofit.create(ApiService.class);
    }

    public static class UserCenterLoginCookieManager implements CookieJar {
        private String mSid = "";

        private OnSidSetListener mListener;
        public UserCenterLoginCookieManager(OnSidSetListener listener) {
            mListener = listener;
        }

        @Override
        public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
            for (Cookie cookie: cookies) {
                if (cookie.name().equalsIgnoreCase("sid")) {
                    mSid = cookie.value();
                    if (mListener != null) {
                        mListener.onSidSet(mSid);
                    }
                }
            }
        }

        @Override
        public List<Cookie> loadForRequest(HttpUrl url) {
            return Collections.emptyList();
        }

        public String getSid() {
            return mSid;
        }

        public interface OnSidSetListener {
            /**
             * @param sid The sid string that was set.
             */
            void onSidSet(String sid);
        }
    }
}
