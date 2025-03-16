package org.pamdesa.model.payload.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrganizationResponse {

  private String id;

  private String name;

  private String description;

  private String logo;

}
