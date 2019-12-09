package com.github.icovn.try_custom_repository;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "link")
public class Link extends BasicEntity {

  @JsonProperty("short_url")
  private String shortUrl;

  @JsonProperty("full_url")
  private String fullUrl;

  @JsonIgnore
  private int clickCount;

  protected Link() {
    // for hibernate
  }

  public Link(String shortUrl, String fullUrl) {
    this.shortUrl = shortUrl;
    this.fullUrl = fullUrl;
  }

  public Link(String shortUrl, String fullUrl, int clickCount) {
    this.shortUrl = shortUrl;
    this.fullUrl = fullUrl;
    this.clickCount = clickCount;
  }

  public String getShortUrl() {
    return shortUrl;
  }

  public String getFullUrl() {
    return fullUrl;
  }

  public int getClickCount() {
    return clickCount;
  }
}
