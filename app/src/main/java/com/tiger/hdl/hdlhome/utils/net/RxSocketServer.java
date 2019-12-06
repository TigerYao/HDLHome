package com.tiger.hdl.hdlhome.utils.net;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.tiger.hdl.hdlhome.dummy.ConfigMode;
import com.tiger.hdl.hdlhome.dummy.ServiceIpEntity;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by admin on 2018/8/3.
 */

public class RxSocketServer {
    static RxSocketServer rxSocket;
    OutputStream outputStream = null;
    Socket socket = null;
    ServerSocket serverSocket;
    BufferedReader bufferedReader = null;
    ConfigMode mConfigMode;
    /**
     * 读取数据超时时间
     */
    final int READ_TIMEOUT = 10 * 1000;
    /**
     * 连接超时时间
     */
    final int CONNECT_TIMEOUT = 5 * 1000;
    final String IP = "218.29.74.138";
    final int PORT = 11274;
    final String TAG = "RxSocketServer-->", SUCCEED = "初始化成功", TIMEOUT = "连接超时", SEND_ERROR = "发送数据异常";
    /**
     * 网络返回的监听
     */
    SocketListener observer;
    public boolean isCancle = false;
    CompositeDisposable compositeDisposable;

    private RxSocketServer() {
        compositeDisposable = new CompositeDisposable();
    }

