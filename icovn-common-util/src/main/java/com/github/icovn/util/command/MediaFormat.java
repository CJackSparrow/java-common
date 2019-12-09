package com.github.icovn.util.command;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class MediaFormat {

  private String filename;

  @JsonProperty("nb_streams")
  private Integer numberOfStreams;

  @JsonProperty("format_name")
  private String formatName;

  @JsonProperty("format_long_name")
  private String formatLongName;

  private Double duration;
}
