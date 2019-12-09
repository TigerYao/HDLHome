package com.tiger.hdl.hdlhome.dummy;

public class DummyItem {
    public String did;
    public String content;
    public int status = -1;

    @Override
    public String toString() {
        return "DummyItem{" +
                "did='" + did + '\'' +
                ", content='" + content + '\'' +
                ", state=" + status +
                '}';
    }
}