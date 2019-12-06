package com.tiger.hdl.hdlhome;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.tiger.hdl.hdlhome.utils.net.RxSocketServer;

public class MyService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        RxSocketServer.getInstance().setResultListener(new RxSocketServer.SocketListener() {
            @Override
            public void cancleListen() {

            }

            @Override
            public void accept(Object o) throws Exception {

            }
        });
        RxSocketServer.getInstance().connectSocket("....");
    }
}
