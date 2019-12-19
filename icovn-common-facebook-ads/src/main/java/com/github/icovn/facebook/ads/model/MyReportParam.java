package com.github.icovn.facebook.ads.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor(staticName = "of")
@Data
public class MyReportParam {

  private AppInformation appInformation;

  private String dateParam;
}
