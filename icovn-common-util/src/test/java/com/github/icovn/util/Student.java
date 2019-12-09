package com.github.icovn.util;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
@JacksonXmlRootElement(localName = "")
class Student {
  private String name;

  private int age;

  public Student(){}
}
