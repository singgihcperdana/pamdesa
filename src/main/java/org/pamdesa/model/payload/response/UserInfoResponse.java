package org.pamdesa.model.payload.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UserInfoResponse {
  private String id;
  private String username;
  private String email;
  private String fullName;
  private String phoneNumber;
  private String address;
  private String role;
  private String organizationName;
}
