package com.github.icovn.facebook.ads.model;

import com.facebook.ads.sdk.AdAccount;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor(staticName = "of")
@Data
public class MyAccountParam {

  private AppInformation appInformation;

  private AdAccount account;

  private String dateParam;
}
