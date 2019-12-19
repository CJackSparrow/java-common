package com.github.icovn.facebook.ads.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor(staticName = "of")
@Data
public class MyCampaignParam {

  private AppInformation appInformation;

  private String dateParam;

  private String campaignId;
}
