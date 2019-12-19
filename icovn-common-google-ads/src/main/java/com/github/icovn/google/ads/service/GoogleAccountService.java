package com.github.icovn.google.ads.service;

import com.google.api.ads.adwords.axis.utils.v201809.SelectorBuilder;
import com.google.api.ads.adwords.axis.v201809.mcm.ManagedCustomer;
import com.google.api.ads.adwords.axis.v201809.mcm.ManagedCustomerPage;
import com.google.api.ads.adwords.axis.v201809.mcm.ManagedCustomerServiceInterface;
import com.google.api.ads.adwords.lib.client.AdWordsSession;
import com.google.api.ads.adwords.lib.factory.AdWordsServicesInterface;
import com.google.api.ads.adwords.lib.selectorfields.v201809.cm.ManagedCustomerField;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class GoogleAccountService {

  private static final int PAGE_SIZE = 500;

  public List<ManagedCustomer> getAllAccounts(
      AdWordsServicesInterface adWordsService, AdWordsSession session) throws RemoteException {
    List<ManagedCustomer> accounts = new ArrayList<>();

    // Get the ServicedAccountService.
    ManagedCustomerServiceInterface managedCustomerService =
        adWordsService.get(session, ManagedCustomerServiceInterface.class);

    // Create selector builder.
    int offset = 0;
    SelectorBuilder selectorBuilder =
        new SelectorBuilder()
            .fields(
                ManagedCustomerField.CustomerId,
                ManagedCustomerField.Name,
                ManagedCustomerField.CanManageClients)
            .offset(offset)
            .limit(PAGE_SIZE);

    // Get results.
    ManagedCustomerPage page;
    do {
      page = managedCustomerService.get(selectorBuilder.build());

      if (page.getEntries() != null) {
        // Create account tree nodes for each customer.
        accounts.addAll(Arrays.asList(page.getEntries()));
      }
      offset += PAGE_SIZE;
      selectorBuilder.increaseOffsetBy(PAGE_SIZE);
    } while (offset < page.getTotalNumEntries());

    return accounts;
  }
}
