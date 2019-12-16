package com.tiger.hdl.hdlhome.utils.net;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.tiger.hdl.hdlhome.dummy.ConfigMode;
import com.tiger.hdl.hdlhome.dummy.DeskInfo;
import com.tiger.hdl.hdlhome.dummy.ServiceIpEntity;
import com.tiger.hdl.hdlhome.utils.DisplayUtil;
import com.tiger.hdl.hdlhome.utils.FileUtils;
import com.tiger.hdl.hdlhome.utils.net.client.TcpConnConfig;
import com.tiger.hdl.hdlhome.utils.net.client.XTcpClient;
import com.tiger.hdl.hdlhome.utils.net.client.bean.TargetInfo;
import com.tiger.hdl.hdlhome.utils.net.client.bean.TcpMsg;
import com.tiger.hdl.hdlhome.utils.net.client.listener.TcpClientListener;
import com.tiger.hdl.hdlhome.utils.net.client.manager.TcpClientManager;

import java.io.File;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

public class SocketClientUtil {
    private final String TAG = "SocketClientUtil";
    private static SocketClientUtil mInstance = null;

    private XTcpClient xTcpClient;
    private Context mCtx;
    private int refreshTime = 5;
    private OnMsgListener mClientListener;

    public static SocketClientUtil getInstance() {
        if (mInstance == null)
            mInstance = new SocketClientUtil();
        return mInstance;
    }

    private SocketClientUtil() {
    }

    public void setCtx(Context ctx) {
        this.mCtx = ctx;
    }

    public void setClientListener(OnMsgListener clientListener) {
        this.mClientListener = clientListener;
    }

    public void openConfig(String configPath) {
        disConnect();
        Observable.just(configPath).map(new Function<String, ConfigMode>() {
            @Override
            public ConfigMode apply(String s) throws Exception {
                Log.i(TAG, "openConfig..." + Thread.currentThread().getName());
                ConfigMode configMode = new ConfigMode();
                String str = null;
                if (s.contains("android_asset") && s.startsWith("file")) {
                    str = FileUtils.readAssTextFile(mCtx, s);
                    configMode = new Gson().fromJson(str, ConfigMode.class);
                } else {
                    str = FileUtils.readTextFile(s);
                    configMode = new Gson().fromJson(str, ConfigMode.class);
                }
                return configMode;
            }
        }).observeOn(AndroidSchedulers.mainThread()).onErrorResumeNext(new Function<Throwable, ObservableSource<? extends ConfigMode>>() {
            @Override
            public ObservableSource<? extends ConfigMode> apply(Throwable throwable) throws Exception {
                Toast.makeText(mCtx, "读取文件错误", Toast.LENGTH_LONG).show();
                return Observable.just(new ConfigMode());
            }
        }).subscribeOn(Schedulers.io()).map(new Function<ConfigMode, ServiceIpEntity>() {
            @Override
            public ServiceIpEntity apply(ConfigMode configMode) throws Exception {
                Log.i(TAG, "ConfigMode..." + Thread.currentThread().getName());
                ServiceIpEntity ipEntity = new ServiceIpEntity();
                if (configMode.model != null) {
                    ipEntity.type = configMode.model;
                    refreshTime = configMode.refresh;
                    if (configMode != null && configMode.socket != null && !configMode.socket.isEmpty()) {
                        int index = configMode.socket.indexOf(ipEntity);
                        if (index > -1) {
                            ipEntity = configMode.socket.get(index);
                        }
                    }
                }
                return ipEntity;
            }
        }).subscribeOn(Schedulers.io()).subscribe(new Consumer<ServiceIpEntity>() {
            @Override
            public void accept(ServiceIpEntity ipEntity) throws Exception {
                Log.i(TAG, "ServiceIpEntity..." + ipEntity.toString());
                if (ipEntity != null && ipEntity.ip != null)
                    connetct(ipEntity.ip, ipEntity.port);
            }
        });
    }

    public void disConnect() {
        if (xTcpClient != null) {
            xTcpClient.disconnect();
            xTcpClient = null;
        }
    }

