package com.tiger.hdl.hdlhome.dummy;

public class ServiceIpEntity {
    public String type;
    public String ip;
    public int port;

    @Override
    public String toString() {
        return "ServiceIpEntity{" +
                "type='" + type + '\'' +
                ", ip='" + ip + '\'' +
                ", port=" + port +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof ServiceIpEntity)
            return type.toLowerCase().equals(((ServiceIpEntity) obj).type.toLowerCase());
        return super.equals(obj);
    }
}