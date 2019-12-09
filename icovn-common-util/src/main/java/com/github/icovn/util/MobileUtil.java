package com.github.icovn.util;

public class MobileUtil {

  public static String formatThaiMobileNumber(String input) {
    return formatMobileNumber(input, "66");
  }

  public static String formatVietNamMobileNumber(String input) {
    return formatMobileNumber(input, "84");
  }

  public static String formatMobileNumber(String msisdn, String countryCode) {
    msisdn = removeInvalidCharacters(msisdn);

    if(msisdn.startsWith(countryCode)){
      return msisdn;
    }

    if (msisdn.startsWith("+" + countryCode)) {
      return msisdn.substring(1);
    }

    if (msisdn.startsWith("0")) {
      return countryCode + msisdn.substring(1);
    }

    return countryCode + msisdn;
  }

  public static String removeZeroAndCountryCode(String msisdn, String countryCode) {
    msisdn = removeCountryCode(msisdn, countryCode);
    msisdn = removeZeroFromStart(msisdn);

    return msisdn;
  }

  public static String removeZeroFromStart(String msisdn){
    msisdn = removeInvalidCharacters(msisdn);

    if (msisdn.startsWith("0")) {
      msisdn = msisdn.substring(1);
    }

    return msisdn;
  }

  public static String removeCountryCode(String msisdn, String countryCode) {
    msisdn = removeInvalidCharacters(msisdn);

    if (msisdn.startsWith(countryCode)) {
      msisdn = msisdn.substring(countryCode.length());
    }

    if (msisdn.startsWith("+" + countryCode)) {
      msisdn = msisdn.substring(countryCode.length() + 1);
    }

    return msisdn;
  }

  private static String removeInvalidCharacters(String input){
    return input.trim().replaceAll("[^\\d]", "");
  }
}
