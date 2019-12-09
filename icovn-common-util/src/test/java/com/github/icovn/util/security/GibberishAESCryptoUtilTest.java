package com.github.icovn.util.security;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class GibberishAESCryptoUtilTest {

  private String key = "454f08bb1165f3c933fb514100d99f38";
  private String value = "MTU3MDk3NDQ1Mzg4OX5QcmVZNEV4T1drWWtkTEtKRk9IVXNCWjV";

  @Test
  public void encryptAndDecrypt() throws Exception {
    GibberishAESCryptoUtil cryptoUtil = new GibberishAESCryptoUtil();
    String encrypted = cryptoUtil.encrypt(value, key.toCharArray());

    String decrypted = cryptoUtil.decrypt(encrypted, key.toCharArray());
    assertEquals(value, decrypted);
  }
}