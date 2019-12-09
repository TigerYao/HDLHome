package com.tiger.hdl.hdlhome.dummy;

import java.util.List;

public class ConfigMode {
    public int deskcount;
    public int refresh;
    public String model;
    public List<ServiceIpEntity> socket;


    public String getRequestStr(){
        return "你好啊";
    }

    @Override
    public String toString() {
        return "ConfigMode{" +
                "deskcount=" + deskcount +
                ", Refresh=" + refresh +
                ", model='" + model + '\'' +
                ", sokcet=" + socket +
                '}';
    }
}
