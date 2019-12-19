package com.github.icovn.google.ads.service;

import com.google.api.ads.adwords.axis.v201809.mcm.ManagedCustomer;
import com.google.api.ads.adwords.lib.client.AdWordsSession;
import com.google.api.ads.adwords.lib.factory.AdWordsServicesInterface;
import com.google.api.ads.adwords.lib.jaxb.v201809.ReportDefinitionDateRangeType;
import com.google.api.ads.adwords.lib.utils.ReportDownloadResponseException;
import com.google.api.ads.adwords.lib.utils.ReportException;
import java.io.IOException;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class GoogleReportService {

  @Value("${app.report.folder}")
  private String reportFolder;

  @Autowired
  private GoogleAccountService accountService;

  @Autowired
  private GoogleAuthService authService;

  @Autowired
  private GoogleReportAsyncService asyncService;

  @Async
  public void getReport(ReportDefinitionDateRangeType dateRangeType)
      throws IOException, ReportDownloadResponseException, ReportException {
    if (dateRangeType.equals(ReportDefinitionDateRangeType.YESTERDAY)) {
      LocalDate startDate = LocalDate.now().minusDays(1);
      LocalDate endDate = LocalDate.now();
      getReport(startDate, endDate);
    }
  }

  @Async
  public void getReport(LocalDate startDate, LocalDate endDate)
      throws IOException, ReportDownloadResponseException, ReportException {
    log.info("(getReport)startDate: {}, endDate: {}", startDate, endDate);
    AdWordsServicesInterface adWordsService = authService.getAdWordsService();
    AdWordsSession adWordsSession = authService.getAdWordsSession();

    List<ManagedCustomer> accounts = accountService.getAllAccounts(adWordsService, adWordsSession);
    log.info("Number of accounts {}", accounts.size());

    for (ManagedCustomer account : accounts) {
      log.info(
          "Account, customerId: {}, name: {}, canManageClients: {}",
          account.getCustomerId(),
          account.getName(),
          account.getCanManageClients());

      if (account.getCanManageClients()) {
        continue;
      }

      AdWordsSession adWordsAccountSession =
          authService.getAdWordsSession(Long.toString(account.getCustomerId()));
      String filePath = reportFolder + account.getCustomerId() + ".csv";
      asyncService.getReport(filePath, adWordsService, adWordsAccountSession, startDate, endDate);
    }
  }
}