    private void connetct(String ip, int port) {
        TargetInfo targetInfo = new TargetInfo(ip, port);
        if (xTcpClient != null && xTcpClient.isConnected()) {
            xTcpClient.disconnect();
            TcpClientManager.clearAllTcpClient();
        }
        xTcpClient = XTcpClient.getTcpClient(targetInfo);
        xTcpClient.addTcpClientListener(tcpClientListener);
        xTcpClient.config(new TcpConnConfig.Builder().setConnTimeout(15 * 1000).setCharsetName("gb2312").setIsReconnect(true).create());
        if (xTcpClient.isDisconnected())
            xTcpClient.connect();
    }

    private String getSendMsg() {
        JsonObject jsonObject = new JsonObject();
        String sn = DisplayUtil.getMacAddress();
        jsonObject.addProperty("id", sn);
        jsonObject.addProperty("deskid", "all");
        return jsonObject.toString();
    }

    private void pareseMsg(final String strMsg) {
        Observable.just(strMsg).map(new Function<String, DeskInfo>() {
            @Override
            public DeskInfo apply(String stringBuilder) throws Exception {
                DeskInfo deskInfo = new DeskInfo();
                try {
                    if (TextUtils.isEmpty(strMsg) || strMsg.length() <= 0)
                        return deskInfo;
                    deskInfo = new Gson().fromJson(strMsg, DeskInfo.class);
                    Log.i(TAG, "deskInfo..." + strMsg);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                return deskInfo;
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<DeskInfo>() {
            @Override
            public void accept(DeskInfo deskInfo) throws Exception {
                if (mClientListener != null && deskInfo != null && deskInfo.model != null)
                    mClientListener.onReceived(deskInfo);
            }
        });
    }

    StringBuilder strMsg = new StringBuilder();
    TcpClientListener tcpClientListener = new TcpClientListener() {
        @Override
        public void onConnected(final XTcpClient client) {
            Log.i(TAG, "onConnected...");
            client.sendMsg(getSendMsg());
            Observable.interval(refreshTime, TimeUnit.SECONDS).filter(new Predicate<Long>() {
                @Override
                public boolean test(Long aLong) throws Exception {
                    return xTcpClient != null && xTcpClient.isConnected();
                }
            }).map(new Function<Long, Boolean>() {
                @Override
                public Boolean apply(Long aLong) throws Exception {
                    TcpMsg msg = client.sendMsg(getSendMsg());
                    return msg == null;
                }
            }).subscribe();
            if (mClientListener != null)
                mClientListener.onConnected();
        }

        @Override
        public void onSended(XTcpClient client, TcpMsg tcpMsg) {
            Log.i(TAG, "onSended...");
        }

        @Override
        public void onDisconnected(XTcpClient client, String msg, Exception e) {
            Log.i(TAG, "onDisconnected..." + msg);
            if (mClientListener != null)
                mClientListener.onDisconnect();
        }

        @Override
        public void onReceive(XTcpClient client, TcpMsg tcpMsg) {
            Observable.just(tcpMsg).filter(new Predicate<TcpMsg>() {
                @Override
                public boolean test(TcpMsg tcpMsg) throws Exception {
                    return (tcpMsg != null && !TextUtils.isEmpty(tcpMsg.getSourceDataString()));
                }
            }).map(new Function<TcpMsg, String>() {
                @Override
                public String apply(TcpMsg tcpMsg) throws Exception {
                    Log.i(TAG, "onReceive..." + tcpMsg.getSourceDataString());
//                  while (tcpMsg != null && !TextUtils.isEmpty(tcpMsg.getSourceDataString())) {
                    strMsg.append(tcpMsg.getSourceDataString());
//                  }
                    return strMsg.toString();
                }
            }).subscribeOn(Schedulers.io()).subscribeOn(Schedulers.newThread()).subscribe(new Consumer<String>() {
                @Override
                public void accept(String s) throws Exception {
                    pareseMsg(s);
                    strMsg = new StringBuilder();
                }
            });
        }

        @Override
        public void onValidationFail(XTcpClient client, TcpMsg tcpMsg) {
            Log.i(TAG, "onValidationFail...");
        }
    };

    public interface OnMsgListener {
        void onReceived(DeskInfo deskInfo);

        void onConnected();

        void onDisconnect();
    }
}
