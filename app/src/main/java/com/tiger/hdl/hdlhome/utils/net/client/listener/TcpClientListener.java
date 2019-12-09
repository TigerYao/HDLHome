package com.tiger.hdl.hdlhome.utils.net.client.listener;

import com.tiger.hdl.hdlhome.utils.net.client.XTcpClient;
import com.tiger.hdl.hdlhome.utils.net.client.bean.TcpMsg;

/**
 */
public interface TcpClientListener {
    void onConnected(XTcpClient client);

    void onSended(XTcpClient client, TcpMsg tcpMsg);

    void onDisconnected(XTcpClient client, String msg, Exception e);

    void onReceive(XTcpClient client, TcpMsg tcpMsg);

    void onValidationFail(XTcpClient client, TcpMsg tcpMsg);

    class SimpleTcpClientListener implements TcpClientListener {

        @Override
        public void onConnected(XTcpClient client) {

        }

        @Override
        public void onSended(XTcpClient client, TcpMsg tcpMsg) {

        }

        @Override
        public void onDisconnected(XTcpClient client, String msg, Exception e) {

        }

        @Override
        public void onReceive(XTcpClient client, TcpMsg tcpMsg) {

        }

        @Override
        public void onValidationFail(XTcpClient client, TcpMsg tcpMsg) {

        }

    }
}
