package org.pamdesa.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pamdesa.helper.ResponseHelper;
import org.pamdesa.model.constant.AppPath;
import org.pamdesa.model.entity.User;
import org.pamdesa.model.payload.request.CreateCustomerRequest;
import org.pamdesa.model.payload.response.Response;
import org.pamdesa.model.payload.response.UserInfoResponse;
import org.pamdesa.service.CustomerService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(AppPath.ADMIN_CUSTOMER)
@RequiredArgsConstructor
@Validated
@Slf4j
public class AdminCustomerController {

  private final CustomerService customerService;

  @PostMapping
  public Response<String> createCustomer(@Validated @RequestBody CreateCustomerRequest request) {
    log.info("#createCustomer request: {}", request);
    User customer = customerService.createCustomer(request);
    log.info("#createCustomer response userId: {}", customer.getId());
    return ResponseHelper.ok(customer.getId());
  }

  @GetMapping
  public Response<UserInfoResponse> findCustomerById(@RequestParam("id") String id) {
    log.info("#findCustomerById id: {}", id);
    UserInfoResponse userInfoResponse = customerService.findCustomerById(id);
    log.info("#findCustomerById response userId: {}", userInfoResponse);
    return ResponseHelper.ok(userInfoResponse);
  }

}
