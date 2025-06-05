package org.pamdesa.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pamdesa.helper.ResponseWebHelper;
import org.pamdesa.model.entity.Rate;
import org.pamdesa.model.entity.User;
import org.pamdesa.model.enums.ErrorCode;
import org.pamdesa.model.enums.UserRole;
import org.pamdesa.model.error.ClientException;
import org.pamdesa.model.payload.request.BindAccountRequest;
import org.pamdesa.model.payload.request.CreateCustomerRequest;
import org.pamdesa.model.payload.response.UserInfoResponse;
import org.pamdesa.repository.RateRepository;
import org.pamdesa.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerService {

  private final UserRepository userRepository;

  private final UserService userService;

  private final RateRepository rateRepository;

  public User createCustomer(CreateCustomerRequest request) {
    User adminUser = this.getAndValidateAdminUser();
    if(adminUser.getUserRole() != UserRole.ADMIN) {
      log.error("#createCustomer role not valid");
      throw new ClientException(ErrorCode.BAD_CREDENTIAL);
    }

    Rate rate = rateRepository.findByCode(request.getRateCode())
        .orElseThrow(() -> new ClientException(ErrorCode.DATA_NOT_FOUND));
    return userRepository.save(User.builder()
            .meterId(request.getMeterId())
            .address(request.getAddress())
            .fullName(request.getFullName())
            .userRole(UserRole.CUSTOMER)
            .rate(rate)
            .organization(adminUser.getOrganization())
        .build());
  }

  public UserInfoResponse findCustomerById(String customerId) {
    User adminUser = this.getAndValidateAdminUser();
    User customer = userRepository.findByIdAndOrganization_Id(customerId, adminUser.getOrganization().getId())
        .orElseThrow(() -> new ClientException(ErrorCode.DATA_NOT_FOUND));
    return ResponseWebHelper.toUserInfoResponse(customer);
  }

  public User bindAccount(BindAccountRequest request) {
    User adminUser = this.getAndValidateAdminUser();
    User customer = userRepository.findByMeterIdAndOrganization_Id(request.getMeterId(), adminUser.getOrganization().getId())
        .orElseThrow(() -> new ClientException(ErrorCode.DATA_NOT_FOUND));
    customer.setEmail(customer.getEmail());
    return userRepository.save(customer);
  }

  private User getAndValidateAdminUser() {
    User adminUser = userService.getCurrentUser();
    if(adminUser.getUserRole() != UserRole.ADMIN) {
      log.error("#getAndValidateCurrentUser role not valid");
      throw new ClientException(ErrorCode.BAD_CREDENTIAL);
    }
    return adminUser;
  }

}
