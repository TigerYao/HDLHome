package com.tiger.hdl.hdlhome.utils.net.client.helper.decode;

import com.tiger.hdl.hdlhome.utils.net.client.TcpConnConfig;
import com.tiger.hdl.hdlhome.utils.net.client.bean.TargetInfo;

public class BaseDecodeHelper implements AbsDecodeHelper {
    @Override
    public byte[][] execute(byte[] data, TargetInfo targetInfo, TcpConnConfig tcpConnConfig) {
        return new byte[][]{data};
    }
}
