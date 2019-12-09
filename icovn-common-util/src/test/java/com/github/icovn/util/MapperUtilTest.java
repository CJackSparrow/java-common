package com.github.icovn.util;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.hamcrest.collection.IsMapContaining;
import org.junit.Before;
import org.junit.Test;

@Slf4j
public class MapperUtilTest {

  private Student student = new Student("Goku", 7);

  private Map<String, Object> expected = new HashMap<>();

  @Before
  public void setUp() throws Exception {
    expected.put("name", "Goku");
    expected.put("age", 7);
  }

  @Test
  public void toMapFromJson() {
    Map<String, Object> map = MapperUtil.toMap("{\"name\":\"Goku\",\"age\":7}");

    doTest(map);
  }

  @Test
  public void toMapFromObject() {
    Map<String, Object> map = MapperUtil.toMap(student);

    doTest(map);
  }

  @Test
  public void toJson() {
    assertEquals("{\"name\":\"Goku\",\"age\":7}", MapperUtil.toJson(student));
  }

  @Test
  public void toObject() throws Exception{
    Student student = MapperUtil.getXmlMapper().readValue("<student><name>Goku</name><age>7</age><test>xxx</test></student>", Student.class);
    log.info("student: {}", student);
    assertNotNull(student);
  }

  private void doTest(Map<String, Object> map) {
    // 1. Test equal, ignore order
    assertThat(expected, is(map));

    // 2. Test size
    assertThat(map.size(), is(2));

    // 3. Test map entry, best!
    assertThat(map, IsMapContaining.hasEntry("name", "Goku"));

    assertThat(map, not(IsMapContaining.hasEntry("age", "7")));

    // 4. Test map key
    assertThat(map, IsMapContaining.hasKey("name"));

    // 5. Test map value
    assertThat(map, IsMapContaining.hasValue("Goku"));
  }
}
