package com.tiger.hdl.hdlhome.utils.net;

public class HttpApiImp {
    public interface NetResponse<T> {
        void onError(final Throwable e);

        void onSuccess(T model);

        void onProgress(int progress);
    }
}