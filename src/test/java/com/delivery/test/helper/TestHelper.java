package com.delivery.test.helper;

import java.lang.reflect.InvocationTargetException;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TestHelper {

  private static final String letters = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
  private static final Random random = new Random();

  public static String getRandomId(String prefix) {
    return prefix + getRandomId();
  }

  public static String getRandomId() {
    return UUID.randomUUID().toString().substring(0, 8);
  }

  public static String getRandomText(int length) {
    return IntStream.range(0, length)
      .mapToObj(i -> letters.charAt(random.nextInt(letters.length())))
      .map(Objects::toString)
      .collect(Collectors.joining());
  }

  @SuppressWarnings("unchecked")
  public static <T extends Enum<T>> T getRandom(Class<T> enumClass) {
    try {
      T[] enumValues = (T[]) enumClass.getDeclaredMethod("values").invoke(null);
      int randomValue = new Random().nextInt(enumValues.length);
      return enumValues[randomValue];
    } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
      throw new RuntimeException(e);
    }
  }

}
