package com.github.icovn.queue.config;

import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.SimpleMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
@ComponentScan({
    "com.github.icovn.queue.service"
})
@Slf4j
public class RabbitmqPublisherConfiguration {

  @PostConstruct
  public void postConstruct() {
    log.info("(init)RabbitmqConfiguration");
  }

  @Bean(name = "rabbitTemplate")
  @Primary
  public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
    RabbitTemplate template = new RabbitTemplate(connectionFactory);
    template.setMessageConverter(new Jackson2JsonMessageConverter());
    return template;
  }

  @Bean(name = "rabbitTemplateSimple")
  public RabbitTemplate rabbitTemplateSimple(ConnectionFactory connectionFactory) {
    RabbitTemplate template = new RabbitTemplate(connectionFactory);
    template.setMessageConverter(new SimpleMessageConverter());
    return template;
  }
}
