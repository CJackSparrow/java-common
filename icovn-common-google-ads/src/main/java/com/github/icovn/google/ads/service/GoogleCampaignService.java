package com.github.icovn.google.ads.service;

import com.google.api.ads.adwords.axis.v201809.cm.Campaign;
import com.google.api.ads.adwords.axis.v201809.cm.CampaignPage;
import com.google.api.ads.adwords.axis.v201809.cm.CampaignServiceInterface;
import com.google.api.ads.adwords.lib.client.AdWordsSession;
import com.google.api.ads.adwords.lib.factory.AdWordsServicesInterface;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class GoogleCampaignService {

  private static final int PAGE_SIZE = 100;

  public List<Campaign> getAllCampaigns(
      AdWordsServicesInterface adWordsServices, AdWordsSession session) throws RemoteException {
    List<Campaign> campaigns = new ArrayList<>();

    // Get the CampaignService.
    CampaignServiceInterface campaignService =
        adWordsServices.get(session, CampaignServiceInterface.class);

    int offset = 0;

    String query = "SELECT Id, Name, Status ORDER BY Name";

    CampaignPage page;
    do {
      String pageQuery = query + String.format(" LIMIT %d, %d", offset, PAGE_SIZE);
      // Get all campaigns.
      page = campaignService.query(pageQuery);

      // Display campaigns.
      if (page.getEntries() != null) {
        campaigns.addAll(Arrays.asList(page.getEntries()));
      } else {
        log.info("No campaigns were found.");
      }

      offset += PAGE_SIZE;
    } while (offset < page.getTotalNumEntries());

    return campaigns;
  }
}
