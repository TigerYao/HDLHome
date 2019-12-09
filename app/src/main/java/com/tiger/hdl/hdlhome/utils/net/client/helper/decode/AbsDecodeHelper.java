package com.tiger.hdl.hdlhome.utils.net.client.helper.decode;

import com.tiger.hdl.hdlhome.utils.net.client.TcpConnConfig;
import com.tiger.hdl.hdlhome.utils.net.client.bean.TargetInfo;

/**
 * 解析消息的处理
 */
public interface AbsDecodeHelper {
    /**
     *
     * @param data  完整的数据包
     * @param targetInfo    对方的信息(ip/port)
     * @param tcpConnConfig    tcp连接配置，可自定义
     * @return
     */
    byte[][] execute(byte[] data, TargetInfo targetInfo, TcpConnConfig tcpConnConfig);
}