    public void connectSocket(final String fileName) {
//        compositeDisposable.add(

        Observable.just(fileName)
                .map(new Function<String, ServiceIpEntity>() {
                    @Override
                    public ServiceIpEntity apply(String s) throws Exception {
//                FileReader fileReader = new FileReader(fileName);
//
//                BufferedReader bufferedReader = new BufferedReader(fileReader);
//                StringBuilder stringBuilder = new StringBuilder();
//                while (bufferedReader.readLine() != null) {
//                    stringBuilder.append(bufferedReader.readLine());
//                }
//                Log.i(TAG, "File..." + stringBuilder.toString());
//                try {
//                    if (stringBuilder != null && stringBuilder.length() > 0) {
//                        mConfigMode = new Gson().fromJson(stringBuilder.toString(), ConfigMode.class);
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                bufferedReader.close();
//                fileReader.close();
                        ServiceIpEntity ipEntity = new ServiceIpEntity();
//                if (mConfigMode != null && mConfigMode.sokcet != null) {
//                    ipEntity.type = mConfigMode.model;
//                    int index = mConfigMode.sokcet.indexOf(ipEntity);
//                    ipEntity = mConfigMode.sokcet.get(index);
//                } else {
                        mConfigMode = new ConfigMode();
                        mConfigMode.Refresh = 5;
                        ipEntity.ip = "192.168.0.144";
                        ipEntity.port = 5555;
//                }
                        return ipEntity;
                    }
                })
                .subscribeOn(Schedulers.io())
                .map(new Function<ServiceIpEntity, Boolean>() {
                    @Override
                    public Boolean apply(ServiceIpEntity ipEntity) throws Exception {
                        boolean isOk = ipEntity == null || TextUtils.isEmpty(ipEntity.ip) ? false : initSocket(ipEntity.ip, ipEntity.port);
                        if (isOk)
                            request();
                        return isOk;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Boolean>() {
                               @Override
                               public void accept(Boolean aBoolean) throws Exception {
                                   if (!aBoolean)
                                       cancleAll(true);

                               }
                           }
//        )
                );
    }

    public static RxSocketServer getInstance() {
        if (rxSocket == null) {
            rxSocket = new RxSocketServer();
        }
        return rxSocket;
    }


    /**
     * 初始化蓝牙通信，需要放在子线程
     *
     * @param ip   {@link RxSocketServer#IP}  ip地址
     * @param port {@link RxSocketServer#PORT} 端口号
     */
    private boolean initSocket(String ip, int port) {
        try {
            serverSocket = new ServerSocket(5555);
            socket = serverSocket.accept();
            socket.setSoTimeout(READ_TIMEOUT);
            final String address = socket.getRemoteSocketAddress().toString();

            Log.d(TAG, "连接成功，连接的设备为：" + address);
//            socket.connect(new InetSocketAddress(InetAddress.getByName(ip), port), CONNECT_TIMEOUT);
            outputStream = socket.getOutputStream();
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
            return true;
        } catch (IOException e) {
            Log.d(TAG, TIMEOUT);
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 发送数据
     *
     * @return 接口返回的数据
     */
    private String sendData(String data) {
        StringBuilder result = new StringBuilder("hello");
        try {
            outputStream.write(data.getBytes("UTF-8"));

            return result.toString();
        } catch (Exception e) {
            if (e != null) {
                Log.d(TAG, e.getMessage() + "..");
                e.printStackTrace();
            }
            return SEND_ERROR;
        }
    }

    /**
     * 取消所有的请求
     *
     * @param isCancle true:取消访问  false:允许访问
     */
    public void cancleAll(boolean isCancle) {
        this.isCancle = isCancle;
        if (true) {
            if (observer != null)
                observer.cancleListen();
            if (compositeDisposable != null)
                compositeDisposable.dispose();
            try {
                if (socket != null && socket.isConnected())
                    socket.close();
            } catch (Exception e) {

            }
        }
    }

    public void request() {
//        compositeDisposable.add(
//        Observable.interval(mConfigMode.Refresh, TimeUnit.SECONDS)
//                .map(new Function<Long, List<DeskInfo>>() {
//                    @Override
//                    public List<DeskInfo> apply(Long configMode) throws Exception {
//                        try {
//                            String request = sendData(mConfigMode.getRequestStr());
//                            if(request != null) {
//                                Gson gson = new Gson();
//                                List<DeskInfo> deskInfos = gson.fromJson(request, new TypeToken<List<DeskInfo>>() {
//                                }.getType());
//                                return deskInfos;
//                            }else
//                                return new ArrayList<>();
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                            return new ArrayList<>();
//                        }
//                    }
//                })
//                .map(new Function<List<DeskInfo>, String>() {
//                    @Override
//                    public String apply(List<DeskInfo> deskInfos) throws Exception {
//                        return "";
//                    }
//                })
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(observer);

        Observable.interval(1, TimeUnit.SECONDS).takeWhile(new Predicate<Long>() {
            @Override
            public boolean test(Long integer) throws Exception {
                return true;
            }
        }).flatMap(new Function<Long, ObservableSource<Integer>>() {
            @Override
            public ObservableSource<Integer> apply(Long aLong) throws Exception {
                try {
                    String len = bufferedReader.readLine();
                    if (len != null)
                        Log.d(TAG, aLong + "...ss...." + len);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return Observable.just(1);
            }
        }).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer aLong) throws Exception {
            }
        });

//        Observable.interval(2, TimeUnit.SECONDS, Schedulers.io())
//                .flatMap(new Function<Long, ObservableSource<Integer>>() {
//                    @Override
//                    public ObservableSource<Integer> apply(Long aLong) throws Exception {
//                        return Observable.just(1);
//                    }
//                })
//                .takeWhile(new Predicate<Integer>() {
//                    @Override
//                    public boolean test(Integer integer) throws Exception {
//                        return true;
//                    }
//                })
//                .observeOn(Schedulers.io())
//                .subscribeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Consumer<Integer>() {
//                    @Override
//                    public void accept(Integer integer) throws Exception {
//                        Log.e(TAG, "i is " + integer);
//                    }
//                });
    }

    /**
     * 设置网络返回的监听
     *
     * @param listener
     */
    public void setResultListener(SocketListener listener) {
        this.observer = listener;
    }

    public interface SocketListener<T> extends Consumer<T> {
        void cancleListen();
    }
}