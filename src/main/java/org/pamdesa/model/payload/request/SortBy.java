package org.pamdesa.model.payload.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SortBy {

  @JsonProperty("property_name") private String propertyName;

  @JsonProperty("direction") private SortByDirection direction;
}
