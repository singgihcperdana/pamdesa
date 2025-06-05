package org.pamdesa.model.payload.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserInfoResponse {
  private String id;
  private String meterId;
  private String rateId;
  private String email;
  private String fullName;
  private String phoneNumber;
  private String address;
  private String role;
  private String organizationCode;
  private String organizationName;
}
