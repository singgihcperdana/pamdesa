package org.pamdesa.model.payload.request;


import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class SignupRequest extends CreateCustomerRequest {

  @NotBlank
  @Size(min = 3, max = 20)
  private String username;

  @Size(max = 50)
  @Email
  private String email;

  @Size(max = 15)
  private String phoneNumber;

  @NotBlank
  private String organizationId;

  @NotBlank
  @Size(min = 8, max = 40)
  private String password;

  @NotBlank
  @Size(min = 8, max = 40)
  private String rePassword;

}
