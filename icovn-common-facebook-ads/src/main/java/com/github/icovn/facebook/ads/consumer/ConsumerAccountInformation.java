package com.github.icovn.facebook.ads.consumer;

import static com.github.icovn.util.ExceptionUtil.getFullStackTrace;

import com.facebook.ads.sdk.APINodeList;
import com.facebook.ads.sdk.AdAccount.APIRequestGetInsights;
import com.facebook.ads.sdk.AdsInsights;
import com.facebook.ads.sdk.AdsInsights.EnumLevel;
import com.github.icovn.facebook.ads.model.MyAccountParam;
import com.github.icovn.facebook.ads.model.MyBusType;
import com.github.icovn.facebook.ads.model.MyCampaignParam;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.bus.Event;
import reactor.bus.EventBus;
import reactor.fn.Consumer;

@Service
@Slf4j
public class ConsumerAccountInformation implements Consumer<Event<MyAccountParam>> {

  @Autowired
  private EventBus eventBus;

  @Value("${app.facebookAds.sleepTime:300000}")
  private long sleepTime;

  @Override
  public void accept(Event<MyAccountParam> receiveEvent) {
    MyAccountParam myAccountParam = receiveEvent.getData();
    log.info("myAccountParam: {}", myAccountParam);

    try {
      List<Object> filtering = new ArrayList<>();
      filtering.add("{'field':'clicks','operator':'GREATER_THAN','value':0}");

      List<AdsInsights> adsInsightsList = new ArrayList<>();
      APIRequestGetInsights request = myAccountParam.getAccount().getInsights();
      APINodeList<AdsInsights> adsInsights =
          request
              .setParam("time_range", myAccountParam.getDateParam())
              .setFiltering(filtering)
              .setLevel(EnumLevel.VALUE_CAMPAIGN)
              .requestAllFields()
              .execute();
      while (adsInsights != null) {
        adsInsightsList.addAll(adsInsights);
        adsInsights = adsInsights.nextPage();
      }

      for (AdsInsights adsInsight : adsInsightsList) {
        eventBus.notify(
            MyBusType.CAMPAIGN_INFORMATION,
            Event.wrap(
                MyCampaignParam.of(
                    myAccountParam.getAppInformation(),
                    myAccountParam.getDateParam(),
                    adsInsight.getFieldCampaignId())));
      }
    } catch (Exception ex) {
      log.error("myAccountParam: {}, ex: {}", myAccountParam, getFullStackTrace(ex));

      // The call will be blocked for a minute.
      // During this time the max score will decay, being dropped to 0 after a maximum of 5 minutes.
      try {
        Thread.sleep(sleepTime);
      } catch (InterruptedException e) {
        log.error("ex: {}", getFullStackTrace(e));
      }
      eventBus.notify(MyBusType.ACCOUNT_INFORMATION, Event.wrap(myAccountParam));
    }
  }
}
