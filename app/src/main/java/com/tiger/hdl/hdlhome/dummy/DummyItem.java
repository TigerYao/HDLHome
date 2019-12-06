package com.tiger.hdl.hdlhome.dummy;

public class DummyItem {
    public String id;
    public String content;
    public String details;

    public DummyItem() {
    }

    public DummyItem(String id, String content, String details) {
        this.id = id;
        this.content = content;
        this.details = details;
    }

    @Override
    public String toString() {
        return content;
    }
}