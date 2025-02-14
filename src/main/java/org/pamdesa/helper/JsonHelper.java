package org.pamdesa.helper;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JsonHelper {

  private final ObjectMapper objectMapper;

  @SneakyThrows
  public <T> T fromJson(String json, Class<T> tClass) {
    return objectMapper.readValue(json, tClass);
  }

  @SneakyThrows
  public <T> T fromJson(String json, TypeReference<T> tTypeReference) {
    return objectMapper.readValue(json, tTypeReference);
  }

  @SneakyThrows
  public <T> String toJson(T object) {
    return objectMapper.writeValueAsString(object);
  }

}
