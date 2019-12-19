package com.github.icovn.facebook.ads.consumer;

import static com.github.icovn.util.ExceptionUtil.getFullStackTrace;

import com.facebook.ads.sdk.APINodeList;
import com.facebook.ads.sdk.APIRequest;
import com.facebook.ads.sdk.AdsInsights;
import com.github.icovn.facebook.ads.model.MyBusType;
import com.github.icovn.facebook.ads.model.MyCampaignData;
import com.github.icovn.facebook.ads.model.MyCampaignParam;
import com.github.icovn.queue.service.QueueService;
import com.github.icovn.util.DateUtil;
import java.util.Calendar;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.bus.Event;
import reactor.bus.EventBus;
import reactor.fn.Consumer;

@Service
@Slf4j
public class ConsumerCampaignInformation implements Consumer<Event<MyCampaignParam>> {

  @Value("${icovn.facebook.ads.enable.debug:true}")
  protected Boolean enableDebug;

  @Value("${icovn.facebook.ads.queue-campaign-data}")
  private String queueCampaignData;

  @Value("${icovn.facebook.ads.sleep-time:300000}")
  private long sleepTime;

  private final EventBus eventBus;
  private final QueueService queueService;

  public ConsumerCampaignInformation(EventBus eventBus, QueueService queueService) {
    this.eventBus = eventBus;
    this.queueService = queueService;
  }

  @Override
  public void accept(Event<MyCampaignParam> receiveEvent) {
    MyCampaignParam myCampaignParam = receiveEvent.getData();
    log.info("myCampaignParam: {}", myCampaignParam);

    try {
      APIRequest<AdsInsights> request =
          new APIRequest<>(
              myCampaignParam.getAppInformation().getContext(enableDebug),
              myCampaignParam.getCampaignId(),
              "/insights",
              "GET",
              AdsInsights.getParser());

      APINodeList<AdsInsights> adsInsights =
          (APINodeList<AdsInsights>)
              (request
                  .setParam("time_range", myCampaignParam.getDateParam())
                  .setParam(
                      "fields",
                      "account_id,account_name,account_currency,campaign_id,campaign_name,impressions,clicks,spend")
                  .execute());

      if (adsInsights.size() > 0) {
        // Add to queue here
        log.info("adsInsight {}", adsInsights.get(0));
        queueService.push(toCampaignData(adsInsights.get(0)), queueCampaignData);
      } else {
        log.error("ERROR_DATA_NOTE_EXIST, myCampaignParam: {}", myCampaignParam);
      }
    } catch (Exception ex) {
      log.error("myCampaignParam: {}, ex: {}", myCampaignParam, getFullStackTrace(ex));

      // The call will be blocked for a minute.
      // During this time the max score will decay, being dropped to 0 after a maximum of 5 minutes.
      try {
        Thread.sleep(sleepTime);
      } catch (InterruptedException e) {
        log.error("ex: {}", getFullStackTrace(e));
      }
      eventBus.notify(MyBusType.CAMPAIGN_INFORMATION, Event.wrap(myCampaignParam));
    }
  }

  private MyCampaignData toCampaignData(AdsInsights adsInsights) {
    MyCampaignData campaignData = new MyCampaignData();
    campaignData.setDate(adsInsights.getFieldDateStart());
    campaignData.setCampaign(adsInsights.getFieldCampaignName());
    campaignData.setCampaignId(Long.parseLong(adsInsights.getFieldCampaignId()));
    campaignData.setAccount(adsInsights.getFieldAccountName());
    campaignData.setAccountId(Long.parseLong(adsInsights.getFieldAccountId()));
    campaignData.setSource("FACEBOOK");
    campaignData.setCurrency(adsInsights.getFieldAccountCurrency());
    campaignData.setImpressions(Long.parseLong(adsInsights.getFieldImpressions()));
    campaignData.setClick(Long.parseLong(adsInsights.getFieldClicks()));
    campaignData.setAmount(Double.parseDouble(adsInsights.getFieldSpend()));

    Calendar calendar = Calendar.getInstance();
    calendar.setTime(toDate(adsInsights.getFieldDateStart()));
    campaignData.setDateMonth(calendar.get(Calendar.MONTH) + 1);
    campaignData.setDateYear(calendar.get(Calendar.YEAR));
    return campaignData;
  }

  private Date toDate(String reportDate) {
    return DateUtil.toDate(reportDate, "yyyy-MM-dd");
  }
}
