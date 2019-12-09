package com.tiger.hdl.hdlhome.dummy;

import java.util.List;

public class DeskInfo {
   public String model;
   public List<DummyItem> data;

   @Override
   public String toString() {
      return "DeskInfo{" +
              "model='" + model + '\'' +
              ", data=" + data +
              '}';
   }


}
