package org.pamdesa.model.payload.request;


import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class CreateCustomerRequest {

  @NotBlank
  private String rateCode;

  @NotBlank
  private String meterId;

  @NotBlank
  private String fullName;

  @NotBlank
  private String address;

}
