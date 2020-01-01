package com.github.icovn.try_common_service;

import com.github.icovn.service.model.ModelWithCreator;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor(staticName = "of")
@Data
@Entity
@NoArgsConstructor
@Table(name = "story")
public class Story extends ModelWithCreator {

  private String name;
}
