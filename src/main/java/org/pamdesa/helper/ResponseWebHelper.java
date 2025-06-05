package org.pamdesa.helper;

import org.pamdesa.model.entity.Organization;
import org.pamdesa.model.entity.Rate;
import org.pamdesa.model.entity.User;
import org.pamdesa.model.payload.response.UserInfoResponse;

public class ResponseWebHelper {

  public static UserInfoResponse toUserInfoResponse (User user) {
    return UserInfoResponse.builder()
        .id(user.getId())
        .rateId(CommonHelper.transformOrElseNull(user.getRate(), Rate::getId))
        .meterId(user.getMeterId())
        .email(user.getEmail())
        .phoneNumber(user.getPhoneNumber())
        .address(user.getAddress())
        .fullName(user.getFullName())
        .role(user.getUserRole().name())
        .organizationCode(CommonHelper.transformOrElseNull(
            user.getOrganization(), Organization::getCode))
        .organizationName(CommonHelper.transformOrElseNull(
            user.getOrganization(), Organization::getName))
        .build();
  }

}
