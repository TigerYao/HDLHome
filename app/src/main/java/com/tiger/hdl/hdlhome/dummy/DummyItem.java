package com.tiger.hdl.hdlhome.dummy;

import android.graphics.Color;

/**
 *     {"model":"A","data":[{"did":"1","status":"1"},{"did":"2","status":"1"},{"did":"3","status":"0"},{"did":"2","status":"3"},{"did":"3","status":"1"},{"did":"2","status":"3"},{"did":"3","status":"1"},{"did":"2","status":"3"},{"did":"3","status":"3"},{"did":"2","status":"3"},{"did":"3","status":"0"},{"did":"2","status":"3"},{"did":"3","status":"0"},{"did":"2","status":"3"},{"did":"3","status":"0"},{"did":"2","status":"3"},{"did":"3","status":"0"},{"did":"2","status":"3"},{"did":"3","status":"0"},{"did":"2","status":"3"},{"did":"3","status":"0"},{"did":"2","status":"3"},{"did":"3","status":"0"},{"did":"2","status":"3"},{"did":"3","status":"0"},{"did":"2","status":"3"},{"did":"3","status":"0"},{"did":"2","status":"3"},{"did":"3","status":"0"},{"did":"2","status":"3"},{"did":"3","status":"0"}
 * ,{"did":"2","status":"3"},{"did":"3","status":"0"},{"did":"2","status":"3"},{"did":"3","status":"0"},{"did":"2","status":"3"},{"did":"3","status":"0"},{"did":"2","status":"3"},{"did":"3","status":"0"},{"did":"2","status":"3"},{"did":"3","status":"0"},{"did":"2","status":"3"},{"did":"3","status":"0"}
 * ,{"did":"2","status":"3"},{"did":"3","status":"0"},{"did":"2","status":"3"},{"did":"3","status":"0"},{"did":"2","status":"3"},{"did":"3","status":"1"},{"did":"2","status":"3"},{"did":"3","status":"1"},{"did":"2","status":"3"},{"did":"3","status":"1"},{"did":"2","status":"3"},{"did":"3","status":"0"},{"did":"2","status":"3"},{"did":"3","status":"0"}
 * ]}
 */
public class DummyItem {
    public String did;
    public String content;
    public int status = -1;
    public int color = 0;

    public int getColor() {
        if (color > 0)
            return color;
        switch (status) {
            case 1:
                color = Color.RED;
                break;
            case 0:
                color = Color.GREEN;
                break;
            case 3:
                color = Color.YELLOW;
                break;
            default:
                color = Color.GRAY;
                break;
        }

        return color;
    }

    @Override
    public String toString() {
        return "DummyItem{" +
                "did='" + did + '\'' +
                ", content='" + content + '\'' +
                ", state=" + status +
                '}';
    }
}