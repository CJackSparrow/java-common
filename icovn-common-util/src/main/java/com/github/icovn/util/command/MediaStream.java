package com.github.icovn.util.command;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class MediaStream {

  private int index;

  @JsonProperty("codec_name")
  private String codecName;

  @JsonProperty("codec_long_name")
  private String codecLongName;

  @JsonProperty("codec_type")
  private String codecType;

  private Integer width;

  private Integer height;

  @JsonProperty("duration_ts")
  private Long durationTs;
}
