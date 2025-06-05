package org.pamdesa.helper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommonHelper {

  public static<T, D> D transformOrElseNull(T data, Function<T, D> transformationFunc) {
    return transformOrElse(data, transformationFunc, null);
  }

  public static<T, D> D transformOrElse(T data, Function<T, D> transformationFunc, D fallbackValue) {
    return transformOrElseGet(Optional.ofNullable(data), transformationFunc, ()-> fallbackValue);
  }

  public static <T, D> D transformOrElseGet(
      Optional<T> optData, Function<T, D> transformationFunc, Supplier<D> fallbackValue) {
    return optData
        .map(transformationFunc)
        .orElseGet(fallbackValue);
  }

}
