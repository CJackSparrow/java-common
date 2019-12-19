package com.github.icovn.facebook.ads.consumer;

import static com.github.icovn.util.ExceptionUtil.getFullStackTrace;

import com.facebook.ads.sdk.APIException;
import com.facebook.ads.sdk.APINodeList;
import com.facebook.ads.sdk.APIRequest;
import com.facebook.ads.sdk.AdAccount;
import com.github.icovn.facebook.ads.model.MyAccountParam;
import com.github.icovn.facebook.ads.model.MyBusType;
import com.github.icovn.facebook.ads.model.MyReportParam;
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
public class ConsumerDateReport implements Consumer<Event<MyReportParam>> {

  @Value("${api.facebookAds.enableDebug}")
  private boolean enableDebug;

  @Value("${app.facebookAds.sleepTime:300000}")
  private long sleepTime;

  @Autowired
  private EventBus eventBus;

  @Override
  public void accept(Event<MyReportParam> receiveEvent) {
    MyReportParam myReportParam = receiveEvent.getData();
    log.info("myReportParam: {}", myReportParam);

    try {
      APIRequest<AdAccount> request =
          new APIRequest<AdAccount>(
              myReportParam.getAppInformation().getContext(enableDebug),
              "me",
              "/adaccounts",
              "GET",
              AdAccount.getParser());

      int accountPageIndex = 1;
      List<AdAccount> adAccountList = new ArrayList<>();
      APINodeList<AdAccount> accounts = (APINodeList<AdAccount>) (request.execute());
      while (accounts != null) {
        log.info("page {}, accounts {}", accountPageIndex, accounts.size());
        adAccountList.addAll(accounts);

        accounts = accounts.nextPage();
        ++accountPageIndex;
      }

      for (AdAccount account : adAccountList) {
        MyAccountParam accountParam = MyAccountParam.of(
            myReportParam.getAppInformation(), account, myReportParam.getDateParam());
        eventBus.notify(MyBusType.ACCOUNT_INFORMATION, Event.wrap(accountParam));
      }
    } catch (APIException ex) {
      log.error("myReportParam: {}, ex: {}", myReportParam, getFullStackTrace(ex));

      // The call will be blocked for a minute.
      // During this time the max score will decay, being dropped to 0 after a maximum of 5 minutes.
      try {
        Thread.sleep(sleepTime);
      } catch (InterruptedException e) {
        log.error("ex: {}", getFullStackTrace(e));
      }
      eventBus.notify(MyBusType.APP_REPORT, Event.wrap(myReportParam));
    }
  }
}
