package com.github.icovn.util.command;

import java.util.List;
import lombok.Data;

@Data
public class MediaInformation {

  private List<MediaStream> streams;

  private MediaFormat format;
}
