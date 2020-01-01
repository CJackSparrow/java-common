package com.github.icovn.try_spring_cloud_zoo_keeper;

import static org.springframework.util.SerializationUtils.deserialize;
import static org.springframework.util.SerializationUtils.serialize;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.data.Stat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * https://curator.apache.org/curator-framework/index.html
 */
@Service
@Slf4j
public class ZooKeeperDataService {

  @Autowired
  private CuratorFramework client;

  public Object get(String path) throws Exception {
    String fullPath = "/config/" + path;
    return deserialize(client.getData().forPath(fullPath));
  }

  public String getNamespace(){
    return client.getNamespace();
  }

  public void set(String path, Object value) throws Exception {
    String fullPath = "/config/" + path;
    log.info("(set)fullPath: {}", fullPath);

    Stat stat = client.checkExists().forPath(fullPath);
    log.info("(set)stat: {}", stat);
    if (stat == null) {
      client.create().forPath(fullPath, serialize(value));
    } else {
      client.setData().forPath(fullPath, serialize(value));
    }
  }


}
