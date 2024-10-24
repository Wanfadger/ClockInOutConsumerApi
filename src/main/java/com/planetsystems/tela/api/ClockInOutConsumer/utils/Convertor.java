package com.planetsystems.tela.api.ClockInOutConsumer.utils;

import java.text.DecimalFormat;
import java.time.Duration;
import java.time.LocalTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;

public class Convertor {
  public static DecimalFormat RoundOff = new DecimalFormat("#.0");

  public static <T> Predicate<T> distinctByKey(
          Function<? super T, ?> keyExtractor) {

    Map<Object, Boolean> seen = new ConcurrentHashMap<>();
    return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
  }




}
