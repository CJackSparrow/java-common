package com.github.icovn.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexUtil {

  public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
      Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

  public static final Pattern VALID_PHONE_NUMBER = Pattern.compile("^\\+(?:[0-9] ?){6,14}[0-9]$");

  public static boolean isValidEmail(String email) {
    Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(email);
    return matcher.find();
  }

  public static boolean isValidPhone(String phoneNumber) {
    Matcher matcher = VALID_PHONE_NUMBER.matcher(phoneNumber);
    return matcher.find();
  }
}
