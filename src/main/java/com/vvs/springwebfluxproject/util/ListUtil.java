package com.vvs.springwebfluxproject.util;

import java.util.stream.Collector;
import java.util.stream.Collectors;

public class ListUtil {

  public static <T> Collector<T, ?, T> toSingleton() {
    return Collectors.collectingAndThen(Collectors.toList(), list -> {
      if (list.size() != 1) throw new IllegalArgumentException();
      return list.get(0);
    });
  }
}
