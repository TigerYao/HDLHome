package com.tiger.hdl.hdlhome.utils.net.client.manager;

import com.tiger.hdl.hdlhome.utils.net.client.XTcpClient;
import com.tiger.hdl.hdlhome.utils.net.client.bean.TargetInfo;

import java.util.HashSet;
import java.util.Set;

/**
 * tcpclient的管理者
 */
public class TcpClientManager {
    private static Set<XTcpClient> sMXTcpClients = new HashSet<>();

    public static void putTcpClient(XTcpClient XTcpClient) {
        sMXTcpClients.add(XTcpClient);
    }
    public static void clearTcpClient(XTcpClient XTcpClient) {
        sMXTcpClients.remove(XTcpClient);
    }
    public static void clearAllTcpClient() {
        sMXTcpClients.clear();
    }


    public static XTcpClient getTcpClient(TargetInfo targetInfo) {
        for (XTcpClient tc : sMXTcpClients) {
            if (tc.getTargetInfo().equals(targetInfo)) {
                return tc;
            }
        }
        return null;
    }
}
