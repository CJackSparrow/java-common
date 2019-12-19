package com.github.icovn.facebook.ads.model;

import lombok.Data;

@Data
public class MyCampaignData {

  private long id;
  private long accountId;
  private long campaignId;
  private long click;
  private long impressions;

  private String account;
  private String campaign;
  private String currency;
  private String date;
  private String source;

  private double amount;

  private int dateMonth;
  private int dateYear;
}
