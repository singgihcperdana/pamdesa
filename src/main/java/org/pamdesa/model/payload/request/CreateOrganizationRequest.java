package org.pamdesa.model.payload.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class CreateOrganizationRequest {

  @NotBlank(message = "NotBlank")
  private String name;

  @NotBlank(message = "NotBlank")
  private String description;

  @NotBlank(message = "NotBlank")
  private String address;

  private String logo;

}
