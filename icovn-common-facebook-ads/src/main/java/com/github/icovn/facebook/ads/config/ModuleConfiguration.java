package com.github.icovn.facebook.ads.config;

import static reactor.bus.selector.Selectors.$;

import com.github.icovn.facebook.ads.consumer.ConsumerAccountInformation;
import com.github.icovn.facebook.ads.consumer.ConsumerCampaignInformation;
import com.github.icovn.facebook.ads.consumer.ConsumerDateReport;
import com.github.icovn.facebook.ads.model.MyBusType;
import com.github.icovn.facebook.ads.service.FacebookCampaignService;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import reactor.Environment;
import reactor.bus.EventBus;

@Configuration
@ComponentScan("com.github.icovn.facebook.ads.service")
@Slf4j
public class ModuleConfiguration {

  private final EventBus eventBus;

  private final ConsumerAccountInformation consumerAccountInformation;

  private final ConsumerCampaignInformation consumerCampaignInformation;

  private final ConsumerDateReport consumerDateReport;

  public ModuleConfiguration(EventBus eventBus,
      ConsumerAccountInformation consumerAccountInformation,
      ConsumerCampaignInformation consumerCampaignInformation,
      ConsumerDateReport consumerDateReport) {
    this.eventBus = eventBus;
    this.consumerAccountInformation = consumerAccountInformation;
    this.consumerCampaignInformation = consumerCampaignInformation;
    this.consumerDateReport = consumerDateReport;
  }

  @Bean
  Environment env() {
    return Environment.initializeIfEmpty().assignErrorJournal();
  }

  @Bean
  EventBus createEventBus(Environment env) {
    return EventBus.create(env, reactor.Environment.THREAD_POOL);
  }

  @Bean
  FacebookCampaignService facebookCampaignService() {
    return new FacebookCampaignService();
  }

  @PostConstruct
  public void init() {
    log.info("init vn.topica.sf18.client.facebook success");

    eventBus.on($(MyBusType.APP_REPORT), consumerDateReport);
    eventBus.on($(MyBusType.ACCOUNT_INFORMATION), consumerAccountInformation);
    eventBus.on($(MyBusType.CAMPAIGN_INFORMATION), consumerCampaignInformation);
  }
}
