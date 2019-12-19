package com.github.icovn.facebook.ads.service;

import static com.github.icovn.util.ExceptionUtil.getFullStackTrace;

import com.facebook.ads.sdk.AdAccount;
import com.github.icovn.facebook.ads.model.AppInformation;
import com.github.icovn.facebook.ads.model.AppProperties;
import com.github.icovn.facebook.ads.model.MyAccountParam;
import com.github.icovn.facebook.ads.model.MyBusType;
import com.github.icovn.facebook.ads.model.MyReportParam;
import com.github.icovn.util.DateUtil;
import java.util.Date;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import reactor.bus.Event;
import reactor.bus.EventBus;

@Slf4j
public class FacebookCampaignService {

  @Value("${api.facebookAds.enableDebug}")
  protected Boolean enableDebug;

  @Autowired
  private AppProperties appProperties;

  @Autowired
  private EventBus eventBus;

  @Async
  public void getData(Date start, Date end) {
    log.info("(getData)start:{}, end:{}", start, end);

    List<String> list = DateUtil.toList(start, end, "yyyy-MM-dd");

    for (String dateParam : list) {
      for (AppInformation appInformation : appProperties.getApps()) {
        eventBus.notify(
            MyBusType.APP_REPORT, Event.wrap(MyReportParam.of(appInformation, dateParam)));
      }
    }
  }

  @Async
  public void getData(Date start, Date end, String app) {
    log.info("(getData)start:{}, end:{}, app:{}", start, end, app);

    for (AppInformation appInformation : appProperties.getApps()) {
      if (appInformation.getApp().equals(app)) {
        List<String> list = DateUtil.toList(start, end, "yyyy-MM-dd");
        for (String dateParam : list) {
          eventBus.notify(
              MyBusType.APP_REPORT, Event.wrap(MyReportParam.of(appInformation, dateParam)));
        }
      }
    }
  }

  @Async
  public void getData(Date start, Date end, String app, String accountId) {
    log.info("(getData)start:{}, end:{}, app:{}, accountId:{}", start, end, app, accountId);

    for (AppInformation appInformation : appProperties.getApps()) {
      if (appInformation.getApp().equals(app)) {
        AdAccount account = findById(appInformation, accountId);
        if (account != null) {
          List<String> list = DateUtil.toList(start, end, "yyyy-MM-dd");
          for (String dateParam : list) {
            eventBus.notify(
                MyBusType.ACCOUNT_INFORMATION,
                Event.wrap(MyAccountParam.of(appInformation, account, dateParam)));
          }
        }
      }
    }
  }

  private AdAccount findById(AppInformation appInformation, String id) {
    try {
      return new AdAccount("act_" + id, appInformation.getContext(enableDebug))
          .get()
          .requestTosAcceptedField()
          .execute();
    } catch (Exception ex) {
      log.error("findById - appInformation:{}, id:{}, ex:{}", appInformation, id,
          getFullStackTrace(ex));
      return null;
    }
  }
}
