package com.github.icovn.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class MobileUtilTest {

  @Test
  public void formatThaiMobileNumber() {}

  @Test
  public void formatMobileNumberWithPlus() {
    String msisdn = "+84383523686";
    String countryCode = "84";
    assertEquals("84383523686", MobileUtil.formatMobileNumber(msisdn, countryCode));
  }

  @Test
  public void formatMobileNumberWithZeroAtStart() {
    String msisdn = "0383523686";
    String countryCode = "84";
    assertEquals("84383523686", MobileUtil.formatMobileNumber(msisdn, countryCode));
  }

  @Test
  public void formatMobileNumberWithInvalidCharacters() {
    String msisdn = "383.523-686";
    String countryCode = "84";
    assertEquals("84383523686", MobileUtil.formatMobileNumber(msisdn, countryCode));
  }

  @Test
  public void formatMobileNumber() {
    String msisdn = "84383523686";
    String countryCode = "84";
    assertEquals("84383523686", MobileUtil.formatMobileNumber(msisdn, countryCode));
  }

  @Test
  public void removeZeroFromStart() {
    String msisdn = "0383523686";
    assertEquals("383523686", MobileUtil.removeZeroFromStart(msisdn));
  }

  @Test
  public void removeCountryCode() {
    String msisdn = "84383523686";
    String countryCode = "84";
    assertEquals("383523686", MobileUtil.removeCountryCode(msisdn, countryCode));
  }
}
