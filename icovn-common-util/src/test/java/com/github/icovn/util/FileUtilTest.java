package com.github.icovn.util;

import static org.junit.Assert.assertEquals;

public class FileUtilTest {

  @org.junit.Test
  public void getExtension() {
    assertEquals(
        ".jpg",
        FileUtil.getExtension(
            "http://d1nzpkv5wwh1xf.cloudfront.net/320/thailand-58526b8a893f0925740d52ad/20170429-pockpiyawat01/----------------------.jpg"));
  }
}
