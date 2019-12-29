package com.tiger.hdl.hdlhome.dummy;

import android.graphics.Color;
import android.text.TextUtils;

import com.tiger.hdl.hdlhome.R;

/**
      {"model":"A","data":[{"did":"1","status":"1"},{"did":"005","status":"1"},{"did":"3","status":"0"},{"did":"2","status":"3"},{"did":"3","status":"1"},{"did":"2","status":"3"},{"did":"3","status":"1"},{"did":"2","status":"3"},{"did":"3","status":"3"},{"did":"2","status":"3"},{"did":"3","status":"0"},{"did":"2","status":"3"},{"did":"3","status":"0"},{"did":"2","status":"3"},{"did":"3","status":"0"},{"did":"2","status":"3"},{"did":"3","status":"0"},{"did":"2","status":"3"},{"did":"3","status":"0"},{"did":"2","status":"3"},{"did":"3","status":"0"},{"did":"2","status":"3"},{"did":"3","status":"0"},{"did":"2","status":"3"},{"did":"3","status":"0"},{"did":"2","status":"3"},{"did":"3","status":"0"},{"did":"2","status":"3"},{"did":"3","status":"0"},{"did":"2","status":"3"},{"did":"3","status":"0"}
  ,{"did":"2","status":"3"},{"did":"3","status":"0"},{"did":"2","status":"3"},{"did":"3","status":"0"},{"did":"2","status":"3"},{"did":"3","status":"0"},{"did":"2","status":"3"},{"did":"3","status":"0"},{"did":"2","status":"3"},{"did":"3","status":"0"},{"did":"2","status":"3"},{"did":"3","status":"0"}
  ,{"did":"2","status":"3"},{"did":"3","status":"0"},{"did":"2","status":"3"},{"did":"3","status":"0"},{"did":"2","status":"3"},{"did":"3","status":"1"},{"did":"2","status":"3"},{"did":"3","status":"1"},{"did":"2","status":"3"},{"did":"3","status":"1"},{"did":"2","status":"3"},{"did":"3","status":"0"},{"did":"2","status":"3"},{"did":"3","status":"0"}
  ]}
 */
public class DummyItem implements Comparable<DummyItem>{
    public String did;
    public String content;
    public int status = -1;
    public int color = 0;

    public int getColorId() {
        if (color > 0)
            return color;
        switch (status) {
            case 1:
                color = R.color.red;
                break;
            case 0:
                color = R.color.green;
                break;
            case 3:
                color = R.color.yellow;
                break;
            default:
                color = R.color.gray_7f7f7f;
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

    public int getDidNum(){
        try {
            if (TextUtils.isDigitsOnly(did))
                return Integer.parseInt(did);
        }catch (Exception e){

        }
        return 200;
    }

    @Override
    public int compareTo(DummyItem dummyItem) {
        if(getDidNum() < 199){
            return getDidNum() - dummyItem.getDidNum();
        }
        return 200;
    }
}