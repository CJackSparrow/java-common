package com.github.icovn.google.ads.service;

import com.github.icovn.queue.service.QueueService;
import com.google.api.ads.adwords.lib.client.AdWordsSession;
import com.google.api.ads.adwords.lib.client.reporting.ReportingConfiguration;
import com.google.api.ads.adwords.lib.factory.AdWordsServicesInterface;
import com.google.api.ads.adwords.lib.jaxb.v201809.DownloadFormat;
import com.google.api.ads.adwords.lib.jaxb.v201809.ReportDefinitionReportType;
import com.google.api.ads.adwords.lib.utils.ReportDownloadResponse;
import com.google.api.ads.adwords.lib.utils.ReportDownloadResponseException;
import com.google.api.ads.adwords.lib.utils.ReportException;
import com.google.api.ads.adwords.lib.utils.v201809.ReportDownloaderInterface;
import com.google.api.ads.adwords.lib.utils.v201809.ReportQuery;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class GoogleReportAsyncService {

  @Value("${icovn.google.ads.queue-report}")
  private String queueReport;

  @Autowired
  private QueueService queueService;

  @Async
  public void getReport(
      String reportFile,
      AdWordsServicesInterface adWordsService,
      AdWordsSession session,
      LocalDate startDate,
      LocalDate endDate)
      throws IOException, ReportDownloadResponseException, ReportException {
    log.info(
        "(getReport)reportFile: {}, startDate: {}, endDate: {}", reportFile, startDate, endDate);
    // Create query.
    ReportQuery query =
        new ReportQuery.Builder()
            .fields(
                "AccountDescriptiveName",
                "ExternalCustomerId",
                "Date",
                "CampaignName",
                "CampaignId",
                "AccountCurrencyCode",
                "Impressions",
                "Clicks",
                "Cost" // amount
                )
            .from(ReportDefinitionReportType.CAMPAIGN_PERFORMANCE_REPORT)
            .where("Cost")
            .greaterThan(0)
            .during(startDate, endDate)
            .build();

    // Optional: Set the reporting configuration of the session to suppress header, column name, or
    // summary rows in the report output. You can also configure this via your ads.properties
    // configuration file. See AdWordsSession.Builder.from(Configuration) for details.
    // In addition, you can set whether you want to explicitly include or exclude zero impression
    // rows.
    ReportingConfiguration reportingConfiguration =
        new ReportingConfiguration.Builder()
            .skipReportHeader(false)
            .skipColumnHeader(false)
            .skipReportSummary(false)
            // Set to false to exclude rows with zero impressions.
            .includeZeroImpressions(true)
            .build();
    session.setReportingConfiguration(reportingConfiguration);

    ReportDownloaderInterface reportDownloader =
        adWordsService.getUtility(session, ReportDownloaderInterface.class);

    // Set the property api.adwords.reportDownloadTimeout or call
    // ReportDownloader.setReportDownloadTimeout to set a timeout (in milliseconds)
    // for CONNECT and READ in report downloads.
    ReportDownloadResponse response =
        reportDownloader.downloadReport(query.toString(), DownloadFormat.CSV);
    response.saveToFile(reportFile);

    // Add file to IMPORT QUEUE
    queueService.push(reportFile, queueReport);
    log.info("Report successfully downloaded to: {}", reportFile);
  }
}
