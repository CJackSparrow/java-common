package com.github.icovn.util.security;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class HashUtilTest {

  private String key = "454f08bb1165f3c933fb514100d99f38";

  @Test
  public void hmacSHA256FirstTest() {
    String input = "EMAWdlqPzWkoRDeEZCOXmIz3yY6enDYr6gqI9h6a3WAEgJr7WZB7cTql5ZBZBRZAPAQ0JuJZBuZBPj8jZBxXjV9FSzjtKRs2AwBO9Asr3b0j6cQgeCwe2I3TuvAfutkR3Y2zh2HxzqbExiazYXSnGnu662YGnTsTDxZAWoZD";
    String expected = "a4dedec005fb387cd13374e9d8022290f6cc8bd8703c6d46fd9a2fbbfc8539cc";
    assertEquals(expected, HashUtil.hmacSHA256(key, input));
  }

  @Test
  public void hmacSHA256SecondTest() {
    String input = "EMAWcbRlXZA2zme0ArctF2ROIbZA01iSUKcZBmzktAqjX4ZCgWTHTyIhqvluB7N9vh0p19QkAu99VojOD40WycBtY8X9MbQhtSueJ7YhBpAuZBwUDUHS2SpSixVJYOE5XOvWYjUSI03dZA9zQwrkq2p6hxiV2TqKDWEZD";
    String expected = "a3d809bc90bc889202f0c1758888eb3fb04b502e97a78826e76f696079bf7122";
    assertEquals(expected, HashUtil.hmacSHA256(key, input));
  }
}