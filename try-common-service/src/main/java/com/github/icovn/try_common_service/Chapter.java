package com.github.icovn.try_common_service;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@Table(name = "chapter")
public class Chapter {

  @Id
  @GeneratedValue
  private Integer id;
  private String name;
  private int order;

  public static Chapter of(String name, int order){
    Chapter chapter = new Chapter();
    chapter.setName(name);
    chapter.setOrder(order);
    return chapter;
  }
}
