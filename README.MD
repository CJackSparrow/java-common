# icovn-common-logging
- Default module will use configuration in this file logback.xml
- You can use other configuration by:
    + Step 1: add "logging.config: classpath:logback-production.xml" in your configuration file
    + Step 2: declare following environment variables KAFKA_APPLICATION, KAFKA_TOPIC and KAFKA_SERVERS

# TODO
- [ ] icovn-common-job: separate database with main datasource
- [ ] icovn-common-job: create jobs from config file (.yml) if job not exist
- [x] Venus