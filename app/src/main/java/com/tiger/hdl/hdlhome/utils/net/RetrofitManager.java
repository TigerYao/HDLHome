package com.tiger.hdl.hdlhome.utils.net;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitManager {
    private static RetrofitManager mInstance;
    public String BASE_URL = "http://152.136.127.193:8085/";
    private OkHttpClient mOkHttpClient;
    private HttpService mService;
    private static boolean isChanged;

    public static synchronized RetrofitManager getInstance() {
        if (mInstance == null || isChanged) {
            isChanged = false;
            synchronized (RetrofitManager.class) {
                mInstance = new RetrofitManager();
                mInstance.init();
            }
        }
        return mInstance;
    }

    public void setBaseUrl(String baseUrl) {
        this.BASE_URL = baseUrl;
        isChanged = true;
    }

    public OkHttpClient getOkHttpClient() {
        if (mOkHttpClient == null) {
            synchronized (RetrofitManager.class) {
                if (mOkHttpClient == null) {
//                    SSLSocketBuild.TrustAllManager trustAllCert = new SSLSocketBuild.TrustAllManager();
                    mOkHttpClient = new OkHttpClient.Builder()
//                            .sslSocketFactory(new SSLSocketFactoryCompat(trustAllCert), trustAllCert)
                            .retryOnConnectionFailure(true)
                            .connectTimeout(15, TimeUnit.SECONDS)
                            .readTimeout(15, TimeUnit.SECONDS)
                            .addInterceptor(new Interceptor() {
                                @Override
                                public Response intercept(Chain chain) throws IOException {
                                    Request request = chain.request()
                                            .newBuilder()
                                            .addHeader("Content-Type", "application/json;charset=UTF-8")
                                            .addHeader("Accept-Encoding", "identity")
                                            .build();
                                    return chain.proceed(request);
                                }
                            }).build();
                }
            }
        }
        return mOkHttpClient;
    }

    public void init() {
        Retrofit retrofit = new Retrofit.Builder()
                .client(getOkHttpClient())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                //设置数据解析器
                .addConverterFactory(GsonConverterFactory.create())
                //设置网络请求的Url地址
                .baseUrl(BASE_URL)
                .build();
        mService = retrofit.create(HttpService.class);

    }

    public HttpService getService() {
        return mService;
    }
}